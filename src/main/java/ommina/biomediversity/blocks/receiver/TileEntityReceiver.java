package ommina.biomediversity.blocks.receiver;

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
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.blocks.cluster.IClusterComponent;
import ommina.biomediversity.blocks.collector.TileEntityCollector;
import ommina.biomediversity.blocks.tile.CollectorFinder;
import ommina.biomediversity.blocks.tile.TileEntityAssociation;
import ommina.biomediversity.blocks.transmitter.TileEntityTransmitter;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.config.Constants;
import ommina.biomediversity.fluids.BdFluidTank;
import ommina.biomediversity.network.BroadcastHelper;
import ommina.biomediversity.network.ITankBroadcast;
import ommina.biomediversity.network.Network;
import ommina.biomediversity.util.NbtUtils;
import ommina.biomediversity.world.chunkloader.ChunkLoader;
import ommina.biomediversity.worlddata.TransmitterData;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public class TileEntityReceiver extends TileEntityAssociation implements ITickableTileEntity, ITankBroadcast, IClusterComponent, INamedContainerProvider {

    private static final int TANK_COUNT = 1;
    private static final int MINIMUM_DELTA = 200;
    private static final float CHUNKLOAD_MIN_PERCENTAGE = 0.20f;
    private static final float CHUNKLOAD_MAX_PERCENTAGE = 0.80f;
    private static final int CHUNKLOAD_MIN_FLUID_INCREASE = 90;
    private static final int MAX_CHUNKLOAD_DURATION = 5 * 60 * 20 / Constants.CLUSTER_TICK_DELAY; // 5min

    final CollectorFinder COLLECTOR = new CollectorFinder();
    final BroadcastHelper BROADCASTER = new BroadcastHelper( TANK_COUNT, MINIMUM_DELTA, this );
    final BdFluidTank TANK = new BdFluidTank( Config.transmitterCapacity.get() );
    final EnergyStorage BATTERY = new EnergyStorage( Config.receiverRequirePowerToOperate.get() || Config.receiverRequirePowerToChunkload.get() ? Config.receiverPowerCapacity.get() : 0, Integer.MAX_VALUE, 0 );

    private int fluidHashCode;
    private int lastFluidAmount = 0;
    private int power;
    private String biomeRegistryName = "null:null";
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

    public static void handle( PacketUpdateReceiver packet, Supplier<NetworkEvent.Context> ctx ) {

        ctx.get().enqueueWork( () -> {

            Optional<World> world = LogicalSidedProvider.CLIENTWORLD.get( ctx.get().getDirection().getReceptionSide() );

            if ( world.get().isBlockLoaded( packet.tilePos ) ) {

                TileEntity tile = world.get().getTileEntity( packet.tilePos );

                if ( tile instanceof TileEntityReceiver ) {

                    TileEntityReceiver ter = (TileEntityReceiver) tile;

                    ter.TANK.setFluid( packet.fluid );
                    ter.COLLECTOR.setCollectorPos( packet.collectorPos );
                    ter.temperature = packet.temperature;
                    ter.biomeRegistryName = packet.biomeRegistryName;

                }

            }

        } );

        ctx.get().setPacketHandled( true );

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

    public String getBiomeRegistryName() {
        return this.biomeRegistryName;
    }

    @Nullable
    public TileEntityCollector getCollector() {
        return COLLECTOR.getCollector( world );

    }

    public float getTemperature() {
        return this.temperature;
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


    //public void resetSearchCount() {

    //searchAttemptCount = 0;
    //}

    private void doMainWork() {

        TileEntityCollector coll = getCollector();

        if ( coll == null ) {  // TODO: Can this block be moved inside CollectorFinder?

            if ( hasWorld() && COLLECTOR.getCollectorPos() != null && world.isBlockLoaded( COLLECTOR.getCollectorPos() ) )
                removeCollector();

            if ( COLLECTOR.getCollectorPos() == null && (loop % Constants.CLUSTER_SEARCH_ON_LOOP) == 0 ) {
                BlockPos pos = COLLECTOR.find( world, getPos() );
                if ( pos != null ) {
                    BROADCASTER.forceBroadcast();
                    markDirty();
                }
            }

            return;

        }

        if ( this.getAssociatedIdentifier() != null && this.getAssociatedPos() != null ) {

            world.getCapability( BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY, null ).ifPresent( cap -> {

                TransmitterData pd = cap.getTransmitter( this.getOwner(), this.getAssociatedIdentifier() );

                biomeRegistryName = pd.biomeId.toString();
                temperature = pd.temperature; // + getTemperatureAdjustment( pd.temperature ); // TODO: Peltier
                rainfall = pd.rainfall;

                if ( pd.fluid != null && pd.getAmount() >= Constants.CLUSTER_FLUID_CONSUMPTION ) {

                    if ( !coll.isCollectorTurnedOff() ) {

                        if ( !world.isBlockPowered( getPos() ) ) {

                            boolean isTransmitterLoaded = false;

                            int drainAmount = Constants.CLUSTER_FLUID_CONSUMPTION;

                            fluidHashCode = pd.fluid.hashCode();
                            power = FluidStrengths.getStrength( fluidHashCode );

                            coll.collect( getPos(), fluidHashCode, power, biomeRegistryName, temperature, rainfall ); //TODO: Make the collector do something

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

        doBroadcast();

        this.markDirty();

    }

    private boolean hasEnoughPowerToChunkload() {

        return !Config.receiverRequirePowerToChunkload.get() || BATTERY.getEnergyStored() >= Config.receiverPowerConsumptionChunloading.get() * Constants.CLUSTER_TICK_DELAY;

    }

    private void loadTransmitterChunk() {

        ChunkLoader.forceSingle( world, this.getAssociatedPos() );

        isChunkloadingTransmitter = true;
        chunkloadDurationRemaining = MAX_CHUNKLOAD_DURATION;

        BiomeDiversity.LOGGER.debug( "Loading transmitter at " + this.getAssociatedPos().toString() );

    }

    private void removeCollector() {

        COLLECTOR.setCollectorPos( null );
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

        BiomeDiversity.LOGGER.debug( "Unloading transmitter at " + this.getAssociatedPos().toString() );

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
            Network.channel.send( PacketDistributor.NEAR.with( () -> new PacketDistributor.TargetPoint( this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 64.0f, DimensionType.OVERWORLD ) ), new PacketUpdateReceiver( this ) );
            BROADCASTER.reset();
        }

    }

    @Override
    public BdFluidTank getTank( int index ) {

        return TANK;
    }

    @Override
    @Nullable
    public BlockPos getCollectorPos() {
        return COLLECTOR.getCollectorPos();
    }

    @Override
    public boolean isClusterComponentConnected() {
        return (COLLECTOR.getCollectorPos() != null);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent( getType().getRegistryName().getPath() );
    }

    @Override
    public boolean hasFastRenderer() {

        return true;
    }

    @Override
    public void read( CompoundNBT nbt ) {

        COLLECTOR.setCollectorPos( NbtUtils.getBlockPos( "collectorpos", nbt ) );

        super.read( nbt );

    }

    @Override
    public CompoundNBT write( CompoundNBT nbt ) {

        if ( COLLECTOR.getCollectorPos() != null )
            NbtUtils.putBlockPos( "collectorpos", nbt, COLLECTOR.getCollectorPos() );

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

}