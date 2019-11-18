package ommina.biomediversity.blocks.plug;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.blocks.cluster.IClusterComponent;
import ommina.biomediversity.blocks.collector.TileEntityCollector;
import ommina.biomediversity.blocks.tile.CollectorFinder;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.config.Constants;
import ommina.biomediversity.energy.BdEnergyStorage;
import ommina.biomediversity.network.GenericTilePacketRequest;
import ommina.biomediversity.network.Network;
import ommina.biomediversity.util.NbtUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Supplier;

public class TileEntityPlug extends TileEntity implements IClusterComponent, ITickableTileEntity {

    final CollectorFinder FINDER = new CollectorFinder();
    final PlugRenderData PLUG_RENDER = new PlugRenderData();

    private TileEntityCollector getPlugRenderDataCollector; // GetPlugRenderData is called each frame, so we'll reuse this reference to avoid a bit of GC stress

    private int delay = Constants.CLUSTER_TICK_DELAY;
    private int loop = 1;

    private int plug_connection_type;

    private boolean firstTick = true;

    public TileEntityPlug() {
        super( ModTileEntities.PLUG );

    }

    private void distributeRf() {

        TileEntity tileEntity = world.getTileEntity( getPos().down() );

        if ( tileEntity != null && tileEntity.getCapability( CapabilityEnergy.ENERGY, Direction.UP ).isPresent() ) {

            tileEntity.getCapability( CapabilityEnergy.ENERGY, Direction.UP ).ifPresent( e -> {

                TileEntityCollector collector = FINDER.get( world );

                if ( collector != null ) {

                    BdEnergyStorage battery = collector.getEnergyStorage();
                    int maxReceive = e.receiveEnergy( Integer.MAX_VALUE, true );
                    int withdraw = battery.extractEnergy( maxReceive, false );
                    e.receiveEnergy( withdraw, false );

                }

            } );

        }

    }

    //region Overrides
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability( @Nonnull Capability<T> capability, @Nullable Direction side ) {

        if ( capability == CapabilityEnergy.ENERGY && (side == null || side == Direction.DOWN) )
            return FINDER.getCollector( world ).getCollector().getEnergyHandler().cast();

        return super.getCapability( capability, side );

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
    public void onChunkUnloaded() {

        TileEntityCollector collector = FINDER.get( world );

        if ( collector != null )
            collector.deregisterComponent( this );

    }

    @Override
    public boolean hasFastRenderer() {

        return true;
    }

    @Override
    public void read( CompoundNBT nbt ) {

        FINDER.setCollectorPos( NbtUtils.getBlockPos( "collectorpos", nbt ) );

        super.read( nbt );

    }

    @Override
    public CompoundNBT write( CompoundNBT nbt ) {

        if ( FINDER.getCollectorPos() != null )
            NbtUtils.putBlockPos( "collectorpos", nbt, FINDER.getCollectorPos() );

        return super.write( nbt );

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

    public PlugRenderData getPlugRenderData() {

        if ( plug_connection_type == TileEntityCollector.PLUG_CONNECTION_RF ) {

            TileEntityCollector collector = FINDER.get( world );

            getPlugRenderDataCollector = collector;

            if ( collector != null )
                PLUG_RENDER.value = collector.getEnergyStorage().getEnergyStored();
            else
                PLUG_RENDER.value = 0;

        }

        return PLUG_RENDER;

    }

    public void doBroadcast() {

        Network.channel.send( PacketDistributor.NEAR.with( () -> new PacketDistributor.TargetPoint( this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 64.0f, DimensionType.OVERWORLD ) ), new PacketUpdatePlug( this ) );

    }

    public static void handle( PacketUpdatePlug packet, Supplier<NetworkEvent.Context> ctx ) {

        ctx.get().enqueueWork( () -> {

            Optional<World> world = LogicalSidedProvider.CLIENTWORLD.get( ctx.get().getDirection().getReceptionSide() );

            if ( world.get().isBlockLoaded( packet.tilePos ) ) {

                TileEntity tile = world.get().getTileEntity( packet.tilePos );

                if ( tile instanceof TileEntityPlug ) {

                    TileEntityPlug plug = (TileEntityPlug) tile;

                    plug.FINDER.setCollectorPos( packet.collectorPos );

                }

            }

        } );

        ctx.get().setPacketHandled( true );

    }

    private void removeCollector() {

        FINDER.setCollectorPos( null );
        getPlugRenderDataCollector = null;
        doBroadcast();
        markDirty();

    }

    private void doMainWork() {

        CollectorFinder.GetCollectorResult collectorResult = FINDER.getCollector( world );

        if ( collectorResult.getCollector() == null ) {

            if ( collectorResult.isCollectorMissing() )
                removeCollector();

            if ( FINDER.getCollectorPos() == null && (loop % Constants.CLUSTER_SEARCH_ON_LOOP) == 0 ) {
                BlockPos pos = FINDER.find( world, getPos() );
                if ( pos != null ) {
                    doBroadcast();
                    markDirty();
                }
            }

            return;

        }

        TileEntityCollector collector = collectorResult.getCollector();

    }



    private void doFirstTick() {

        firstTick = false;

        Block block = world.getBlockState( this.pos ).getBlock();

        if ( block == ModBlocks.PLUG ) {
            plug_connection_type = TileEntityCollector.PLUG_CONNECTION_RF;
            PLUG_RENDER.sprite = BiomeDiversity.getId( "block/cluster/cluster_glow_internal" );
            PLUG_RENDER.maximum = Config.collectorEnergyCapacity.get();
        }

        if ( world.isRemote )
            Network.channel.sendToServer( new GenericTilePacketRequest( this.pos ) );

    }

}
