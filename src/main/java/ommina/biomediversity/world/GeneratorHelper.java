
package ommina.biomediversity.world;

import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;

public class GeneratorHelper {

    public static BlockPos getTopSolidBlock( IWorld world, BlockPos pos ) { // Copied, roughly, from world.getTopSolidOrLiquidBlock

        IChunk chunk = world.getChunk( pos );

        BlockPos blockpos = pos;
        BlockPos blockpos1 = pos;

        for ( blockpos = new BlockPos( pos.getX(), chunk.getTopFilledSegment() + 16, pos.getZ() ); blockpos.getY() > 0; blockpos = blockpos1 ) {

            blockpos1 = blockpos.down();
            BlockState state = chunk.getBlockState( blockpos1 );

            if ( !state.getMaterial().isLiquid() && !(state.getBlock() instanceof FallingBlock) && state.getMaterial().blocksMovement() && !state.isIn( BlockTags.LEAVES ) )
                break;
        }

        return blockpos1;
    }

}
