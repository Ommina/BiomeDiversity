package ommina.biomediversity.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.world.gen.GeneratorHelper;

import java.util.Random;

public class JunglePoolFeature extends Feature<NoFeatureConfig> {

    private final int MINIMUM_Y = 40;
    private final int ATTEMPTS_PER_CHUNK = 4;

    public JunglePoolFeature( Codec<NoFeatureConfig> configCodec ) {
        super( configCodec );
    }

    //region Overrides
    @Override
    public boolean generate( ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config ) {

        for ( int i = 0; i < ATTEMPTS_PER_CHUNK; i++ ) {

            BlockPos blockpos = GeneratorHelper.getTopSolidBlock( world, pos.add( rand.nextInt( 8 ) - rand.nextInt( 8 ), rand.nextInt( 4 ) - rand.nextInt( 4 ), rand.nextInt( 8 ) - rand.nextInt( 8 ) ) );

            if ( blockpos.getY() >= MINIMUM_Y && isSuitableLocation( blockpos, world ) ) {
                world.setBlockState( blockpos, ModBlocks.JUNGLEWATER.getDefaultState(), 2 );
            }
        }

        return true;

    }
//endregion Overrides

    private static boolean isSuitableLocation( BlockPos pos, IWorld world ) {

        return world.getBlockState( pos.up() ).getBlock().getDefaultState().getMaterial() == Material.LEAVES &&
             isDirtish( pos.north(), world ) &&
             isDirtish( pos.south(), world ) &&
             isDirtish( pos.west(), world ) &&
             isDirtish( pos.east(), world );

    }

    private static boolean isDirtish( BlockPos pos, IWorld world ) {

        Block block = world.getBlockState( pos ).getBlock();

        return (block == Blocks.GRASS_BLOCK || block == Blocks.DIRT);

    }

}