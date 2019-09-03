
package ommina.biomediversity.blocks.rainbarrel;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import ommina.biomediversity.blocks.BlockTileEntity;

public class RainBarrel extends BlockTileEntity<TileEntityRainBarrel> {

    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB( 0f, 0f, 0f, 1.0f, 15f / 16f, 1.0f );

    public RainBarrel() {

        super( Block.Properties.create( Material.ROCK ).harvestLevel( 2 ).harvestTool( ToolType.PICKAXE ).hardnessAndResistance( 10f ) );

    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean hasTileEntity( BlockState state ) {
        return true;
    }

    @Override
    public TileEntity createTileEntity( BlockState state, IBlockReader world ) {
        return new TileEntityRainBarrel();
    }


/*



    @Override
    public boolean isOpaqueCube( IBlockState state ) {

        return false;
    }

    @Override
    public boolean isFullCube( IBlockState state ) {

        return false;
    }

    @SideOnly( Side.CLIENT )
    @Override
    public BlockRenderLayer getBlockLayer() {

        return BlockRenderLayer.CUTOUT;
    }


    @Override
    public AxisAlignedBB getBoundingBox( IBlockState state, IBlockAccess source, BlockPos pos ) {

        return BOUNDING_BOX;
    }

    public AxisAlignedBB getCollisionBoundingBox( IBlockState blockState, World worldIn, BlockPos pos ) {

        return BOUNDING_BOX;
    }

*/

}
