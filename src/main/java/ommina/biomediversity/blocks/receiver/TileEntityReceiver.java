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
import ommina.biomediversity.blocks.collector.IClusterComponent;
import ommina.biomediversity.blocks.collector.TileEntityCollector;
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
    private static final int SEARCH_ON_LOOP = 5;
    private static final int MAX_SEARCH_COUNT = 36000 / (Constants.CLUSTER_TICK_DELAY * SEARCH_ON_LOOP); // ~30min

    final BroadcastHelper BROADCASTER = new BroadcastHelper( TANK_COUNT, MINIMUM_DELTA, this );
    final BdFluidTank TANK = new BdFluidTank( Config.transmitterCapacity.get() );
    final EnergyStorage BATTERY = new EnergyStorage( Config.receiverRequirePowerToOperate.get() || Config.receiverRequirePowerToChunkload.get() ? Config.receiverPowerCapacity.get() : 0, Integer.MAX_VALUE, 0 );

    private int searchAttemptCount = 0;
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
    private TileEntityCollector collector;
    private BlockPos collectorPos;
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
                    ter.collectorPos = packet.collectorPos;
                    ter.temperature = packet.temperature;
                    ter.biomeRegistryName = packet.biomeRegistryName;

                    if ( ter.collectorPos != null && world.get().isBlockLoaded( ter.collectorPos ) ) {
                        ter.collector = (TileEntityCollector) world.get().getTileEntity( ter.collectorPos );
                    }

                }

            }

        } );

        ctx.get().setPacketHandled( true );

    }

    public EnergyStorage clientGetBattery() {
        return BATTERY;
    }

    public void doChunkloading() {

        if ( !Config.receiverEnableChunkLoading.get() || this.getAssociatedPos() == null || collector == null )
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

    public void resetSearchCount() {

        searchAttemptCount = 0;
    }

/*

    private boolean checkCollectorTeAtCollectorPos() {

        // Logic for unloaded collector is different than a collector that just isn't found.
        // If pos is set, and there's no collector there, unset the pos, clear the nbt, and clear the field so it will start searching
        // If collector is just unloaded... stall / hang out.  It might still exist, but until it's loaded, we're stuck.

        if ( collectorPos == null || !hasWorld() || !world.isBlockLoaded( collectorPos ) )
            return false;

        TileEntity te = getWorld().getTileEntity( collectorPos );

        if ( te instanceof TileEntityCollector ) {
            collector = (TileEntityCollector) te;
            markDirty();
            return true;
        }

        removeCollector();

        return false;

    }

*/

    private boolean collectorExistsAtCollectorPos() {

        if ( !hasWorld() || !world.isBlockLoaded( collectorPos ) ) {
            return true;
        }

        return (getWorld().getTileEntity( collectorPos ) instanceof TileEntityCollector);

    }

    private void doMainWork() {

        if ( this.getAssociatedIdentifier() != null && this.getAssociatedPos() != null ) {

            world.getCapability( BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY, null ).ifPresent( cap -> {

                TransmitterData pd = cap.getTransmitter( this.getOwner(), this.getAssociatedIdentifier() );

                biomeRegistryName = pd.biomeId.toString();
                temperature = pd.temperature; // + getTemperatureAdjustment( pd.temperature ); // TODO: Peltier
                rainfall = pd.rainfall;

                if ( pd.fluid != null && pd.getAmount() >= Constants.CLUSTER_FLUID_CONSUMPTION ) {

                    if ( this.collector != null && !this.collector.isCollectorTurnedOff() ) {

                        if ( !world.isBlockPowered( getPos() ) ) {

                            boolean isTransmitterLoaded = false;

                            int drainAmount = Constants.CLUSTER_FLUID_CONSUMPTION;

                            fluidHashCode = pd.fluid.hashCode();
                            power = FluidStrengths.getStrength( fluidHashCode );

                            collector.collect( getPos(), fluidHashCode, power, biomeRegistryName, temperature, rainfall ); //TODO: Make the collector do something

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

        setCollector();

        if ( this.collector == null && (loop % SEARCH_ON_LOOP) == 0 ) {
            findCollector();
        }

        doBroadcast();

        this.markDirty();

    }

    private boolean findCollector() {

        if ( collectorPos != null && collectorExistsAtCollectorPos() ) {
            setCollector();
            return true;
        }

        if ( searchAttemptCount > MAX_SEARCH_COUNT )
            return false;

        searchAttemptCount++;

        int posX = getPos().getX();
        int posY = getPos().getY();
        int posZ = getPos().getZ();

        for ( int y = posY - Config.receiverCollectorSearchVerticalNeg.get(); y <= posY + Config.receiverCollectorSearchVerticalPos.get(); y++ )
            for ( int x = posX - Config.receiverCollectorSearchHorizontal.get(); x <= posX + Config.receiverCollectorSearchHorizontal.get(); x++ )
                for ( int z = posZ - Config.receiverCollectorSearchHorizontal.get(); z <= posZ + Config.receiverCollectorSearchHorizontal.get(); z++ ) {
                    BlockPos bp = new BlockPos( x, y, z );
                    if ( world.isBlockLoaded( bp ) ) {
                        TileEntity te = world.getTileEntity( bp );
                        if ( te instanceof IClusterComponent ) {
                            IClusterComponent tecc = (IClusterComponent) te;
                            if ( tecc.isClusterComponentConnected() ) {
                                this.collector = tecc.getCollector();
                                this.collectorPos = tecc.getCollector().getPos();
                                markDirty();
                                BROADCASTER.forceBroadcast();
                                return true;
                            }
                        }
                    }
                }

        return false;

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

        BROADCASTER.forceBroadcast();

        collectorPos = null;
        collector = null;
        markDirty();

    }

    private void setCollector() {

        if ( collectorPos == null || !collectorExistsAtCollectorPos() )
            removeCollector();
        else if ( collector == null && hasWorld() && world.isBlockLoaded( collectorPos ) )
            collector = (TileEntityCollector) getWorld().getTileEntity( collectorPos );
        else
            return;

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

    @Nullable
    @Override
    public TileEntityCollector getCollector() {
        return this.collector;
    }

    @Override
    public boolean isClusterComponentConnected() {
        return (this.collector != null);
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

        collectorPos = NbtUtils.getBlockPos( "collectorpos", nbt );

        super.read( nbt );

    }

    @Override
    public CompoundNBT write( CompoundNBT nbt ) {

        if ( collectorPos != null )
            NbtUtils.putBlockPos( "collectorpos", nbt, collectorPos );

        return super.write( nbt );

    }

    @Override
    protected void doFirstTick() {
        super.doFirstTick();

        if ( !world.isRemote ) {

            setCollector();

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