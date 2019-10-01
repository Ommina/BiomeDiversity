package ommina.biomediversity.blocks.crops;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import ommina.biomediversity.BiomeDiversity;

public class GrassOnlyBlock extends Block {

    public GrassOnlyBlock( Block.Properties builder ) {
        super( builder );
    }

    @Override
    public void neighborChanged( BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving ) {

        BiomeDiversity.LOGGER.info( "nc: pos: " + pos.toString() + " block: " + block.toString() + " fromPos: " + fromPos + " isMoving" + isMoving );

        super.neighborChanged( state, world, pos, block, fromPos, isMoving );
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isValidPosition( BlockState state, IWorldReader world, BlockPos blockPos ) {

        return blockPos.getY() > 1 && (world.getBlockState( blockPos.down() ) == Blocks.DIRT.getDefaultState() || world.getBlockState( blockPos.down() ) == Blocks.GRASS_BLOCK.getDefaultState());

    }

}
