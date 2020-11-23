package ommina.biomediversity.blocks.receiver;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.blocks.cluster.ICollectorComponent;
import ommina.biomediversity.blocks.collector.TileEntityCollector;
import ommina.biomediversity.blocks.tile.CollectorFinder;
import ommina.biomediversity.blocks.tile.TileEntityAssociation;
import ommina.biomediversity.blocks.transmitter.TileEntityTransmitter;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.config.Constants;
import ommina.biomediversity.fluids.BdFluidTank;
import ommina.biomediversity.fluids.FluidStrengths;
import ommina.biomediversity.network.BroadcastHelper;
import ommina.biomediversity.network.GenericTilePacketRequest;
import ommina.biomediversity.network.ITankBroadcast;
import ommina.biomediversity.network.Network;
import ommina.biomediversity.rendering.RenderHelper;
import ommina.biomediversity.util.NbtUtils;
import ommina.biomediversity.world.chunkloader.ChunkLoader;
import ommina.biomediversity.worlddata.TransmitterData;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Optional;
import java.util.function.Supplier;

public class TileEntityReceiver extends TileEntityAssociation implements ITickableTileEntity, ITankBroadcast, ICollectorComponent, INamedContainerProvider {

    private static final int TANK_COUNT = 1;
    private static final int MINIMUM_DELTA = 200;
    private static final float CHUNKLOAD_MIN_PERCENTAGE = 0.20f;
    private static final float CHUNKLOAD_MAX_PERCENTAGE = 0.80f;
    private static final int CHUNKLOAD_MIN_FLUID_INCREASE = 90;
    private static final int MAX_CHUNKLOAD_DURATION = 5 * 60 * 20 / Constants.CLUSTER_TICK_DELAY; // 5min
    private static final float[] GLOWBAR_COLOUR_CHUNKLOADING = RenderHelper.getRGBA( new Color( 255, 165, 0 ).getRGB() );
    private static final float[] GLOWBAR_COLOUR_DISCONNECTED = RenderHelper.getRGBA( new Color( 254, 0, 0, 255 ).getRGB() );
    private static final float[] GLOWBAR_COLOUR_CONNECTED = RenderHelper.getRGBA( new Color( 0, 200, 0, 255 ).getRGB() );
    final CollectorFinder FINDER = new CollectorFinder();
    final BroadcastHelper BROADCASTER = new BroadcastHelper( TANK_COUNT, MINIMUM_DELTA, this );
    final BdFluidTank TANK = new BdFluidTank( 0, Config.transmitterCapacity.get() );
    final EnergyStorage BATTERY = new EnergyStorage( Config.receiverRequirePowerToOperate.get() || Config.receiverRequirePowerToChunkload.get() ? Config.receiverPowerCapacity.get() : 0, Integer.MAX_VALUE, 0 );
    private int fluidHashCode;
    private int lastFluidAmount = 0;
    private int power;
    private float temperature;
    private float rainfall;
    private int chunkloadDurationRemaining = 0;
    private boolean chunkloadingTimedOut = false;
    private int delay = Constants.CLUSTER_TICK_DELAY;
    private int loop = 1;
    private boolean isChunkloadingTransmitter = false;

    public TileEntityReceiver() {
        super( ModTileEntities.RECEIVER );
    }

    //region Overrides
    @Nullable
    @Override
    public Container createMenu( int i, PlayerInventory playerInventory, PlayerEntity playerEntity ) {
        return new ReceiverContainer( i, world, pos, playerInventory, playerEntity );
    }

    @Override
    public void doBroadcast() {

        if ( BROADCASTER.needsBroadcast() ) {
            Network.channel.send( PacketDistributor.NEAR.with( () -> new PacketDistributor.TargetPoint( this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 64.0f, World.OVERWORLD ) ), new PacketUpdateReceiver( this ) );
            BROADCASTER.reset();
        }

        //Network.channel.sendTo( new PacketUpdateReceiver( this ), NetworkManager., NetworkDirection.PLAY_TO_CLIENT );

        //INSTANCE.sendTo(packet, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);

    }

    @Override
    public BdFluidTank getTank( int index ) {
        return TANK;
    }

    @Override
    @Nullable
    public BlockPos getCollectorPos() {
        return FINDER.getCollectorPos();
    }

    @Override
    public boolean isClusterComponentConnected() {
        return (FINDER.getCollectorPos() != null);
    }

    @Override
    public void invalidateCollector() {
        removeCollector();
    }

    @Override
    public void registerSelf() {

        CollectorFinder.GetCollectorResult result = FINDER.getCollector( world );

        if ( result.getCollector() != null ) {
            result.getCollector().registerComponent( this );
            FINDER.markAsRegistered();
        } else
            BiomeDiversity.LOGGER.error( "Component is marked as ShouldRegister, but Collector TileEntity is null" );

    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent( getType().getRegistryName().getPath() );
    }

