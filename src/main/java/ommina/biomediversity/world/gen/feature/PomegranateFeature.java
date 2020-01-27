package ommina.biomediversity.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.blocks.crops.FakePlantBlock;

import java.util.Random;
import java.util.function.Function;

public class PomegranateFeature extends Feature<NoFeatureConfig> {

    private static final FakePlantBlock POMEGRANATE_BLOCK = (FakePlantBlock) ModBlocks.WORLD_POMEGRANATE;

    public PomegranateFeature( Function<Dynamic<?>, ? extends NoFeatureConfig> config ) {
        super( config );
    }

    //region Overrides
    @Override
    public boolean place( IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config ) {

        for ( BlockState blockstate = worldIn.getBlockState( pos ); (blockstate.isAir( worldIn, pos ) || blockstate.isIn( BlockTags.LEAVES )) && pos.getY() > 0; blockstate = worldIn.getBlockState( pos ) ) {
            pos = pos.down();
        }

        BlockState blockstate1 = POMEGRANATE_BLOCK.getDefaultState();

        for ( int i = 0; i < 4; ++i ) {
            BlockPos blockpos = pos.add( rand.nextInt( 8 ) - rand.nextInt( 8 ), rand.nextInt( 4 ) - rand.nextInt( 4 ), rand.nextInt( 8 ) - rand.nextInt( 8 ) );
            if ( (worldIn.isAirBlock( blockpos ) || worldIn.getBlockState( blockpos ) == Blocks.GRASS.getDefaultState()) && blockstate1.isValidPosition( worldIn, blockpos ) ) {
                worldIn.setBlockState( blockpos, blockstate1, 2 );
            }
        }

        return true;
    }
//endregion Overrides

}