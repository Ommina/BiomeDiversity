package ommina.biomediversity.world.gen.carver;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.carver.CaveWorldCarver;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import ommina.biomediversity.BiomeDiversity;

import java.util.BitSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class FluidWellWorldCarver extends CaveWorldCarver {

    private static final Set<Block> carvableWellBlocks = ImmutableSet.of( Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.PODZOL, Blocks.GRASS_BLOCK, Blocks.TERRACOTTA, Blocks.WHITE_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA, Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA, Blocks.LIME_TERRACOTTA, Blocks.PINK_TERRACOTTA, Blocks.GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA, Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.BLUE_TERRACOTTA, Blocks.BROWN_TERRACOTTA, Blocks.GREEN_TERRACOTTA, Blocks.RED_TERRACOTTA, Blocks.BLACK_TERRACOTTA, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.MYCELIUM, Blocks.SNOW, Blocks.SAND, Blocks.GRAVEL, Blocks.WATER, Blocks.LAVA, Blocks.OBSIDIAN, Blocks.AIR, Blocks.CAVE_AIR, Blocks.PACKED_ICE );

    public FluidWellWorldCarver( Function<Dynamic<?>, ? extends ProbabilityConfig> config ) {

        super( config, 256 );
        carvableBlocks = ImmutableSet.of( Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.PODZOL, Blocks.GRASS_BLOCK, Blocks.TERRACOTTA, Blocks.WHITE_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA, Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA, Blocks.LIME_TERRACOTTA, Blocks.PINK_TERRACOTTA, Blocks.GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA, Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.BLUE_TERRACOTTA, Blocks.BROWN_TERRACOTTA, Blocks.GREEN_TERRACOTTA, Blocks.RED_TERRACOTTA, Blocks.BLACK_TERRACOTTA, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.MYCELIUM, Blocks.SNOW, Blocks.SAND, Blocks.GRAVEL, Blocks.WATER, Blocks.LAVA, Blocks.OBSIDIAN, Blocks.AIR, Blocks.CAVE_AIR, Blocks.PACKED_ICE );

    }

    //region Overrides
    @Override
    protected boolean func_225556_a_( IChunk chunk, Function<BlockPos, Biome> blockPosBiomeFunction, BitSet bitSet, Random random, BlockPos.Mutable pos1, BlockPos.Mutable pos2, BlockPos.Mutable pos3, int int1, int int2, int int3, int int4, int int5, int int6, int int7, int int8, AtomicBoolean atomicBoolean ) {
        return true;//' func_222728_a( this, chunk, bitSet, random, pos1, int1, int2, int3, int4, int5, int6, int7, int8 );
    }

    @Override
    protected boolean func_222700_a( IChunk chunkIn, int chunkX, int chunkZ, int minX, int maxX, int minY, int maxY, int minZ, int maxZ ) {
        return false;
    }

    @Override
    public boolean shouldCarve( Random rand, int chunkX, int chunkZ, ProbabilityConfig config ) {
        return rand.nextFloat() <= config.probability;
    }

    @Override
    public boolean func_225555_a_( IChunk chunk, Function<BlockPos, Biome> p_225555_2_, Random p_225555_3_, int p_225555_4_, int p_225555_5_, int p_225555_6_, int p_225555_7_, int p_225555_8_, BitSet p_225555_9_, ProbabilityConfig p_225555_10_ ) {

        BiomeDiversity.LOGGER.warn( "22555a chunkpos" + chunk.getPos().toString() );

        // Calls GenerateCaveRadius
        // Calls 227206

        /*

        int i = (this.func_222704_c() * 2 - 1) * 16;
        int j = p_225555_3_.nextInt( p_225555_3_.nextInt( p_225555_3_.nextInt( this.func_222724_a() ) + 1 ) + 1 );

        for ( int k = 0; k < j; ++k ) {
            double d0 = (double) (p_225555_5_ * 16 + p_225555_3_.nextInt( 16 ));
            double d1 = (double) this.generateCaveStartY( p_225555_3_ );
            double d2 = (double) (p_225555_6_ * 16 + p_225555_3_.nextInt( 16 ));
            int l = 1;
            if ( p_225555_3_.nextInt( 4 ) == 0 ) {
                double d3 = 0.5D;
                float f1 = 1.0F + p_225555_3_.nextFloat() * 6.0F;
                this.func_227205_a_( chunk, p_225555_2_, p_225555_3_.nextLong(), p_225555_4_, p_225555_7_, p_225555_8_, d0, d1, d2, f1, 0.5D, p_225555_9_ );
                l += p_225555_3_.nextInt( 4 );
            }

            for ( int k1 = 0; k1 < l; ++k1 ) {
                float f = p_225555_3_.nextFloat() * ((float) Math.PI * 2F);
                float f3 = (p_225555_3_.nextFloat() - 0.5F) / 4.0F;
                float f2 = this.generateCaveRadius( p_225555_3_ );
                int i1 = i - p_225555_3_.nextInt( i / 4 );
                int j1 = 0;
                this.func_227206_a_( chunk, p_225555_2_, p_225555_3_.nextLong(), p_225555_4_, p_225555_7_, p_225555_8_, d0, d1, d2, f2, f, f3, 0, i1, this.func_222725_b(), p_225555_9_ );
            }
        }

        */

        return true;
    }

    @Override
    protected float generateCaveRadius( Random rand ) {

        BiomeDiversity.LOGGER.warn( "generateCaveRadius" );

        float f = rand.nextFloat() * 2.0F + rand.nextFloat();
        if ( rand.nextInt( 10 ) == 0 ) {
            f *= rand.nextFloat() * rand.nextFloat() * 3.0F + 1.0F;
        }

        return f;
    }

    @Override
    protected void func_227206_a_( IChunk p_227206_1_, Function<BlockPos, Biome> p_227206_2_, long p_227206_3_, int p_227206_5_, int p_227206_6_, int p_227206_7_, double p_227206_8_, double p_227206_10_, double p_227206_12_, float p_227206_14_, float p_227206_15_, float p_227206_16_, int p_227206_17_, int p_227206_18_, double p_227206_19_, BitSet p_227206_21_ ) {

        BiomeDiversity.LOGGER.warn( "227206a" );

        Random random = new Random( p_227206_3_ );
        int i = random.nextInt( p_227206_18_ / 2 ) + p_227206_18_ / 4;
        boolean flag = random.nextInt( 6 ) == 0;
        float f = 0.0F;
        float f1 = 0.0F;

        for ( int j = p_227206_17_; j < p_227206_18_; ++j ) {
            double d0 = 1.5D + (double) (MathHelper.sin( (float) Math.PI * (float) j / (float) p_227206_18_ ) * p_227206_14_);
            double d1 = d0 * p_227206_19_;
            float f2 = MathHelper.cos( p_227206_16_ );
            p_227206_8_ += (double) (MathHelper.cos( p_227206_15_ ) * f2);
            p_227206_10_ += (double) MathHelper.sin( p_227206_16_ );
            p_227206_12_ += (double) (MathHelper.sin( p_227206_15_ ) * f2);
            p_227206_16_ = p_227206_16_ * (flag ? 0.92F : 0.7F);
            p_227206_16_ = p_227206_16_ + f1 * 0.1F;
            p_227206_15_ += f * 0.1F;
            f1 = f1 * 0.9F;
            f = f * 0.75F;
            f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            f = f + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
            if ( j == i && p_227206_14_ > 1.0F ) {
                this.func_227206_a_( p_227206_1_, p_227206_2_, random.nextLong(), p_227206_5_, p_227206_6_, p_227206_7_, p_227206_8_, p_227206_10_, p_227206_12_, random.nextFloat() * 0.5F + 0.5F, p_227206_15_ - ((float) Math.PI / 2F), p_227206_16_ / 3.0F, j, p_227206_18_, 1.0D, p_227206_21_ );
                this.func_227206_a_( p_227206_1_, p_227206_2_, random.nextLong(), p_227206_5_, p_227206_6_, p_227206_7_, p_227206_8_, p_227206_10_, p_227206_12_, random.nextFloat() * 0.5F + 0.5F, p_227206_15_ + ((float) Math.PI / 2F), p_227206_16_ / 3.0F, j, p_227206_18_, 1.0D, p_227206_21_ );
                return;
            }

            if ( random.nextInt( 4 ) != 0 ) {
                if ( !this.func_222702_a( p_227206_6_, p_227206_7_, p_227206_8_, p_227206_12_, j, p_227206_18_, p_227206_14_ ) ) {
                    return;
                }

                this.func_227208_a_( p_227206_1_, p_227206_2_, p_227206_3_, p_227206_5_, p_227206_6_, p_227206_7_, p_227206_8_, p_227206_10_, p_227206_12_, d0, d1, p_227206_21_ );
            }
        }

    }
//endregion Overrides

    protected static boolean canCarve( BlockState blockState ) {
        return carvableWellBlocks.contains( blockState.getBlock() );
    }

    protected static boolean func_222728_a( WorldCarver<?> worldCarver, IChunk chunk, BitSet bitSet, Random random, BlockPos.Mutable pos, int int1, int int2, int int3, int int4, int int5, int int6, int int7, int int8 ) {

        // BiomeDiversity.LOGGER.warn( "I want to do a thing at chunk " + chunk.getPos().toString() + " pos " + pos.toString() );

        return false;

    }

}
