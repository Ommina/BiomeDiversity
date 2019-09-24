package ommina.biomediversity.world.gen;

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
import ommina.biomediversity.world.gen.feature.ModFeatures;

public class ModWorldGeneration {

    private static OreFeature ORE_FEATURE_CONFIG = new OreFeature( OreFeatureConfig::deserialize );

    public static void generate() {

        for ( Biome biome : ForgeRegistries.BIOMES ) {

            if ( Config.orinociteOreEnabled.get() && biome.getCategory() != Biome.Category.THEEND && biome.getCategory() != Biome.Category.NETHER ) {
                addOre( biome, ModBlocks.ORE_ORINOCITE, Config.orinociteOreGenerationSizeBase.get() + Config.orinociteOreGenerationSizeVariance.get(), Config.orinociteOreGenerationAttempts.get(), Config.orinociteOreGenerationMinY.get(), Config.orinociteOreGenerationMaxY.get() );
            }

            if ( Config.junglePoolsEnabled.get() && biome.getCategory() == Biome.Category.JUNGLE ) {
                addJunglePools( biome );
            }

            if ( Config.fluidWellsEnabled.get() )
                addFluidWells( biome );

            if ( Config.nocifiedStoneEnabled.get() && biome.getCategory() == Biome.Category.EXTREME_HILLS ) {
                addOre( biome, ModBlocks.STONE_NOCIFIED_UNDAMAGED, Config.nocifiedStoneGenerationSizeBase.get() + Config.nocifiedStoneGenerationSizeVariance.get(), Config.nocifiedStoneGenerationAttempts.get(), Config.nocifiedStoneGenerationMinY.get(), Config.nocifiedStoneGenerationMaxY.get() );
            }

        }

    }

    private static void addOre( Biome biome, Block block, int size, int chances, int minHeight, int maxHeight ) {

        biome.addFeature( GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
             ORE_FEATURE_CONFIG,
             new OreFeatureConfig( OreFeatureConfig.FillerBlockType.NATURAL_STONE, block.getDefaultState(), size ),
             Placement.COUNT_RANGE,
             new CountRangeConfig( chances, minHeight, 0, maxHeight )
        ) );

    }

    private static void addJunglePools( Biome biome ) {

        biome.addFeature( GenerationStage.Decoration.TOP_LAYER_MODIFICATION, Biome.createDecoratedFeature( ModFeatures.JUNGLE_POOL, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig( 10 ) ) );

    }

    private static void addFluidWells( Biome biome ) {

        biome.addStructure( ModFeatures.FLUID_WELL, IFeatureConfig.NO_FEATURE_CONFIG );
        biome.addFeature( GenerationStage.Decoration.UNDERGROUND_STRUCTURES, Biome.createDecoratedFeature( ModFeatures.FLUID_WELL, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig( 10 ) ) );

    }

    /*

    private void generateCrop( BlockCrops crop, Random random, int x, int z, World world ) {

        if ( !GeneratePomegranateBush.shouldGenerate() )
            return;

        GeneratePomegranateBush generator = new GeneratePomegranateBush();

        generator.generate( world, x + 8, z + 8, random );

    }

*/

}
