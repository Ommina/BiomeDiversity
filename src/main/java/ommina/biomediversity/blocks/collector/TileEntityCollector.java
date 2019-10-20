package ommina.biomediversity.blocks.collector;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.blocks.cluster.IClusterComponent;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.config.Constants;
import ommina.biomediversity.fluids.BdFluidTank;
import ommina.biomediversity.fluids.ModFluids;
import ommina.biomediversity.network.BroadcastHelper;
import ommina.biomediversity.network.ITankBroadcast;
import ommina.biomediversity.network.Network;
import ommina.biomediversity.util.Pair;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class TileEntityCollector extends TileEntity implements IClusterComponent, ITickableTileEntity, ITankBroadcast {

    public static final int WARM = 0;
    public static final int COOL = 1;

    static final int TANK_COUNT = 12;
    private static final int MINIMUM_DELTA = 300;

    final List<BdFluidTank> TANK = new ArrayList<>( TANK_COUNT );

    private final Reception RECEPTOR = new Reception();
    private final EnergyStorage BATTERY = new EnergyStorage( Config.collectorEnergyCapacity.get(), 0, Integer.MAX_VALUE );
    private final BroadcastHelper BROADCASTER = new BroadcastHelper( TANK_COUNT, MINIMUM_DELTA, this );

    private int delay = Constants.CLUSTER_TICK_DELAY;
    private int storedEnergy;
    private int buffer = 0;
    private int lastRfCreated = 0;
    private int releasePerTick = 0;
    private int uniqueBiomeCount;
    private float temperature;

    public TileEntityCollector() {
        super( ModTileEntities.COLLECTOR );

        for ( int i = 0; i < TANK_COUNT; i++ ) {
            TANK.add( i, new BdFluidTank( i < 8 ? Constants.COLLECTOR_OUTER_TANK_CAPACITY : Constants.COLLECTOR_INNER_TANK_CAPACITY ) );
        }

        TANK.forEach( o -> o.setCanDrain( true ).setCanFill( false ) );

    }

    public static void handle( PacketUpdateCollector packet, Supplier<NetworkEvent.Context> ctx ) {

        ctx.get().enqueueWork( () -> {

            Optional<World> world = LogicalSidedProvider.CLIENTWORLD.get( ctx.get().getDirection().getReceptionSide() );

            if ( world.get().isBlockLoaded( packet.tilePos ) ) {

                TileEntity tile = world.get().getTileEntity( packet.tilePos );

                if ( tile instanceof TileEntityCollector ) {

                    TileEntityCollector ter = (TileEntityCollector) tile;

                    for ( int n = 0; n < TANK_COUNT; n++ )
                        ter.TANK.get( n ).setFluid( packet.fluids[n] );


                    //ter.collectorPos = packet.collectorPos;
                    //ter.temperature = packet.temperature;
                    //ter.biomeRegistryName = packet.biomeRegistryName;

                    //if ( ter.collectorPos != null && world.get().isBlockLoaded( ter.collectorPos ) ) {
                    //    ter.collector = (TileEntityCollector) world.get().getTileEntity( ter.collectorPos );
                    //}

                }

            }

        } );

        ctx.get().setPacketHandled( true );

    }

    public boolean canCollectorAcceptEnergyPulse() {

        //Biomediversity.logger.warn( " config: " + Config.collectorSelfThrottleEnabled );
        //Biomediversity.logger.warn( " batteryStored: " + BATTERY.getEnergyStored() );
        //Biomediversity.logger.warn( " batteryMax: " + BATTERY.getMaxEnergyStored() );
        //Biomediversity.logger.warn( " buffer: " + buffer );
        //Biomediversity.logger.warn( " lastRf: " + lastRfCreated );

        return !Config.collectorIsSelfThrottleEnabled.get() || BATTERY.getEnergyStored() + buffer + lastRfCreated < BATTERY.getMaxEnergyStored();

    }

    public void collect( BlockPos receiverPos, int fluidHash, int power, String biomeRegistryName, float temperature, float rainfall ) {

        RECEPTOR.add( biomeRegistryName, fluidHash, temperature, power );

    }

    public boolean isCollectorTurnedOff() {
        return getWorld().isBlockPowered( getPos() );
    }

    //region Overrides
    @Override
    public void doBroadcast() {

        if ( BROADCASTER.needsBroadcast() ) {
            Network.channel.send( PacketDistributor.NEAR.with( () -> new PacketDistributor.TargetPoint( this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 64.0f, DimensionType.OVERWORLD ) ), new PacketUpdateCollector( this ) );
            BROADCASTER.reset();
        }

    }

    @Override
    public BdFluidTank getTank( int index ) {
        return TANK.get( index );
    }

    @Override
    @Nullable
    public BlockPos getCollectorPos() {
        return this.getPos();
    }

    @Override
    public boolean isClusterComponentConnected() {
        return true;
    }

    @Override
    public void tick() {

        if ( world.isRemote || isCollectorTurnedOff() )
            return;

        delay--;

/*

        if ( BATTERY.getEnergyStored() > 0 ) {

            for ( EnumFacing f : energyFacings ) { // This does mean that certain directions (in the order of the enum) will get priority for energy reception
                TileEntity te = world.getTileEntity( getPos().offset( f ) );
                if ( te != null && te.hasCapability( CapabilityEnergy.ENERGY, f.getOpposite() ) ) {
                    int receive = te.getCapability( CapabilityEnergy.ENERGY, f.getOpposite() ).receiveEnergy( BATTERY.getEnergyStored(), true );
                    if ( receive > 0 ) {
                        te.getCapability( CapabilityEnergy.ENERGY, f.getOpposite() ).receiveEnergy( receive, false );
                        BATTERY.extractEnergy( receive, false );
                    }
                    if ( BATTERY.getEnergyStored() <= 0 )
                        break;
                }
            }

        }

*/


        buffer -= BATTERY.receiveEnergy( releasePerTick, false );

        doBroadcast();

        //System.out.println( "delay: " + delay + ", " + "buffer: " + buffer + ", lastRfCreatred: " + lastRfCreated + ", releasePerTick: " + releasePerTick );

        if ( delay > 0 )
            return;

        delay = Constants.CLUSTER_TICK_DELAY;

        if ( !canCollectorAcceptEnergyPulse() )
            return;

        Emission miss = RECEPTOR.emit();

        lastRfCreated = miss.getEnergy();

        if ( lastRfCreated > 0 ) {
            uniqueBiomeCount = miss.getHits();
            temperature = miss.getTemperatureTotal();
        }

        //int energyStored = BATTERY.receiveEnergyInternal( lastRfCreated, false );

        if ( buffer <= Constants.CLUSTER_TICK_DELAY + 1 ) { // All the energy was stored -- create the fluids.

            Pair fluids = miss.getFluidCreated();

            if ( fluids.a > 0 )
                TANK.get( WARM ).fill( new FluidStack( ModFluids.WARMBIOMETIC, fluids.a ), IFluidHandler.FluidAction.EXECUTE );

            if ( fluids.b > 0 )
                TANK.get( COOL ).fill( new FluidStack( ModFluids.COOLBIOMETIC, fluids.b ), IFluidHandler.FluidAction.EXECUTE );

        } else {
            //System.out.println( "buffer: " + buffer + ", lastRfCreatred: " + lastRfCreated );
        }

        buffer += lastRfCreated;
        releasePerTick = buffer / Constants.CLUSTER_TICK_DELAY;

        markDirty();

    }

//endregion Overrides

}