    @Override
    public void handleUpdateTag( BlockState state, CompoundNBT tag ) {
        BiomeDiversity.LOGGER.warn( "Moo!  Again.  " + tag.toString() );
    }

    @Override
    public void onChunkUnloaded() {

        TileEntityCollector collector = FINDER.get( world );

        if ( collector != null )
            collector.deregisterComponent( this );

    }

    @Override
    public void onLoad() {

        if ( world.isRemote )
            Network.channel.sendToServer( new GenericTilePacketRequest( this.pos ) );

    }

    @Override
    public void read( BlockState blockState, CompoundNBT nbt ) {
        super.read( blockState, nbt );

        FINDER.setCollectorPos( NbtUtils.getBlockPos( "collectorpos", nbt ) );

    }

    @Override
    public CompoundNBT write( CompoundNBT nbt ) {

        if ( FINDER.getCollectorPos() != null )
            NbtUtils.putBlockPos( "collectorpos", nbt, FINDER.getCollectorPos() );

        return super.write( nbt );

    }

    @Override
    protected void doFirstTick() {
        super.doFirstTick();

        if ( !world.isRemote ) {

            if ( this.getOwner() == null )
                BiomeDiversity.LOGGER.warn( "Receiver has null owner at: " + this.getPos() );
            else
                refreshReceiverTankFromTransmitterNetwork();
        }

        BROADCASTER.reset();

    }

    @Override
    public void tick() {

        if ( firstTick )
            doFirstTick();

        delay--;

        if ( delay > 0 )
            return;

        delay = Constants.CLUSTER_TICK_DELAY;

        if ( world.isRemote || world.isBlockPowered( getPos() ) )
            return;

        doMainWork();

        loop++;

    }
//endregion Overrides

    public static void handle( PacketUpdateReceiver packet, Supplier<NetworkEvent.Context> ctx ) {

        ctx.get().enqueueWork( () -> {

            Optional<World> world = LogicalSidedProvider.CLIENTWORLD.get( ctx.get().getDirection().getReceptionSide() );

            if ( world.get().isBlockLoaded( packet.tilePos ) ) {

                TileEntity tile = world.get().getTileEntity( packet.tilePos );

                if ( tile instanceof TileEntityReceiver ) {

                    TileEntityReceiver ter = (TileEntityReceiver) tile;

                    ter.TANK.setFluid( packet.fluid );
                    ter.FINDER.setCollectorPos( packet.collectorPos );
                    ter.temperature = packet.temperature;
                    ter.biomeRegistryName = packet.biomeRegistryName;
                    ter.isChunkloadingTransmitter = packet.isChunkloadingTransmitter;

                }

            }

        } );

        ctx.get().setPacketHandled( true );

    }

    public float[] getGlowbarColour() {

        if ( isChunkloadingTransmitter )
            return GLOWBAR_COLOUR_CHUNKLOADING;

        if ( isClusterComponentConnected() )
            return GLOWBAR_COLOUR_CONNECTED;

        return GLOWBAR_COLOUR_DISCONNECTED;

    }

    public EnergyStorage clientGetBattery() {
        return BATTERY;
    }

    public void doChunkloading() {

        if ( !Config.receiverEnableChunkLoading.get() || this.getAssociatedPos() == null )
            return;

        chunkloadDurationRemaining--;

        if ( !isChunkloadingTransmitter && (float) this.getTank( 0 ).getFluidAmount() / (float) Config.transmitterCapacity.get() <= CHUNKLOAD_MIN_PERCENTAGE && !chunkloadingTimedOut && hasEnoughPowerToChunkload() )
            loadTransmitterChunk();

        else if ( isChunkloadingTransmitter && ((float) this.getTank( 0 ).getFluidAmount() / (float) Config.transmitterCapacity.get() >= CHUNKLOAD_MAX_PERCENTAGE || chunkloadDurationRemaining == 0 || !hasEnoughPowerToChunkload()) )
            unloadTransmitterChunk();

    }

    public CollectorFinder.GetCollectorResult getCollector() {
        return FINDER.getCollector( world );
    }

    public float getTemperature() {
        return this.temperature;
    }

    public boolean isChunkloadingTransmitter() {
        return this.isChunkloadingTransmitter;
    }

    public void refreshReceiverTankFromTransmitterNetwork() {

        world.getCapability( BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY, null ).ifPresent( cap -> {

            TransmitterData pd = cap.getTransmitter( this.getOwner(), this.getIdentifier() );

            if ( pd.fluid != null ) {

                this.getTank( 0 ).setFluid( new FluidStack( pd.fluid, pd.getAmount() ) );

                lastFluidAmount = pd.getAmount();

                if ( !FluidStrengths.contains( pd.fluid.hashCode() ) ) {
                    BiomeDiversity.LOGGER.warn( "Fluid network contains a fluid with hash " + pd.fluid.hashCode() + ", but it is not in the config.  Perhaps it was removed?  Setting power to 1.  BiomeId: " + pd.biomeId );
                    power = 1;
                } else {
                    power = FluidStrengths.getStrength( pd.fluid.hashCode() );
                }

                biomeRegistryName = pd.biomeId.toString();
                rainfall = pd.rainfall;

            }

        } );

    }

