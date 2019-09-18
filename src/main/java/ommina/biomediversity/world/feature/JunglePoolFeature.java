package ommina.biomediversity.world.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.world.GeneratorHelper;

import java.util.Random;
import java.util.function.Function;

public class JunglePoolFeature extends Feature<NoFeatureConfig> {

    private final int MINIMUM_Y = 40;
    private final int ATTEMPTS_PER_CHUNK = 4;

    public JunglePoolFeature( String name, Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn ) {
        super( configFactoryIn );

        setRegistryName( BiomeDiversity.getId( name ) );
    }

    @Override
    public boolean place( IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config ) {

        for ( int i = 0; i < ATTEMPTS_PER_CHUNK; i++ ) {

            BlockPos blockpos = GeneratorHelper.getTopSolidBlock( world, pos.add( rand.nextInt( 8 ) - rand.nextInt( 8 ), rand.nextInt( 4 ) - rand.nextInt( 4 ), rand.nextInt( 8 ) - rand.nextInt( 8 ) ) );

            if ( blockpos.getY() >= MINIMUM_Y && isSuitableLocation( blockpos, world ) ) {
                world.setBlockState( blockpos, ModBlocks.JUNGLEWATER.getDefaultState(), 2 );
            }
        }

        return true;

    }

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