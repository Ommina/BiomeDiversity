package ommina.biomediversity.blocks.receiver;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.PacketDistributor;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.blocks.tile.TileEntityAssociation;
import ommina.biomediversity.blocks.transmitter.TileEntityTransmitter;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.fluids.BdFluidTank;
import ommina.biomediversity.network.BroadcastHelper;
import ommina.biomediversity.network.GenericTankPacket;
import ommina.biomediversity.network.ITankBroadcast;
import ommina.biomediversity.network.Network;
import ommina.biomediversity.worlddata.TransmitterData;

public class TileEntityReceiver extends TileEntityAssociation implements ITickableTileEntity, ITankBroadcast {

    private static final int TANK_COUNT = 1;
    private static final int MINIMUM_DELTA = 200;
    private final BroadcastHelper BROADCASTER = new BroadcastHelper( TANK_COUNT, MINIMUM_DELTA, this );
    private final BdFluidTank TANK = new BdFluidTank( Config.transmitterCapacity.get() );

    private int lastAmount = 0;
    private int power;
    private String biomeId;
    private float temperature;
    private float rainfall;

    private int delay = 20;// One Second Config.RECEIVER_UPDATE_DELAY;
    private int loop = 1;

    public TileEntityReceiver() {
        super( ModTileEntities.RECEIVER );
    }

    @Override
    public void tick() {

        if ( firstTick )
            doFirstTick();

        delay--;

        if ( delay > 0 )
            return;

        delay = 20;

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

                        if ( pd.fluid != null ) {

/*

                            if ( collector != null && !collector.isCollectorTurnedOff() ) {

                                if ( !world.isBlockPowered( getPos() ) ) {

                                    boolean isPillarLoaded = false;

                                    int drainAmount = Config.costInmbPerSecond;

                                    fluidHash = pd.fluid.hashCode();
                                    power = FluidStrengths.getStrength( fluidHash );

                                    collector.collect( hash, fluidHash, power, biomeId, temperature, rainfall );

                                    pd.drain( drainAmount );

                                    isPillarLoaded = world.isBlockLoaded( this.getAssociatedPos() );

                                    if ( isPillarLoaded ) {
                                        TileEntity te = world.getTileEntity( this.getAssociatedPos() );
                                        if ( te != null && te instanceof TileEntityPillar ) {
                                            TileEntityPillar tep = (TileEntityPillar) te;
                                            tep.getTank().drainInternal( drainAmount, true );
                                        }
                                    }

                                    if ( pd.getAmount() + drainAmount - lastAmount >= CHUNKLOAD_MIN_FLUID_INCREASE )
                                        resetChunkloadDuration();

                                    lastAmount = pd.getAmount();

                                }
                            }

*/

// ------------------------ REMOVE

                            boolean isPillarLoaded = false;

                            isPillarLoaded = world.isBlockLoaded( this.getAssociatedPos() );

                            if ( isPillarLoaded ) {
                                TileEntity te = world.getTileEntity( this.getAssociatedPos() );
                                if ( te != null && te instanceof TileEntityTransmitter ) {
                                    TileEntityTransmitter tep = (TileEntityTransmitter) te;
                                    tep.getTank( 0 ).drain_internal( 20, IFluidHandler.FluidAction.EXECUTE );
                                }
                            }

// ------------------------ REMOVE


                            this.getTank( 0 ).setFluid( new FluidStack( pd.fluid, pd.getAmount() - 20 ) );

                            world.notifyNeighborsOfStateChange( getPos(), this.getBlockState().getBlock() );

                        }

                    }

                } );

                // doChunkloading(); // TODO: Enable automatic chunkloading

            }

            //if ( collector == null && (loop % SEARCH_ON_LOOP) == 0 ) {
            //    findCollector();
            //    updateBlockStateForLink( this, this.isLinked() );
            //}

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

}