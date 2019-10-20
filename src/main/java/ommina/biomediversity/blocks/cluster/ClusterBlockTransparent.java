package ommina.biomediversity.blocks.cluster;

import net.minecraft.util.BlockRenderLayer;

public class ClusterBlockTransparent extends ClusterBlock {

    public ClusterBlockTransparent( Properties properties ) {
        super( properties );
    }

//region Overrides

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

//endregion Overrides

}
