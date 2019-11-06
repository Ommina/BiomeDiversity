package ommina.biomediversity.blocks.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ommina.biomediversity.blocks.cluster.IClusterComponent;
import ommina.biomediversity.blocks.collector.TileEntityCollector;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.config.Constants;

import javax.annotation.Nullable;

public class CollectorFinder {

    private final GetCollectorResult collectorResult;

    private int searchAttemptCount = 0;
    private BlockPos collectorPos;

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
                        if ( te instanceof IClusterComponent ) {
                            IClusterComponent tecc = (IClusterComponent) te;
                            if ( tecc.isClusterComponentConnected() ) {
                                collectorPos = tecc.getCollectorPos();
                                return collectorPos;
                            }
                        }
                    }
                }

        return null;

    }

    public GetCollectorResult getCollector( @Nullable World world ) {

        if ( world == null || collectorPos == null || !world.isBlockLoaded( collectorPos ) )
            return collectorResult.setCollectorPosUnloaded();

        TileEntity tileEntity = world.getTileEntity( this.collectorPos );

        if ( tileEntity instanceof TileEntityCollector )
            return collectorResult.setTileEntityCollector( (TileEntityCollector) tileEntity );

        return collectorResult.setCollectorMissing();

    }

    @Nullable
    public BlockPos getCollectorPos() {
        return collectorPos;
    }

    public void setCollectorPos( @Nullable BlockPos collectorPos ) {
        this.collectorPos = collectorPos;
    }

}
