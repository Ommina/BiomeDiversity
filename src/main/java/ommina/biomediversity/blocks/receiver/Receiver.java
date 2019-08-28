package ommina.biomediversity.blocks.receiver;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.common.ToolType;
import ommina.biomediversity.blocks.BlockTileEntity;

public class Receiver extends BlockTileEntity<TileEntityReceiver> {

    public Receiver() {

        super( Block.Properties.create( Material.ROCK ).harvestLevel( 2 ).harvestTool( ToolType.PICKAXE ).hardnessAndResistance( 10f ) );

    }

    /*

    @Override
    public boolean hasTileEntity( BlockState state ) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity( BlockState state, IBlockReader world ) {
        return new TileEntityPillar();
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( IS_CONNECTED );
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        return this.getDefaultState().with( IS_CONNECTED, true );
    }

    */

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

}
