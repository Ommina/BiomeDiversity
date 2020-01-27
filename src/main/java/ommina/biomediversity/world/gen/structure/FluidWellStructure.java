package ommina.biomediversity.world.gen.structure;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.MarginedStructureStart;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;
import java.util.function.Function;

public class FluidWellStructure extends ScatteredStructure<NoFeatureConfig> {

    public static final int BASE_HEIGHT = 38; // centre of sphere
    public static final float FREQUENCY_MULTIPLIER = 0.002f;
    public static final float BASE_FREQUENCY = 0.0039f;

    private static final int MINIMUM_DISTANCE_TO_STRONGHOLD = 100;
    private static final int MINIMUM_DISTANCE_TO_MINESHAFT = 16;
    private static final int MAXIMUM_RADIUS = 18;

    public FluidWellStructure( Function<Dynamic<?>, ? extends NoFeatureConfig> config ) {
        super( config );
    }

    //region Overrides
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

    @Override
    protected ChunkPos getStartPositionForPosition( ChunkGenerator<?> chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ ) {

        int i = 20;
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
    protected int getBiomeFeatureDistance( ChunkGenerator<?> chunkGenerator ) {
        return 24;
    }

    @Override
    protected int getBiomeFeatureSeparation( ChunkGenerator<?> chunkGenerator ) {
        return 8;
    }

    @Override
    protected int getSeedModifier() {
        return 90210;
    }
//endregion Overrides

    public static class Start extends MarginedStructureStart {

        public Start( Structure<?> structure, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int referenceIn, long seed ) {
            super( structure, chunkX, chunkZ, boundingBox, referenceIn, seed );
        }

        //region Overrides
        @Override
        public void init( ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn ) {

            int i = chunkX * 16;
            int j = chunkZ * 16;

            int weirdOffset = MAXIMUM_RADIUS / 2;//9

            BlockPos blockpos = new BlockPos( i + weirdOffset, BASE_HEIGHT, j + weirdOffset );

            this.components.add( new FluidWellStructurePiece( blockpos, MAXIMUM_RADIUS ) );

            this.recalculateStructureSize();

        }
//endregion Overrides

    }

/*

    public BlockPos getPos() {

        int weirdOffset = MAXIMUM_RADIUS / 2;//9

        return new BlockPos( (this.getChunkPosX() << 4) + weirdOffset, 0, (this.getChunkPosZ() << 4) + weirdOffset );

    }

    @Override
    public void generateStructure( IWorld worldIn, Random rand, MutableBoundingBox structurebb, ChunkPos pos ) {

        synchronized( this.components ) {
            Iterator<StructurePiece> iterator = this.components.iterator();

            while ( iterator.hasNext() ) {
                StructurePiece structurepiece = iterator.next();
                if ( structurepiece.getBoundingBox().intersectsWith( structurebb ) && !structurepiece.addComponentParts( worldIn, rand, structurebb, pos ) ) {
                    iterator.remove();
                }
            }

            this.recalculateStructureSize();
        }
    }

}

*/

}
