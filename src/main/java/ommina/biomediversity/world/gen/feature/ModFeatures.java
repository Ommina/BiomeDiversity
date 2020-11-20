package ommina.biomediversity.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.registry.DeferredRegistration;

@ObjectHolder( BiomeDiversity.MODID )
@Mod.EventBusSubscriber( modid = BiomeDiversity.MODID )
public class ModFeatures {

    public static RegistryObject<JunglePoolFeature> JUNGLE_POOL_FEATURE = DeferredRegistration.FEATURES.register( "jungle_pool", () -> new JunglePoolFeature( NoFeatureConfig.field_236558_a_ ) );
    public static RegistryObject<FluidWellFeature> FLUID_WELL_FEATURE = DeferredRegistration.FEATURES.register( "fluid_well", () -> new FluidWellFeature( NoFeatureConfig.field_236558_a_ ) );
    public static RegistryObject<PomegranateFeature> POMEGRANTE_FEATURE = DeferredRegistration.FEATURES.register( "pomegranate", () -> new PomegranateFeature( NoFeatureConfig.field_236558_a_ ) );
    public static RegistryObject<ColzaFeature> COLZA_FEATURE = DeferredRegistration.FEATURES.register( "colza", () -> new ColzaFeature( NoFeatureConfig.field_236558_a_ ) );

    private static ConfiguredFeature<?, ?> JUNGLE_POOL;
    private static ConfiguredFeature<?, ?> FLUID_WELL;
    private static ConfiguredFeature<?, ?> POMEGRANTE;
    private static ConfiguredFeature<?, ?> COLZA;

    private static boolean isFeatureRegistrationComplete = false;

    private static void registerFeatures() {

        if ( isFeatureRegistrationComplete )
            return;

        JUNGLE_POOL = JUNGLE_POOL_FEATURE.get()
             .withConfiguration( new NoFeatureConfig() )
             .withPlacement( Placement.CHANCE.configure( new ChanceConfig( 3 ) ) );

        FLUID_WELL = FLUID_WELL_FEATURE.get()
             .withConfiguration( new NoFeatureConfig() )
             .withPlacement( Placement.RANGE.configure( new TopSolidRangeConfig( 5, 0, 10 ) ) )
             .square()
             .func_242731_b( 1 ); // number of veins per chunk

        POMEGRANTE = POMEGRANTE_FEATURE.get()
             .withConfiguration( new NoFeatureConfig() )
             .withPlacement( Placement.CHANCE.configure( new ChanceConfig( 3 ) ) );

        COLZA = COLZA_FEATURE.get()
             .withConfiguration( new NoFeatureConfig() )
             .withPlacement( Placement.CHANCE.configure( new ChanceConfig( 3 ) ) );

        registerFeature( JUNGLE_POOL, "jungle_pool" );
        registerFeature( FLUID_WELL, "fluid_well" );
        registerFeature( POMEGRANTE, "pomegranate" );
        registerFeature( COLZA, "colza" );

        isFeatureRegistrationComplete = true;

    }

    private static void registerFeature( ConfiguredFeature<?, ?> feature, String registryName ) {
        Registry.register( WorldGenRegistries.CONFIGURED_FEATURE, BiomeDiversity.getId( registryName ), feature );
    }

    @SubscribeEvent
    public static void addFeaturesToBiomes( BiomeLoadingEvent event ) {

        registerFeatures();

        if ( Config.orinociteOreEnabled.get() && event.getCategory() != Biome.Category.THEEND && event.getCategory() != Biome.Category.NETHER ) {
            addOre( event, ModBlocks.ORINOCITE_ORE, Config.orinociteOreGenerationSizeBase.get() + Config.orinociteOreGenerationSizeVariance.get(), Config.orinociteOreGenerationAttempts.get(), Config.orinociteOreGenerationMinY.get(), Config.orinociteOreGenerationMaxY.get() );
        }

        if ( Config.junglePoolGenerationEnabled.get() && event.getCategory() == Biome.Category.JUNGLE ) {
            addJunglePools( event );
        }

        if ( Config.fluidWellGenerationEnabled.get() )
            addFluidWells( event, ModBlocks.FLUID_WELL );

        if ( Config.nocifiedStoneEnabled.get() && event.getCategory() == Biome.Category.EXTREME_HILLS ) {
            addOre( event, ModBlocks.STONE_NOCIFIED_UNDAMAGED, Config.nocifiedStoneGenerationSizeBase.get() + Config.nocifiedStoneGenerationSizeVariance.get(), Config.nocifiedStoneGenerationAttempts.get(), Config.nocifiedStoneGenerationMinY.get(), Config.nocifiedStoneGenerationMaxY.get() );
        }

        if ( Config.pomegranateGenerationEnabled.get() && event.getCategory() == Biome.Category.SAVANNA ) {
            addPlant( ModFeatures.POMEGRANTE, event );
        }

        if ( Config.colzaGenerationEnabled.get() && event.getCategory() == Biome.Category.PLAINS ) {
            addPlant( ModFeatures.COLZA, event );
        }

    }

    private static void addOre( BiomeLoadingEvent biomeEvent, Block block, int size, int chances, int minHeight, int maxHeight ) {

        biomeEvent.getGeneration().withFeature( GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(
             new OreFeatureConfig( OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, block.getDefaultState(), size ) )
             .withPlacement( Placement.RANGE.configure( new TopSolidRangeConfig( minHeight, 0, 10 ) ) )
             .func_242731_b( chances ) ); // bottomOffset, topOffset, maximum

    }

    private static void addJunglePools( BiomeLoadingEvent biomeEvent ) {
        biomeEvent.getGeneration().withFeature( GenerationStage.Decoration.TOP_LAYER_MODIFICATION, JUNGLE_POOL );
    }

    private static void addFluidWells( BiomeLoadingEvent biomeEvent, Block block ) {
        biomeEvent.getGeneration().withFeature( GenerationStage.Decoration.UNDERGROUND_ORES, FLUID_WELL );
    }

    private static void addPlant( ConfiguredFeature<?, ?> feature, BiomeLoadingEvent biomeEvent ) {
        biomeEvent.getGeneration().withFeature( GenerationStage.Decoration.VEGETAL_DECORATION, feature );
    }

}

