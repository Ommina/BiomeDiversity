package ommina.biomediversity.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.blocks.crops.FakePlantBlock;

import java.util.Random;

public class ColzaFeature extends Feature<NoFeatureConfig> {

    private static final FakePlantBlock COLZA_BLOCK = (FakePlantBlock) ModBlocks.WORLD_COLZA;

    public ColzaFeature( Codec<NoFeatureConfig> configCodec ) {
        super( configCodec );
    }

    //region Overrides
    @Override
    public boolean generate( ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config ) {

        for ( BlockState blockstate = world.getBlockState( pos ); (blockstate.isAir( world, pos ) || blockstate.isIn( BlockTags.LEAVES )) && pos.getY() > 0; blockstate = world.getBlockState( pos ) ) {
            pos = pos.down();
        }

        BlockState blockstate1 = COLZA_BLOCK.getDefaultState();

        for ( int i = 0; i < 4; ++i ) {
            BlockPos blockpos = pos.add( rand.nextInt( 8 ) - rand.nextInt( 8 ), rand.nextInt( 4 ) - rand.nextInt( 4 ), rand.nextInt( 8 ) - rand.nextInt( 8 ) );
            if ( (world.isAirBlock( blockpos ) || world.getBlockState( blockpos ) == Blocks.GRASS.getDefaultState()) && blockstate1.isValidPosition( world, blockpos ) ) {
                world.setBlockState( blockpos, blockstate1, 2 );
            }
        }

        return true;
    }
//endregion Overrides

}
