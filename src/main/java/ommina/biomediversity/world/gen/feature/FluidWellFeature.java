package ommina.biomediversity.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.config.Config;

import java.util.Random;

public class FluidWellFeature extends Feature<OreFeatureConfig> {

    public FluidWellFeature( Codec<OreFeatureConfig> configCodec ) {
        super( configCodec );
    }

    //region Overrides
    @Override
    public boolean generate( ISeedReader world, ChunkGenerator p_241855_2_, Random random, BlockPos pos, OreFeatureConfig p_241855_5_ ) {

        if ( random.nextInt( 2000 ) > Config.fluidWellProbability.get() )
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
