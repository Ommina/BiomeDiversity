package ommina.biomediversity.world.gen;

import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.world.gen.feature.ModFeatures;

public class ModWorldGeneration {

    private static final OreFeature ORE_FEATURE_CONFIG = new OreFeature( OreFeatureConfig::deserialize );

    public static void generate() {

        for ( Biome biome : ForgeRegistries.BIOMES ) {

            if ( Config.orinociteOreEnabled.get() && biome.getCategory() != Biome.Category.THEEND && biome.getCategory() != Biome.Category.NETHER ) {
                addOre( biome, ModBlocks.ORINOCITE_ORE, Config.orinociteOreGenerationSizeBase.get() + Config.orinociteOreGenerationSizeVariance.get(), Config.orinociteOreGenerationAttempts.get(), Config.orinociteOreGenerationMinY.get(), Config.orinociteOreGenerationMaxY.get() );
            }

            if ( Config.junglePoolGenerationEnabled.get() && biome.getCategory() == Biome.Category.JUNGLE ) {
                addJunglePools( biome );
            }

            if ( Config.fluidWellGenerationEnabled.get() )
                addFluidWells( biome, ModBlocks.FLUID_WELL );

            if ( Config.nocifiedStoneEnabled.get() && biome.getCategory() == Biome.Category.EXTREME_HILLS ) {
                addOre( biome, ModBlocks.STONE_NOCIFIED_UNDAMAGED, Config.nocifiedStoneGenerationSizeBase.get() + Config.nocifiedStoneGenerationSizeVariance.get(), Config.nocifiedStoneGenerationAttempts.get(), Config.nocifiedStoneGenerationMinY.get(), Config.nocifiedStoneGenerationMaxY.get() );
            }

            if ( Config.pomegranateGenerationEnabled.get() && biome.getCategory() == Biome.Category.SAVANNA ) {
                addPlant( ModFeatures.POMEGRANTE, biome );
            }

            if ( Config.colzaGenerationEnabled.get() && biome.getCategory() == Biome.Category.PLAINS ) {
                addPlant( ModFeatures.COLZA, biome );
            }

        }

    }

    private static void addOre( Biome biome, Block block, int size, int chances, int minHeight, int maxHeight ) {

        biome.addFeature( GenerationStage.Decoration.UNDERGROUND_ORES,
             Feature.ORE.withConfiguration(
                  new OreFeatureConfig( OreFeatureConfig.FillerBlockType.NATURAL_STONE, block.getDefaultState(), size ) )
                  .withPlacement( Placement.COUNT_RANGE.configure( new CountRangeConfig( chances, minHeight, 0, maxHeight ) ) ) );

    }

    private static void addJunglePools( Biome biome ) {

        biome.addFeature( GenerationStage.Decoration.TOP_LAYER_MODIFICATION,
             ModFeatures.JUNGLE_POOL.withConfiguration( IFeatureConfig.NO_FEATURE_CONFIG ).withPlacement( Placement.NOPE.configure( IPlacementConfig.NO_PLACEMENT_CONFIG ) ) );

    }

    private static void addFluidWells( Biome biome, Block block ) {

        biome.addFeature( GenerationStage.Decoration.UNDERGROUND_ORES,
             ModFeatures.FLUID_WELL.withConfiguration(
                  new OreFeatureConfig( OreFeatureConfig.FillerBlockType.NATURAL_STONE, block.getDefaultState(), 1 ) )
                  .withPlacement( Placement.COUNT_RANGE.configure( new CountRangeConfig( 1, 1, 0, 1 ) ) ) );


        // biome.addFeature( GenerationStage.Decoration.UNDERGROUND_STRUCTURES,
        //      ModFeatures.FLUID_WELL.withConfiguration( IFeatureConfig.NO_FEATURE_CONFIG ).withPlacement( Placement.NOPE.configure( IPlacementConfig.NO_PLACEMENT_CONFIG ) ) );


//        biomeIn.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES,
//             Feature.WOODLAND_MANSION.func_225566_b_(IFeatureConfig.NO_FEATURE_CONFIG).func_227228_a_(Placement.NOPE.func_227446_a_(IPlacementConfig.NO_PLACEMENT_CONFIG)));


        //       biomeIn.addFeature(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, Feature.BURIED_TREASURE.func_225566_b_(new BuriedTreasureConfig(0.01F)).func_227228_a_(Placement.NOPE.func_227446_a_(IPlacementConfig.NO_PLACEMENT_CONFIG)));
        //      biome.addFeature( GenerationStage.Decoration.UNDERGROUND_STRUCTURES, Biome.createDecoratedFeature( ModFeatures.FLUID_WELL, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig( 10 ) ) );

    }

    private static void addPlant( Feature<NoFeatureConfig> feature, Biome biome ) {

        biome.addFeature( GenerationStage.Decoration.VEGETAL_DECORATION,
             feature.withConfiguration( IFeatureConfig.NO_FEATURE_CONFIG ).withPlacement( Placement.COUNT_HEIGHTMAP_DOUBLE.configure( new FrequencyConfig( 4 ) ) ) );

    }

}
