package ommina.biomediversity.blocks.collector;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.blocks.cluster.IClusterComponent;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.config.Constants;
import ommina.biomediversity.energy.BdEnergyStorage;
import ommina.biomediversity.fluids.BdFluidTank;
import ommina.biomediversity.fluids.ModFluids;
import ommina.biomediversity.network.GenericTilePacketRequest;
import ommina.biomediversity.network.ITankBroadcast;
import ommina.biomediversity.network.Network;
import ommina.biomediversity.util.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public class TileEntityCollector extends TileEntity implements IClusterComponent, ITickableTileEntity, ITankBroadcast {

    public static final int PLUG_CONNECTION_RF = 1;

    public static final int WARM = 0;
    public static final int COOL = 1;

    static final int TANK_COUNT = 12;
    private static final int MINIMUM_DELTA = 300;

    final List<BdFluidTank> TANK = new ArrayList<>( TANK_COUNT );
    final BdEnergyStorage BATTERY;

    private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of( this::createEnergyHandler );
    private final BroadcastHelper BROADCASTER;

    private final Reception RECEPTOR = new Reception();
    private final Set<IClusterComponent> components = new HashSet<>();

    private int delay = Constants.CLUSTER_TICK_DELAY;
    private int storedEnergy;
    private int buffer = 0;
    private int lastRfCreated = 0;
    private int releasePerTick = 0;
    private int uniqueBiomeCount;

    @Override
    public void invalidateCollector() {
        //nop
    }

    private float temperature;

    private boolean firstTick = true;

    @Override
    public void onChunkUnloaded() {

        components.forEach( IClusterComponent::invalidateCollector );
        components.clear();

    }

    public TileEntityCollector() {
        super( ModTileEntities.COLLECTOR );

        for ( int i = 0; i < TANK_COUNT; i++ ) {
            TANK.add( i, new BdFluidTank( i < 8 ? Constants.COLLECTOR_OUTER_TANK_CAPACITY : Constants.COLLECTOR_INNER_TANK_CAPACITY ) );
        }

        TANK.forEach( o -> o.setCanDrain( true ).setCanFill( false ) );

        BATTERY = (BdEnergyStorage) energyHandler.orElse( null );

        BROADCASTER = new BroadcastHelper( TANK_COUNT, MINIMUM_DELTA, this, BATTERY );

    }

    public void registerComponent( IClusterComponent component ) {
        components.add( component );
    }

    public void deregisterComponent( IClusterComponent component ) {
        components.remove( component );
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
    public void read( CompoundNBT nbt ) {

        BATTERY.setEnergyStored( nbt.getInt( "energystored" ) );

        super.read( nbt );

    }

    @Override
    public CompoundNBT write( CompoundNBT nbt ) {

        nbt.putInt( "energystored", BATTERY.getEnergyStored() );

        return super.write( nbt );

    }

    @Override
    public void tick() {

        if ( firstTick )
            doFirstTick();

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

        BATTERY.receiveEnergyInternal( releasePerTick, false );

        //buffer -= BATTERY.receiveEnergyInternal( releasePerTick, false );

        doBroadcast();

        //System.out.println( "delay: " + delay + ", " + "buffer: " + buffer + ", lastRfCreatred: " + lastRfCreated + ", releasePerTick: " + releasePerTick );

        if ( delay > 0 )
            return;

        delay = Constants.CLUSTER_TICK_DELAY;

        if ( !canAcceptEnergyPulse() )
            return;

        Emission miss = RECEPTOR.emit();

        lastRfCreated = miss.getEnergy();

        if ( lastRfCreated > 0 ) {
            uniqueBiomeCount = miss.getHits();
            temperature = miss.getTemperatureTotal();
        }

        BiomeDiversity.LOGGER.warn( "lastRf: " + lastRfCreated + " at " + pos.toString() );

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

    public LazyOptional<IEnergyStorage> getEnergyHandler() {
        return energyHandler;
    }

    private IEnergyStorage createEnergyHandler() {
        return new BdEnergyStorage( Config.collectorEnergyCapacity.get(), 0, Integer.MAX_VALUE );
    }

    private void doFirstTick() {

        firstTick = false;

        if ( world.isRemote )
            Network.channel.sendToServer( new GenericTilePacketRequest( this.pos ) );

    }

    @Nonnull
    public EnergyStorage getEnergyStorage() {
        return BATTERY;
    }

    public static void handle( PacketUpdateCollector packet, Supplier<NetworkEvent.Context> ctx ) {

        ctx.get().enqueueWork( () -> {

            Optional<World> world = LogicalSidedProvider.CLIENTWORLD.get( ctx.get().getDirection().getReceptionSide() );

            if ( world.get().isBlockLoaded( packet.tilePos ) ) {

                TileEntity tile = world.get().getTileEntity( packet.tilePos );

                if ( tile instanceof TileEntityCollector ) {

                    TileEntityCollector ter = (TileEntityCollector) tile;

                    ter.BATTERY.setEnergyStored( packet.storedEnergy );

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

    public boolean canAcceptEnergyPulse() {

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

}
