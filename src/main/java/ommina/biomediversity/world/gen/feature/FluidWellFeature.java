package ommina.biomediversity.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.config.Config;

import java.util.Random;
import java.util.function.Function;

public class FluidWellFeature extends Feature<OreFeatureConfig> {

    public FluidWellFeature( Function<Dynamic<?>, ? extends OreFeatureConfig> config ) {
        super( config );
    }

    //region Overrides
    @Override
    public boolean place( IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, OreFeatureConfig config ) {

        if ( rand.nextInt( 2000 ) > Config.fluidWellProbability.get() )
            return true;

        pos = new BlockPos( pos.getX(), 1, pos.getZ() );

        BlockState blockState = world.getBlockState( pos );

        if ( blockState.getBlock() != Blocks.STONE && blockState.getBlock() != Blocks.BEDROCK )
            return true;

        BiomeDiversity.LOGGER.warn( "Creating FluidWell TE at " + pos.toString() );

        world.setBlockState( pos, ModBlocks.FLUID_WELL.getDefaultState(), 2 );

        return true;

    }
//endregion Overrides

}
