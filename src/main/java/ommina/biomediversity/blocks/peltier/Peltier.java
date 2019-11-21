package ommina.biomediversity.blocks.peltier;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.common.ToolType;
import ommina.biomediversity.blocks.BlockTileEntity;
import ommina.biomediversity.config.Constants;

public class Peltier extends Block { //BlockTileEntity<TileEntityPeltier> {

    public Peltier() {

        super( Block.Properties.create( Material.ROCK ).harvestLevel( 2 ).harvestTool( ToolType.PICKAXE ).hardnessAndResistance( Constants.DEFAULT_TILE_ENTITY_HARDNESS ) );

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
