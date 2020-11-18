package ommina.biomediversity.blocks.collector;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.blocks.cluster.ClusterBlock;
import ommina.biomediversity.blocks.cluster.ICollectorComponent;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.config.Constants;
import ommina.biomediversity.energy.BdEnergyStorage;
import ommina.biomediversity.fluids.BdFluidTank;
import ommina.biomediversity.fluids.ModFluids;
import ommina.biomediversity.network.GenericTilePacketRequest;
import ommina.biomediversity.network.ITankBroadcast;
import ommina.biomediversity.network.Network;
import ommina.biomediversity.sounds.ModSounds;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public class TileEntityCollector extends TileEntity implements ICollectorComponent, ITickableTileEntity, ITankBroadcast {

    static final int TANK_COUNT = 12;
    private static final int MINIMUM_DELTA = 300;

    final List<BdFluidTank> TANK = new ArrayList<>( TANK_COUNT );
    final BdEnergyStorage BATTERY;

    private final BroadcastHelper BROADCASTER;

    private final Reception RECEPTOR = new Reception();
    private final Set<ICollectorComponent> components = new HashSet<>();

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
            TANK.add( i, new BdFluidTank( i, i < 8 ? Constants.COLLECTOR_OUTER_TANK_CAPACITY : Constants.COLLECTOR_INNER_TANK_CAPACITY ) );
        }

        TANK.forEach( o -> o.setCanDrain( true ).setCanFill( false ) );

        BATTERY = new BdEnergyStorage( Config.collectorEnergyCapacity.get(), 0, Integer.MAX_VALUE );

        BROADCASTER = new BroadcastHelper( TANK_COUNT, MINIMUM_DELTA, this, BATTERY, BATTERY.getMaxEnergyStored() / Constants.RF_RENDER_STEP_COUNT );
        BROADCASTER.forceBroadcast();

    }

    //region Overrides
    @Override
    public void doBroadcast() {

        if ( BROADCASTER.needsBroadcast() ) {
            Network.channel.send( PacketDistributor.NEAR.with( () -> new PacketDistributor.TargetPoint( this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 64.0f, World.OVERWORLD ) ), new PacketUpdateCollector( this ) );
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
        //nop -- can't invalidate self
    }

    @Override
    public void registerSelf() {
        //nop -- can't register self
    }

    @Override
    public void onChunkUnloaded() {

        components.forEach( ICollectorComponent::invalidateCollector );
        components.clear();

    }

    @Override
    public void onLoad() {

        if ( world.isRemote )
            Network.channel.sendToServer( new GenericTilePacketRequest( this.pos ) );

    }

    @Override
    public void read( BlockState blockState, CompoundNBT nbt ) {
        super.read( blockState, nbt );

        BATTERY.setEnergyStored( nbt.getInt( "energystored" ) );

        for ( int n = 0; n < TANK_COUNT; n++ )
            TANK.get( n ).read( nbt );

    }

    @Override
    public CompoundNBT write( CompoundNBT nbt ) {

        nbt.putInt( "energystored", BATTERY.getEnergyStored() );

        for ( int n = 0; n < TANK_COUNT; n++ )
            TANK.get( n ).write( nbt );

        return super.write( nbt );

    }

    @Override
    public void tick() {

        if ( firstTick )
            doFirstTick();

        if ( isCollectorTurnedOff() || !isCollectorFormed() )
            return;

        delay--;

        if ( world.isRemote && delay % 5 == 0 )
            world.playSound( pos.getX(), pos.getY(), pos.getZ(), ModSounds.COLLECTOR_RUNNING, SoundCategory.BLOCKS, 0.2f, 1f, false );

        buffer -= BATTERY.receiveEnergyInternal( releasePerTick, false );

        doBroadcast();

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

        if ( buffer <= Constants.CLUSTER_TICK_DELAY + 1 ) { // All the energy was stored -- create the fluids.

            FluidProduct products = miss.getFluidCreated();

            if ( !products.isEmpty() ) {

                if ( products.getWarm() > 0 )
                    TANK.get( Tubes.Warm.tank ).add( new FluidStack( ModFluids.WARMBIOMETIC, products.getWarm() ), IFluidHandler.FluidAction.EXECUTE );

                if ( products.getCool() > 0 )
                    TANK.get( Tubes.Cool.tank ).add( new FluidStack( ModFluids.COOLBIOMETIC, products.getCool() ), IFluidHandler.FluidAction.EXECUTE );

                if ( products.getByproduct() > 0 )
                    TANK.get( Tubes.Byproduct.tank ).add( new FluidStack( ModFluids.BYPRODUCT, products.getByproduct() * 1000 ), IFluidHandler.FluidAction.EXECUTE );

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

    public List<BdFluidTank> getTanks() {
        return TANK;
    }

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

    @Nullable
    public BdFluidTank getFluidTank( int tank ) {

        if ( tank < 0 || tank >= TANK_COUNT )
            return null;

        return TANK.get( tank );

    }

    public void registerComponent( ICollectorComponent component ) {
        components.add( component );
    }

    public void deregisterComponent( ICollectorComponent component ) {
        components.remove( component );
    }

    private void doFirstTick() {

        //TODO: Not required

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
