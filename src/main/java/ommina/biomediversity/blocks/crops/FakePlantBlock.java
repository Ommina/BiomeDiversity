package ommina.biomediversity.blocks.crops;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class FakePlantBlock extends Block {

    public FakePlantBlock( Block.Properties builder ) {
        super( builder );
    }

//region Overrides
    @Override
    public void neighborChanged( BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean isMoving ) {

        if ( neighborPos.getY() == pos.getY() - 1 && world.getBlockState( neighborPos ).isAir() )
            Block.replaceBlock( this.getDefaultState(), Blocks.AIR.getDefaultState(), world, pos, 2 );

        super.neighborChanged( state, world, pos, block, neighborPos, isMoving );

    }

    //@Override
    //public BlockRenderLayer getRenderLayer() {
    //    return BlockRenderLayer.CUTOUT;
    //}

    @Override
    public boolean isValidPosition( BlockState state, IWorldReader world, BlockPos blockPos ) {

        return blockPos.getY() > 1 && (world.getBlockState( blockPos.down() ) == Blocks.DIRT.getDefaultState() || world.getBlockState( blockPos.down() ) == Blocks.GRASS_BLOCK.getDefaultState());

    }
//endregion Overrides

}
