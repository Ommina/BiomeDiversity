package ommina.biomediversity.blocks.receiver;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
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
import ommina.biomediversity.network.GenericTankPacket;
import ommina.biomediversity.network.ITankBroadcast;
import ommina.biomediversity.network.Network;
import ommina.biomediversity.world.chunkloader.ChunkLoader;
import ommina.biomediversity.worlddata.TransmitterData;

import java.util.UUID;

public class TileEntityReceiver extends TileEntityAssociation implements ITickableTileEntity, ITankBroadcast, IClusterComponent {

    private static final int TANK_COUNT = 1;
    private static final int MINIMUM_DELTA = 200;
    private static final float CHUNKLOAD_MIN_PERCENTAGE = 0.20f;
    private static final float CHUNKLOAD_MAX_PERCENTAGE = 0.80f;
    private static final int CHUNKLOAD_MIN_FLUID_INCREASE = 90;
    private static final int MAX_CHUNKLOAD_DURATION = 5 * 60 * 20 / Constants.RECEIVER_TICK_DELAY; // 5min
    private static final int SEARCH_ON_LOOP = 5;
    private static final int MAX_SEARCH_COUNT = 36000 / (Constants.RECEIVER_TICK_DELAY * SEARCH_ON_LOOP); // ~30min
    private final BroadcastHelper BROADCASTER = new BroadcastHelper( TANK_COUNT, MINIMUM_DELTA, this );
    private final BdFluidTank TANK = new BdFluidTank( Config.transmitterCapacity.get() );
    private int searchAttemptCount = 0;
    private int fluidHash;
    private int lastAmount = 0;
    private int power;
    private String biomeId;
    private float temperature;
    private float rainfall;
    private int chunkloadDurationRemaining = 0;
    private boolean chunkloadingTimedOut = false;
    private int delay = Constants.RECEIVER_TICK_DELAY;
    private int loop = 1;
    private TileEntityCollector collector = new TileEntityCollector();
    private BlockPos collectorPos;
    private UUID chunkloadTicket = null;

    public TileEntityReceiver() {
        super( ModTileEntities.RECEIVER );
    }

    @Override
    public TileEntityCollector getCollector() {
        return this.collector;
    }

    @Override
    public boolean isClusterComponentConnected() {
        return (this.collector != null);
    }

    @Override
    public void tick() {

        if ( firstTick )
            doFirstTick();

        delay--;

        if ( delay > 0 )
            return;

        delay = Constants.RECEIVER_TICK_DELAY;

        if ( world.isRemote || world.isBlockPowered( getPos() ) )
            return;

        doMainWork();

        loop++;

    }

    @Override
    protected void doFirstTick() {
        super.doFirstTick();

        if ( !world.isRemote ) {
            if ( this.getOwner() == null )
                BiomeDiversity.LOGGER.warn( "Receiver has null owner at: " + this.getPos() );
            else
                refreshReceiverTankFromPillarNetwork();
        }

        BROADCASTER.reset();

    }

    private void doMainWork() {

        if ( !world.isRemote ) {

            if ( this.getAssociatedIdentifier() != null ) {

                //if ( this.getOwner() == null && this.getAssociatedPos() != null )
                //    repairOwner( this.getAssociatedPos() );

                world.getCapability( BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY, null ).ifPresent( cap -> {

                    TransmitterData pd = cap.getTransmitter( this.getOwner(), this.getAssociatedIdentifier() );

                    if ( pd != null ) {

                        // biomeId = pd.biomeId; // TODO: BiomeID used to be an id, and is now a ResourceLocation.  How should the collector deal with this?
                        //temperature = pd.temperature + getTemperatureAdjustment( pd.temperature ); // TODO: Peltier
                        rainfall = pd.rainfall;

                        // BROADCASTER.setTemperature( client_temperature ); // TODO: Network Packet

                        if ( pd.fluid != null && pd.getAmount() >= Constants.RECEIVER_CONSUMPTION ) {

                            if ( collector != null && !collector.isCollectorTurnedOff() ) {

                                if ( !world.isBlockPowered( getPos() ) ) {

                                    boolean isTransmitterLoaded = false;

                                    int drainAmount = Constants.RECEIVER_CONSUMPTION;

                                    fluidHash = pd.fluid.hashCode();
                                    power = FluidStrengths.getStrength( fluidHash );

                                    // collector.collect( hash, fluidHash, power, biomeId, temperature, rainfall ); //TODO: Make the collector do something

                                    pd.drain( drainAmount );

                                    isTransmitterLoaded = world.isBlockLoaded( this.getAssociatedPos() );

                                    if ( isTransmitterLoaded ) {
                                        TileEntity te = world.getTileEntity( this.getAssociatedPos() );
                                        if ( te != null && te instanceof TileEntityTransmitter ) {
                                            TileEntityTransmitter tep = (TileEntityTransmitter) te;
                                            tep.getTank( 0 ).drain_internal( Constants.RECEIVER_CONSUMPTION, IFluidHandler.FluidAction.EXECUTE );
                                        }
                                    }

                                    if ( pd.getAmount() + drainAmount - lastAmount >= CHUNKLOAD_MIN_FLUID_INCREASE )
                                        resetChunkloadDuration();

                                    lastAmount = pd.getAmount();

                                }
                            }

                            if ( pd.fluid != null && pd.getAmount() >= Constants.RECEIVER_CONSUMPTION )
                                this.getTank( 0 ).setFluid( new FluidStack( pd.fluid, pd.getAmount() ) );
                            else
                                this.getTank( 0 ).setFluid( FluidStack.EMPTY );

                            world.notifyNeighborsOfStateChange( getPos(), this.getBlockState().getBlock() );

                        }

                    }

                } );

                doChunkloading();

            }

            if ( collector == null && (loop % SEARCH_ON_LOOP) == 0 ) {
                findCollector();
                //updateBlockStateForLink( this, this.isLinked() );
            }

            doBroadcast();

            this.markDirty();

        }


    }

