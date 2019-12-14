package ommina.biomediversity.blocks.collector;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.blocks.cluster.ClusterBlock;
import ommina.biomediversity.blocks.cluster.IClusterComponent;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.config.Constants;
import ommina.biomediversity.energy.BdEnergyStorage;
import ommina.biomediversity.fluids.BdFluidTank;
import ommina.biomediversity.fluids.ModFluids;
import ommina.biomediversity.network.GenericTilePacketRequest;
import ommina.biomediversity.network.ITankBroadcast;
import ommina.biomediversity.network.Network;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public class TileEntityCollector extends TileEntity implements IClusterComponent, ITickableTileEntity, ITankBroadcast {

    public static final int PLUG_CONNECTION_RF = 1;

    /*

    public static final int UNUSED0 = 0;      // No, I'm not going to make this an Enum.  Deal with it.  Probably.
    public static final int UNUSED1 = 1;
    public static final int UNUSED2 = 2;
    public static final int COOL = 3;
    public static final int WARM = 4;
    public static final int UNUSED5 = 5;
    public static final int UNUSED6 = 6;
    public static final int BYPRODUCT = 7;
    public static final int UNUSED8 = 8;
    public static final int UNUSED9 = 9;
    public static final int UNUSED10 = 10;
    public static final int UNUSED11 = 11;

    */

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
    private float temperature;
    private boolean firstTick = true;

    public TileEntityCollector() {
        super( ModTileEntities.COLLECTOR );

        for ( int i = 0; i < TANK_COUNT; i++ ) {
            TANK.add( i, new BdFluidTank( i < 8 ? Constants.COLLECTOR_OUTER_TANK_CAPACITY : Constants.COLLECTOR_INNER_TANK_CAPACITY ) );
        }

        TANK.forEach( o -> o.setCanDrain( true ).setCanFill( false ) );

        BATTERY = (BdEnergyStorage) energyHandler.orElse( null );

        BROADCASTER = new BroadcastHelper( TANK_COUNT, MINIMUM_DELTA, this, BATTERY, BATTERY.getMaxEnergyStored() / Constants.RF_RENDER_STEP_COUNT );
        BROADCASTER.forceBroadcast();

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

        return world.getBlockState( this.getPos() ).getBlock() == ModBlocks.COLLECTOR && world.getBlockState( this.getPos() ).get( Collector.FORMED );
    }

    @Override
    public void invalidateCollector() {
        //nop
    }

    @Override
    public void onChunkUnloaded() {

        components.forEach( IClusterComponent::invalidateCollector );
        components.clear();

    }

    @Override
    public boolean hasFastRenderer() {
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

        buffer -= BATTERY.receiveEnergyInternal( releasePerTick, false );

        doBroadcast();

        if ( delay > 0 || !isCollectorFormed() )
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

        if ( buffer <= Constants.CLUSTER_TICK_DELAY + 1 ) { // All the energy was stored -- create the fluids.

            FluidProduct products = miss.getFluidCreated();

            if ( !products.isEmpty() ) {

                if ( products.getWarm() > 0 )
                    TANK.get( Tubes.Warm.tank ).add( new FluidStack( ModFluids.WARMBIOMETIC, products.getWarm() ), IFluidHandler.FluidAction.EXECUTE );

                if ( products.getCool() > 0 )
                    TANK.get( Tubes.Cool.tank ).add( new FluidStack( ModFluids.COOLBIOMETIC, products.getCool() ), IFluidHandler.FluidAction.EXECUTE );

                if ( products.getByproduct() > 0 )
                    TANK.get( Tubes.Byproduct.tank ).add( new FluidStack( ModFluids.BYPRODUCT, products.getByproduct() ), IFluidHandler.FluidAction.EXECUTE );

            }

        } else {
            //System.out.println( "buffer: " + buffer + ", lastRfCreatred: " + lastRfCreated );
        }

        buffer += lastRfCreated;
        releasePerTick = buffer / Constants.CLUSTER_TICK_DELAY;

        /*
        if ( BiomeDiversity.DEBUG ) {
            BiomeDiversity.LOGGER.warn( "lastRf: " + lastRfCreated + ", biomes: " + uniqueBiomeCount + ", temp: " + temperature + ", buffer: " + buffer + ", release: " + releasePerTick + ", stored: " + BATTERY.getEnergyStored() );
        }
        */

        markDirty();

    }
//endregion Overrides

    public void forceBroadcast() {
        BROADCASTER.forceBroadcast();
    }

    public int getUniqueBiomeCount() {
        return uniqueBiomeCount;
    }

    public float getTemperature() {
        return temperature;
    }

    public int getRfReleasedPerTick() {
        return releasePerTick;
    }

    public void registerComponent( IClusterComponent component ) {
        components.add( component );
    }

    public void deregisterComponent( IClusterComponent component ) {
        components.remove( component );
    }

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
    public BdEnergyStorage getEnergyStorage() {
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
                    ter.releasePerTick = packet.releasePerTick;
                    ter.temperature = packet.temperature;
                    ter.uniqueBiomeCount = packet.uniqueBiomeCount;

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

        return world.isBlockPowered( pos );

    }

    public boolean isCollectorFormed() {

        return world.getBlockState( pos ).get( ClusterBlock.FORMED );

    }

}