    public void resetChunkloadDuration() {

        chunkloadDurationRemaining = MAX_CHUNKLOAD_DURATION;
        chunkloadingTimedOut = false;
    }

    private void doMainWork() {

        CollectorFinder.GetCollectorResult collectorResult = getCollector();

        if ( collectorResult.getCollector() == null ) {

            if ( collectorResult.isCollectorMissing() )
                removeCollector();

            if ( FINDER.getCollectorPos() == null && (loop % Constants.CLUSTER_SEARCH_ON_LOOP) == 0 ) {
                BlockPos pos = FINDER.find( world, getPos() );
                if ( pos != null ) {
                    BROADCASTER.forceBroadcast();
                    markDirty();
                }
            }

        } else {

            if ( FINDER.shouldRegister() ) {
                registerSelf();
            }

            TileEntityCollector collector = collectorResult.getCollector();

            if ( this.getAssociatedIdentifier() != null && this.getAssociatedPos() != null ) {

                world.getCapability( BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY, null ).ifPresent( cap -> {

                    TransmitterData pd = cap.getTransmitter( this.getOwner(), this.getAssociatedIdentifier() );

                    biomeRegistryName = pd.biomeId.toString();
                    temperature = pd.temperature; // + getTemperatureAdjustment( pd.temperature ); // TODO: Peltier
                    rainfall = pd.rainfall;

                    if ( pd.fluid != null && pd.getAmount() >= Constants.CLUSTER_FLUID_CONSUMPTION ) {

                        if ( !collector.isCollectorTurnedOff() && collector.canAcceptEnergyPulse() ) {

                            if ( !world.isBlockPowered( getPos() ) ) {

                                boolean isTransmitterLoaded = false;

                                int drainAmount = Constants.CLUSTER_FLUID_CONSUMPTION;

                                fluidHashCode = pd.fluid.hashCode();
                                power = FluidStrengths.getStrength( fluidHashCode );

                                //if ( power != 0 )
                                //    BiomeDiversity.LOGGER.warn( "power: " + power );

                                collector.collect( getPos(), fluidHashCode, power, biomeRegistryName, temperature, rainfall );

                                pd.drain( drainAmount );

                                isTransmitterLoaded = world.isBlockLoaded( this.getAssociatedPos() );

                                if ( isTransmitterLoaded ) {
                                    TileEntity te = world.getTileEntity( this.getAssociatedPos() );
                                    if ( te instanceof TileEntityTransmitter ) {
                                        TileEntityTransmitter tep = (TileEntityTransmitter) te;
                                        tep.getTank( 0 ).drain_internal( Constants.CLUSTER_FLUID_CONSUMPTION, IFluidHandler.FluidAction.EXECUTE );
                                    }
                                }

                                if ( pd.getAmount() + drainAmount - lastFluidAmount >= CHUNKLOAD_MIN_FLUID_INCREASE )
                                    resetChunkloadDuration();

                                lastFluidAmount = pd.getAmount();

                            }
                        }

                        if ( pd.fluid != null && pd.getAmount() >= Constants.CLUSTER_FLUID_CONSUMPTION )
                            this.getTank( 0 ).setFluid( new FluidStack( pd.fluid, pd.getAmount() ) );
                        else
                            this.getTank( 0 ).setFluid( FluidStack.EMPTY );

                        world.notifyNeighborsOfStateChange( getPos(), this.getBlockState().getBlock() );

                    }


                } );

                doChunkloading();

            }

        }

        doBroadcast();

        markDirty();

    }

    private boolean hasEnoughPowerToChunkload() {
        return !Config.receiverRequirePowerToChunkload.get() || BATTERY.getEnergyStored() >= Config.receiverPowerConsumptionChunloading.get() * Constants.CLUSTER_TICK_DELAY;
    }

    private void loadTransmitterChunk() {

        ChunkLoader.forceSingle( world, this.getAssociatedPos() );

        isChunkloadingTransmitter = true;
        chunkloadDurationRemaining = MAX_CHUNKLOAD_DURATION;

        BROADCASTER.forceBroadcast();

        BiomeDiversity.LOGGER.debug( "Loading transmitter at " + this.getAssociatedPos().toString() );

    }

    private void removeCollector() {

        FINDER.setCollectorPos( null );
        BROADCASTER.forceBroadcast();
        doBroadcast();
        markDirty();

    }

    private void unloadTransmitterChunk() {

        if ( !isChunkloadingTransmitter )
            return;

        ChunkLoader.releaseSingle( world, this.getAssociatedPos() );
        isChunkloadingTransmitter = false;

        chunkloadingTimedOut = (chunkloadDurationRemaining == 0);

        BROADCASTER.forceBroadcast();

        BiomeDiversity.LOGGER.debug( "Unloading transmitter at " + this.getAssociatedPos().toString() );

    }

}