    public void refreshReceiverTankFromPillarNetwork() {

        world.getCapability( BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY, null ).ifPresent( cap -> {

            TransmitterData pd = cap.getTransmitter( this.getOwner(), this.getIdentifier() );

            if ( pd.fluid != null ) {

                this.getTank( 0 ).setFluid( new FluidStack( pd.fluid, pd.getAmount() ) );

                lastAmount = pd.getAmount();

                if ( !FluidStrengths.contains( pd.fluid.hashCode() ) ) {
                    BiomeDiversity.LOGGER.warn( "Fluid network contains a fluid with hash " + pd.fluid.hashCode() + ", but it is not in the config.  Perhaps it was removed?  Setting power to 1.00.  BiomeId: " + pd.biomeId );
                    power = 1;
                } else {
                    power = FluidStrengths.getStrength( pd.fluid.hashCode() );
                }

                biomeId = pd.biomeId.toString();
                temperature = pd.temperature;
                rainfall = pd.rainfall;

            }

        } );

    }

    public void resetChunkloadDuration() {

        chunkloadDurationRemaining = MAX_CHUNKLOAD_DURATION;
        chunkloadingTimedOut = false;
    }

    public void doChunkloading() {

        if ( !Config.receiverEnableChunkLoading.get() || this.getAssociatedPos() == null || collector == null )
            return;

        chunkloadDurationRemaining--;

        if ( chunkloadTicket == null && (float) this.getTank( 0 ).getFluidAmount() / (float) Config.transmitterCapacity.get() <= CHUNKLOAD_MIN_PERCENTAGE && !chunkloadingTimedOut )
            loadPillarChunk();

        else if ( chunkloadTicket != null && ((float) this.getTank( 0 ).getFluidAmount() / (float) Config.transmitterCapacity.get() >= CHUNKLOAD_MAX_PERCENTAGE || chunkloadDurationRemaining == 0) )
            unloadPillarChunk();

    }

    private boolean findCollector() {

        if ( collectorPos != null && getCollectorFromPos() )
            return true;

        if ( searchAttemptCount > MAX_SEARCH_COUNT )
            return false;

        searchAttemptCount++;

        int posX = getPos().getX();
        int posY = getPos().getY();
        int posZ = getPos().getZ();

        for ( int y = posY - Config.receiverCollectorSearchVertialNeg.get(); y <= posY + Config.receiverCollectorSearchVertialPos.get(); y++ )
            for ( int x = posX - Config.receiverCollectorSearchHorizontal.get(); x <= posX + Config.receiverCollectorSearchHorizontal.get(); x++ )
                for ( int z = posZ - Config.receiverCollectorSearchHorizontal.get(); z <= posZ + Config.receiverCollectorSearchHorizontal.get(); z++ ) {
                    BlockPos bp = new BlockPos( x, y, z );
                    if ( world.isBlockLoaded( bp ) ) {
                        TileEntity te = world.getTileEntity( bp );
                        if ( te instanceof IClusterComponent ) {
                            IClusterComponent tecc = (IClusterComponent) te;
                            if ( tecc.isClusterComponentConnected() ) {
                                this.collector = tecc.getCollector();
                                markDirty();
                                return true;
                            }
                        }
                    }
                }

        return false;

    }

    @Override
    public void doBroadcast() {

        if ( BROADCASTER.needsBroadcast() ) {
            Network.channel.send( PacketDistributor.NEAR.with( () -> new PacketDistributor.TargetPoint( this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 64.0f, DimensionType.OVERWORLD ) ), new GenericTankPacket( this ) );
            BROADCASTER.reset();
        }

    }

    @Override
    public BdFluidTank getTank( int index ) {

        return TANK;
    }

    private void loadPillarChunk() {

        chunkloadTicket = ChunkLoader.forceSingle( world, this.getAssociatedPos() );
        chunkloadDurationRemaining = MAX_CHUNKLOAD_DURATION;

        BiomeDiversity.LOGGER.debug( "Loading pillar at " + this.getAssociatedPos().toString() );

    }

    private void unloadPillarChunk() {

        if ( chunkloadTicket == null )
            return;

        ChunkLoader.releaseSingle( world, this.getAssociatedPos() );
        chunkloadTicket = null;

        chunkloadingTimedOut = chunkloadDurationRemaining == 0;

        BiomeDiversity.LOGGER.debug( "Unloading pillar at " + this.getAssociatedPos().toString() );

    }

    // End Chunkloading

    // Collector Finding

    private boolean getCollectorFromPos() {

        // Logic for unloaded collector is different than a collector that just isn't found.
        // If pos is set, and there's no collector there, unset the pos, clear the nbt, and clear the field so it will start searching
        // If collector is just unloaded... stall / hang out.  It might still exist, but until it's loaded, we're stuck.

        if ( collectorPos == null || !hasWorld() || !world.isBlockLoaded( collectorPos ) )
            return false;

        TileEntity te = getWorld().getTileEntity( collectorPos );

        if ( te != null && te instanceof TileEntityCollector ) {
            collector = (TileEntityCollector) te;
            markDirty();
            return true;
        }

        removeCollector();

        return false;

    }

    public void removeCollector() {

        collectorPos = null;
        collector = null;
        markDirty();

    }

    @Override
    public boolean hasFastRenderer() {

        return true;
    }

    public void resetSearchCount() {

        searchAttemptCount = 0;
    }

    // End Collector Finding


}