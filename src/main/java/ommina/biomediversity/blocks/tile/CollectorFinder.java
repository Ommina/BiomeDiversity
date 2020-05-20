package ommina.biomediversity.blocks.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.cluster.ICollectorComponent;
import ommina.biomediversity.blocks.collector.Collector;
import ommina.biomediversity.blocks.collector.TileEntityCollector;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.config.Constants;

import javax.annotation.Nullable;

public class CollectorFinder {

    private final GetCollectorResult collectorResult;

    private int searchAttemptCount = 0;

    private BlockPos collectorPos;
    private RegistrationState registrationState = RegistrationState.NEVER;

    public CollectorFinder() {

        collectorResult = new GetCollectorResult();

    }

    public class GetCollectorResult {

        private boolean isCollectorPosUnloaded;
        private boolean isCollectorMissing;
        private TileEntityCollector tileEntityCollector;

        @Nullable
        public TileEntityCollector getCollector() {
            return tileEntityCollector;
        }

        private GetCollectorResult setTileEntityCollector( TileEntityCollector teCollector ) {

            isCollectorPosUnloaded = false;
            isCollectorMissing = false;
            tileEntityCollector = teCollector;

            return this;

        }

        public boolean isCollectorMissing() {
            return isCollectorMissing;
        }

        private GetCollectorResult setCollectorMissing() {

            isCollectorPosUnloaded = false;
            isCollectorMissing = true;
            tileEntityCollector = null;

            return this;

        }

        public boolean isCollectorPosUnloaded() {
            return isCollectorPosUnloaded;
        }

        private GetCollectorResult setCollectorPosUnloaded() {

            isCollectorPosUnloaded = true;
            isCollectorMissing = false;
            tileEntityCollector = null;

            return this;

        }

    }

    @Nullable
    public BlockPos find( @Nullable World world, BlockPos originPos ) {

        if ( collectorPos != null && world != null && world.isBlockLoaded( collectorPos ) ) {
            return collectorPos;
        }

        if ( searchAttemptCount > Constants.CLUSTER_MAX_SEARCH_COUNT )
            return null;

        searchAttemptCount++;

        int posX = originPos.getX();
        int posY = originPos.getY();
        int posZ = originPos.getZ();

        for ( int y = posY - Config.clusterCollectorSearchVerticalNeg.get(); y <= posY + Config.clusterCollectorSearchVerticalPos.get(); y++ )
            for ( int x = posX - Config.clusterCollectorSearchHorizontal.get(); x <= posX + Config.clusterCollectorSearchHorizontal.get(); x++ )
                for ( int z = posZ - Config.clusterCollectorSearchHorizontal.get(); z <= posZ + Config.clusterCollectorSearchHorizontal.get(); z++ ) {
                    BlockPos bp = new BlockPos( x, y, z );
                    if ( world.isBlockLoaded( bp ) ) {
                        TileEntity te = world.getTileEntity( bp );
                        if ( te instanceof ICollectorComponent ) {
                            ICollectorComponent tecc = (ICollectorComponent) te;
                            if ( tecc.isClusterComponentConnected() ) {
                                collectorPos = tecc.getCollectorPos();
                                if ( registrationState == RegistrationState.NEVER )
                                    registrationState = RegistrationState.UNREGISTERED;
                                return collectorPos;
                            }
                        }
                    }
                }

        return null;

    }

    public boolean shouldRegister() {
        return registrationState == RegistrationState.UNREGISTERED;
    }

    public void markAsRegistered() {

        if ( registrationState == RegistrationState.UNREGISTERED ) {
            registrationState = RegistrationState.REGISTERED;
            return;
        }

        BiomeDiversity.LOGGER.error( "Tried to set a ClusterComponent to REGISTERED when it is in state: " + registrationState.toString() );

    }

    public GetCollectorResult getCollector( @Nullable World world ) {

        if ( world == null || collectorPos == null || !world.isBlockLoaded( collectorPos ) )
            return collectorResult.setCollectorPosUnloaded();

        TileEntity tileEntity = world.getTileEntity( this.collectorPos );

        if ( tileEntity instanceof TileEntityCollector && world.getBlockState( collectorPos ).get( Collector.FORMED ) )
            return collectorResult.setTileEntityCollector( (TileEntityCollector) tileEntity );

        return collectorResult.setCollectorMissing();

    }

    /**
    *   Returns the Collector at the stored BlockPos.  Ensures pos is not null, chunk is loaded, and the Tile is, in fact, a Collector
    *
    *   @return Collector TileEntity.  Null is checks fail
    */
    @Nullable
    public TileEntityCollector get( @Nullable World world ) {

        if ( world != null && collectorPos != null && world.isBlockLoaded( collectorPos ) && world.getTileEntity( collectorPos ) instanceof TileEntityCollector )
            return (TileEntityCollector) world.getTileEntity( collectorPos );

        return null;

    }

    @Nullable
    public BlockPos getCollectorPos() {
        return collectorPos;
    }

    public void setCollectorPos( @Nullable BlockPos collectorPos ) {
        this.collectorPos = collectorPos;
    }

    private enum RegistrationState {

        NEVER,
        REGISTERED,
        UNREGISTERED

    }

}
