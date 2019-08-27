package ommina.biomediversity.world;

import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.world.feature.ModFeatures;

public class ModWorldGeneration {

    private static OreFeature ORE_FEATURE_CONFIG = new OreFeature( OreFeatureConfig::deserialize );

    public static void generate() {

        for ( Biome biome : ForgeRegistries.BIOMES ) {

            if ( biome.getCategory() != Biome.Category.THEEND && biome.getCategory() != Biome.Category.NETHER ) {
                if ( Config.Orinocite_Generation_Base_Size.get() > 0 ) {
                    addOre( biome, ModBlocks.oreOrinocite, Config.Orinocite_Generation_Base_Size.get() + Config.Orinocite_Generation_Variance.get(), Config.Orinocite_Generation_Chances.get(), Config.Orinocite_Generation_MinY.get(), Config.Orinocite_Generation_MaxY.get() );
                }
                addJunglePuddles( biome );
            }

        }

    }

/*

    private void generateOverworld( Random random, int chunkX, int chunkZ, World world, ChunkGenerator chunkGenerator, AbstractChunkProvider chunkProvider ) {

        int x = chunkX * 16;
        int z = chunkZ * 16;

        generateOre( ModBlocks.oreOrinocite.getDefaultState(), world, random, x, z, Config.Orinocite_Generation_MinY.get(), Config.Orinocite_Generation_MaxY.get(), Config.Orinocite_Generation_Base_Size.get() + random.nextInt( Config.Orinocite_Generation_Variance.get() ), Config.Orinocite_Generation_Chances.get() );


        generateFluidDeposit( ModFluids.natural, random, x, z, world, chunkGenerator, chunkProvider );

        generateCrop( ModBlocks.cropPomegranate, random, x, z, world );

        generateJunglePuddle( ModFluids.jungleWater, random, x, z, world );

    }

*/

    private static void addOre( Biome biome, Block block, int size, int count, int minHeight, int maxHeight ) {

        biome.addFeature( GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
             ORE_FEATURE_CONFIG,
             new OreFeatureConfig( OreFeatureConfig.FillerBlockType.NATURAL_STONE, block.getDefaultState(), size ),
             Placement.COUNT_RANGE,
             new CountRangeConfig( count, minHeight, 0, maxHeight )
        ) );
    }

    private static void addJunglePuddles( Biome biome ) {

        biome.addFeature( GenerationStage.Decoration.TOP_LAYER_MODIFICATION, Biome.createDecoratedFeature( ModFeatures.JUNGLE_POOL, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig( 10 ) ) );

    }




    /*

    private void generateFluidDeposit( BdFluid fluid, Random random, int worldX, int worldZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider ) {

        if ( GenerateFluidSphere.shouldGenerate( random ) )
            return;

        new GenerateFluidSphere().generate( world, random, worldX + 8, worldZ + 8, chunkGenerator );

    }

    private void generateCrop( BlockCrops crop, Random random, int x, int z, World world ) {

        if ( !GeneratePomegranateBush.shouldGenerate() )
            return;

        GeneratePomegranateBush generator = new GeneratePomegranateBush();

        generator.generate( world, x + 8, z + 8, random );

    }

    private void generateJunglePuddle( BdFluid fluid, Random random, int x, int z, World world ) {

        if ( !GeneratorJunglePuddle.shouldGenerate() )
            return;

        GeneratorJunglePuddle generator = new GeneratorJunglePuddle();

        generator.generate( fluid, world, x + 8, z + 8, random );

    }

*/

}
