package ommina.biomediversity.blocks.plug;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
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
import ommina.biomediversity.network.GenericTilePacketRequest;
import ommina.biomediversity.network.Network;
import ommina.biomediversity.util.NbtUtils;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public class TileEntityPlug extends TileEntity implements IClusterComponent, ITickableTileEntity {

    final CollectorFinder COLLECTOR = new CollectorFinder();
    final PlugRenderData PLUG_RENDER = new PlugRenderData();

    private TileEntityCollector getPlugRenderDataCollector; // GetPlugRenderData is called each frame, so we'll reuse this reference to avoid a bit of GC stress

    private int delay = Constants.CLUSTER_TICK_DELAY;
    private int loop = 1;

    private int plug_connection_type;

    private boolean firstTick = true;

    public TileEntityPlug() {
        super( ModTileEntities.PLUG );

    }

    //region Overrides
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

            getPlugRenderDataCollector = getCollectorResult().getCollector();

            if ( getPlugRenderDataCollector != null )
                PLUG_RENDER.value = getPlugRenderDataCollector.getEnergyStorage().getEnergyStored();
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

                    plug.COLLECTOR.setCollectorPos( packet.collectorPos );

                }

            }

        } );

        ctx.get().setPacketHandled( true );

    }

    private void removeCollector() {

        COLLECTOR.setCollectorPos( null );
        doBroadcast();
        markDirty();

    }

    private void doMainWork() {

        CollectorFinder.GetCollectorResult collectorResult = getCollectorResult();

        if ( collectorResult.getCollector() == null ) {

            if ( collectorResult.isCollectorMissing() )
                removeCollector();

            if ( COLLECTOR.getCollectorPos() == null && (loop % Constants.CLUSTER_SEARCH_ON_LOOP) == 0 ) {
                BlockPos pos = COLLECTOR.find( world, getPos() );
                if ( pos != null ) {
                    doBroadcast();
                    markDirty();
                }
            }

            return;

        }

        TileEntityCollector collector = collectorResult.getCollector();

    }

    public CollectorFinder.GetCollectorResult getCollectorResult() {
        return COLLECTOR.getCollector( world );
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
