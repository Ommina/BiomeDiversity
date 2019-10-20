package ommina.biomediversity.blocks.collector;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import ommina.biomediversity.blocks.cluster.IClusterController;

public class Collector extends Block implements IClusterController { // BlockTileEntity<TileEntityCollector> {

    public Collector() {

        super( Block.Properties.create( Material.ROCK ).harvestLevel( 2 ).harvestTool( ToolType.PICKAXE ).hardnessAndResistance( 10f ) );

    }

//region Overrides
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean hasTileEntity( BlockState state ) {
        return true;
    }

    //@Override
    //protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
    //    builder.add( IS_CONNECTED );
    //}

    //@Override
    //public BlockState getStateForPlacement( BlockItemUseContext context ) {
    //    return this.getDefaultState().with( IS_CONNECTED, true );
    //}

    @Override
    public TileEntity createTileEntity( BlockState state, IBlockReader world ) {
        return new TileEntityCollector();
    }
//endregion Overrides

}
