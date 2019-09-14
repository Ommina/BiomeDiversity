package ommina.biomediversity.world.structure;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import ommina.biomediversity.BiomeDiversity;

import java.util.Random;
import java.util.function.Function;

public class FluidWellStructure extends ScatteredStructure<NoFeatureConfig> {

    private static final int MINIMUM_DISTANCE_TO_STRONGHOLD = 100;
    private static final int MINIMUM_DISTANCE_TO_MINESHAFT = 16;

    private static final int MAXIMUM_RADIUS = 17;

    public static final int BASE_HEIGHT = 26; // centre of sphere
    public static final float FREQUENCY_MULTIPLIER = 0.002f;
    public static final float BASE_FREQUENCY = 0.0039f;

    public FluidWellStructure( String name, Function<Dynamic<?>, ? extends NoFeatureConfig> config ) {
        super( config );

        setRegistryName( BiomeDiversity.getId( name ) );
    }

    @Override
    protected ChunkPos getStartPositionForPosition( ChunkGenerator<?> chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ ) {

        int i = 12;
        int j = 8;
        int k = x + i * spacingOffsetsX;
        int l = z + i * spacingOffsetsZ;
        int i1 = k < 0 ? k - i + 1 : k;
        int j1 = l < 0 ? l - i + 1 : l;
        int k1 = i1 / i;
        int l1 = j1 / i;
        ((SharedSeedRandom) random).setLargeFeatureSeedWithSalt( chunkGenerator.getSeed(), k1, l1, this.getSeedModifier() );
        k1 = k1 * i;
        l1 = l1 * i;
        k1 = k1 + random.nextInt( i - j );
        l1 = l1 + random.nextInt( i - j );

        return new ChunkPos( k1, l1 );

    }

    @Override
    protected int getSeedModifier() {
        return 90210;
    }

    @Override
    public IStartFactory getStartFactory() {
        return FluidWellStructure.Start::new;
    }

    @Override
    public String getStructureName() {
        return "BiomeDiveristy_Fluid_Well";
    }

    @Override
    public int getSize() {
        return 3; // No idea what this does.  I hope it is something exciting.
    }

    public static class Start extends StructureStart {

        public Start( Structure<?> p_i51165_1_, int p_i51165_2_, int p_i51165_3_, Biome p_i51165_4_, MutableBoundingBox p_i51165_5_, int p_i51165_6_, long p_i51165_7_ ) {
            super( p_i51165_1_, p_i51165_2_, p_i51165_3_, p_i51165_4_, p_i51165_5_, p_i51165_6_, p_i51165_7_ );
        }

        public void init( ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn ) {

            int i = chunkX * 16;
            int j = chunkZ * 16;

            BlockPos blockpos = new BlockPos( i + 9, 90, j + 9 );

            this.components.add( new FluidWellStructurePiece( blockpos, MAXIMUM_RADIUS ) );

            this.recalculateStructureSize();

        }

        public BlockPos getPos() {
            return new BlockPos( (this.getChunkPosX() << 4) + 9, 0, (this.getChunkPosZ() << 4) + 9 );
        }

    }

    @Override
    protected int getBiomeFeatureDistance( ChunkGenerator<?> chunkGenerator ) {
        return 24;
    }

    @Override
    protected int getBiomeFeatureSeparation( ChunkGenerator<?> chunkGenerator ) {
        return 8;
    }


}
