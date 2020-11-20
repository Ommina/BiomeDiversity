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
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.blocks.crops.FakePlantBlock;

import java.util.Random;

public class PomegranateFeature extends Feature<NoFeatureConfig> {

    private static final FakePlantBlock POMEGRANATE_BLOCK = (FakePlantBlock) ModBlocks.WORLD_POMEGRANATE;

    public PomegranateFeature( Codec<NoFeatureConfig> configCodec ) {
        super( configCodec );
    }

    //region Overrides
    @Override
    public boolean generate( ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config ) {

        pos = new BlockPos( pos.getX(), 255, pos.getZ() );

        for ( BlockState blockstate = world.getBlockState( pos ); (blockstate.isAir( world, pos ) || blockstate.isIn( BlockTags.LEAVES )) && pos.getY() > 0; blockstate = world.getBlockState( pos ) ) {
            pos = pos.down();
        }

        BlockState blockstate1 = POMEGRANATE_BLOCK.getDefaultState();

        for ( int i = 0; i < 4; ++i ) {
            BlockPos blockpos = pos;
            if ( (world.isAirBlock( blockpos ) || world.getBlockState( blockpos ) == Blocks.GRASS.getDefaultState()) && blockstate1.isValidPosition( world, blockpos ) ) {
                world.setBlockState( blockpos, blockstate1, 2 );
                //BiomeDiversity.LOGGER.warn( "Pom at " + blockpos.toString() );
            }
        }

        return true;
    }
//endregion Overrides

}