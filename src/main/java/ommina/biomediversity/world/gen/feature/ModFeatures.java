package ommina.biomediversity.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.config.Config;

@ObjectHolder( BiomeDiversity.MODID )
@Mod.EventBusSubscriber(  modid = BiomeDiversity.MODID )
public class ModFeatures {

    @ObjectHolder( "jungle_pool" ) public static JunglePoolFeature JUNGLE_POOL;
    @ObjectHolder( "fluid_well" ) public static FluidWellFeature FLUID_WELL;
    @ObjectHolder( "pomegranate" ) public static PomegranateFeature POMEGRANTE;
    @ObjectHolder( "colza" ) public static ColzaFeature COLZA;

    private static ConfiguredFeature<?, ?> ORE_WOOL_OVERWORLD = Feature.ORE
         .withConfiguration( new OreFeatureConfig(
              OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
              Blocks.WHITE_WOOL.getDefaultState(),
              9 ) ) // vein size
         .withPlacement( Placement.RANGE.configure( new TopSolidRangeConfig( 5, 0, 10 ) ) )
         .square()
         .func_242731_b( 20 ); // number of veins per chunk

    @SubscribeEvent
    public static void register( final RegistryEvent.Register<Feature<?>> event ) {

        event.getRegistry().register( new JunglePoolFeature( NoFeatureConfig.field_236558_a_ ).setRegistryName( "jungle_pool" ) );
        event.getRegistry().register( new FluidWellFeature( OreFeatureConfig.CODEC ).setRegistryName( "fluid_well" ) );
        event.getRegistry().register( new PomegranateFeature( NoFeatureConfig.field_236558_a_ ).setRegistryName( "pomegranate" ) );
        event.getRegistry().register( new ColzaFeature( NoFeatureConfig.field_236558_a_ ).setRegistryName( "colza" ) );

    }

    @SubscribeEvent
    public static void addFeaturesToBiomes( BiomeLoadingEvent event ) {


        if ( Config.orinociteOreEnabled.get() && event.getCategory() != Biome.Category.THEEND && event.getCategory() != Biome.Category.NETHER ) {
            addOre( event, ModBlocks.ORINOCITE_ORE, Config.orinociteOreGenerationSizeBase.get() + Config.orinociteOreGenerationSizeVariance.get(), Config.orinociteOreGenerationAttempts.get(), Config.orinociteOreGenerationMinY.get(), Config.orinociteOreGenerationMaxY.get() );
        }

        /*

        if ( Config.junglePoolGenerationEnabled.get() && event.getCategory() == Biome.Category.JUNGLE ) {
            addJunglePools( biome );
        }

        if ( Config.fluidWellGenerationEnabled.get() )
            addFluidWells( biome, ModBlocks.FLUID_WELL );

        if ( Config.nocifiedStoneEnabled.get() && event.getCategory() == Biome.Category.EXTREME_HILLS ) {
            addOre( biome, ModBlocks.STONE_NOCIFIED_UNDAMAGED, Config.nocifiedStoneGenerationSizeBase.get() + Config.nocifiedStoneGenerationSizeVariance.get(), Config.nocifiedStoneGenerationAttempts.get(), Config.nocifiedStoneGenerationMinY.get(), Config.nocifiedStoneGenerationMaxY.get() );
        }

        if ( Config.pomegranateGenerationEnabled.get() && event.getCategory() == Biome.Category.SAVANNA ) {
            addPlant( ModFeatures.POMEGRANTE, biome );
        }

        if ( Config.colzaGenerationEnabled.get() && event.getCategory() == Biome.Category.PLAINS ) {
            addPlant( ModFeatures.COLZA, biome );
        }

*/
    }

    private static void addOre( BiomeLoadingEvent biomeEvent, Block block, int size, int chances, int minHeight, int maxHeight ) {

        biomeEvent.getGeneration().withFeature( GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(
             new OreFeatureConfig( OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, block.getDefaultState(), size ) )
             .withPlacement( Placement.RANGE.configure( new TopSolidRangeConfig( minHeight, 0, 10 ) ) )
             .func_242731_b( chances ) ); // bottomOffset, topOffset, maximum

        //biome.getGenerationSettings().getFeatures().add(  )

        //biome.addFeature( GenerationStage.Decoration.UNDERGROUND_ORES,
        //     Feature.ORE.withConfiguration(
        //          new OreFeatureConfig( OreFeatureConfig.FillerBlockType.NATURAL_STONE, block.getDefaultState(), size ) )
        //          .withPlacement( Placement.COUNT_RANGE.configure( new CountRangeConfig( chances, minHeight, 0, maxHeight ) ) ) );

    }

    /*

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
*/

}

