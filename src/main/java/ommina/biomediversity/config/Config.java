package ommina.biomediversity.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.fluids.FluidStrengths;

import java.nio.file.Path;
import java.util.*;

@Mod.EventBusSubscriber
public class Config {

    public static final String CATEGORY_WORLD_GEN = "worldgen";

    public static final String SUBCATEGORY_ORINOCITE_ORE = "orinocite_ore";
    public static final String SUBCATEGORY_JUNGLE_POOLS = "jungle_pools";
    public static final String SUBCATEGORY_FLUID_WELLS = "fluid_wells";
    public static final String SUBCATEGORY_NOCIFIED_STONE = "nocified_stone";
    public static final String SUBCATEGORY_POMEGRANATE = "pomegranate";
    public static final String SUBCATEGORY_COLZA = "colza";

    public static final String CATEGORY_POWER = "power";
    public static final String SUBCATEGORY_BIOMES = "biomes";

    public static final String CATEGORY_TRANSMITTER = "transmitters";
    public static final String CATAGORY_RECEIVER = "receivers";
    public static final String CATEGORY_RAINBARREL = "rainbarrel";
    public static final String CATEGORY_COLLECTOR = "collector";

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    // WorldGen
    public static ForgeConfigSpec.BooleanValue orinociteOreEnabled;
    public static ForgeConfigSpec.IntValue orinociteOreGenerationMinY;
    public static ForgeConfigSpec.IntValue orinociteOreGenerationMaxY;
    public static ForgeConfigSpec.IntValue orinociteOreGenerationSizeBase;
    public static ForgeConfigSpec.IntValue orinociteOreGenerationSizeVariance;
    public static ForgeConfigSpec.IntValue orinociteOreGenerationAttempts;
    public static ForgeConfigSpec.BooleanValue junglePoolGenerationEnabled;
    public static ForgeConfigSpec.BooleanValue fluidWellGenerationEnabled;
    public static ForgeConfigSpec.BooleanValue pomegranateGenerationEnabled;
    public static ForgeConfigSpec.BooleanValue colzaGenerationEnabled;
    public static ForgeConfigSpec.IntValue fluidWellGenerationRadiusBase;
    public static ForgeConfigSpec.IntValue fluidWellProbability;
    public static ForgeConfigSpec.BooleanValue nocifiedStoneEnabled;
    public static ForgeConfigSpec.IntValue nocifiedStoneGenerationMinY;
    public static ForgeConfigSpec.IntValue nocifiedStoneGenerationMaxY;
    public static ForgeConfigSpec.IntValue nocifiedStoneGenerationSizeBase;
    public static ForgeConfigSpec.IntValue nocifiedStoneGenerationSizeVariance;
    public static ForgeConfigSpec.IntValue nocifiedStoneGenerationAttempts;

    // Power Generation
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> powerFluidWhitelist;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> powerBiomeWhitelist;
    public static Set<String> powerBiomes;
    public static ForgeConfigSpec.IntValue powerMaxBiomeCount;
    public static ForgeConfigSpec.DoubleValue powerBiomeDiversity;
    public static ForgeConfigSpec.IntValue powerBiomeAdjustment;
    public static ForgeConfigSpec.DoubleValue powerFinalMultiplier;
    public static ForgeConfigSpec.IntValue powerMaxReceiversPerFluid;
    public static ForgeConfigSpec.DoubleValue powerRepeatedFluidPenalty;

    // Transmitters
    public static ForgeConfigSpec.IntValue transmitterCapacity;

    // Receivers
    public static ForgeConfigSpec.BooleanValue receiverEnableChunkLoading;
    public static ForgeConfigSpec.BooleanValue receiverRequirePowerToOperate;
    public static ForgeConfigSpec.BooleanValue receiverRequirePowerToChunkload;
    public static ForgeConfigSpec.IntValue receiverPowerCapacity;
    public static ForgeConfigSpec.IntValue receiverPowerConsumptionBase;
    public static ForgeConfigSpec.IntValue receiverPowerConsumptionChunloading;

    // Rain Barrel
    public static ForgeConfigSpec.IntValue rainbarrelCapacity;

    // Collector
    public static ForgeConfigSpec.BooleanValue collectorIsSelfThrottleEnabled;
    public static ForgeConfigSpec.IntValue collectorEnergyCapacity;

    // Cluster Tiles (Receivers, Plugs)
    public static ForgeConfigSpec.IntValue clusterCollectorSearchHorizontal;
    public static ForgeConfigSpec.IntValue clusterCollectorSearchVerticalPos;
    public static ForgeConfigSpec.IntValue clusterCollectorSearchVerticalNeg;

    static {

        setupWorldGenConfig();
        setupPowerConfig();
        setupTransmitter();
        setupReceiver();
        setupRainBarrel();
        setupCollector();

        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();

        parseBiomeList();
        //parseFluidList();

    }

    private Config() {
    }

    public static void loadConfig( ForgeConfigSpec spec, Path path ) {

        final CommentedFileConfig configData = CommentedFileConfig.builder( path )
             .sync()
             .autosave()
             .writingMode( WritingMode.REPLACE )
             .preserveInsertionOrder()
             .build();

        configData.load();
        spec.setConfig( configData );

    }

/*

    @SubscribeEvent
    public static void onLoad( final ModConfig.Loading configEvent ) {

        System.out.println( "Config Loaded" );

    }

    @SubscribeEvent
    public static void onReload( final ModConfig.ConfigReloading configEvent ) {

        System.out.println( "Config REloaded" );

    }

*/

    public static void setupCollector() {

        COMMON_BUILDER.comment( "Collector Configuration" ).push( CATEGORY_COLLECTOR );

        {

            collectorIsSelfThrottleEnabled = COMMON_BUILDER.comment( "Should the collector turn off automatically when it cannot accept a full round of energy, saving the player's fluid.  If false, the player will still be able to automate the shutoff via a comparator and redstone." ).define( "collector_self_throttle", true );
            collectorEnergyCapacity = COMMON_BUILDER.comment( "Energy stored by the collector, in FE units.  Energy produced greater than this amount will be discarded." ).defineInRange( "collector_capacity", 1000000, 10, Integer.MAX_VALUE );

        }

        COMMON_BUILDER.pop();

    }

    private static void setupWorldGenConfig() {

        COMMON_BUILDER.comment( "Worldgen Settings" ).push( CATEGORY_WORLD_GEN );

        {

            COMMON_BUILDER.comment( "Orinocite Ore" ).push( SUBCATEGORY_ORINOCITE_ORE );

            orinociteOreEnabled = COMMON_BUILDER.comment( "Enabled generation of Orinocite ore" ).define( "enableOrinociteOre", true );
            orinociteOreGenerationMinY = COMMON_BUILDER.comment( "Minimum y-level for ore to spawn" ).defineInRange( "ymin", 11, 5, 200 );
            orinociteOreGenerationMaxY = COMMON_BUILDER.comment( "Maximum y=level for ore to spawn" ).defineInRange( "ymax", 18, 5, 200 );
            orinociteOreGenerationSizeBase = COMMON_BUILDER.comment( "Base size of an Orinocite ore vein" ).defineInRange( "baseSize", 5, 1, 20 );
            orinociteOreGenerationSizeVariance = COMMON_BUILDER.comment( "Size variance of an Orinocite ore vein" ).defineInRange( "variance", 3, 0, 15 );
            orinociteOreGenerationAttempts = COMMON_BUILDER.comment( "Attempt per chunk to spawn a vein" ).defineInRange( "attempts", 3, 1, 15 );

            COMMON_BUILDER.pop();

            COMMON_BUILDER.comment( "Jungle Pools" ).push( SUBCATEGORY_JUNGLE_POOLS );

            junglePoolGenerationEnabled = COMMON_BUILDER.comment( "Enables generation of semi-common single-block pools in Jungle biomes." ).define( "enable_jungle_pools", true );

            COMMON_BUILDER.pop();

            COMMON_BUILDER.comment( "Fluid Wells" ).push( SUBCATEGORY_FLUID_WELLS );

            fluidWellGenerationEnabled = COMMON_BUILDER.comment( "Enables generation of (fairly large) underground pools of 'mineral-water' fluid.  Suitable for consumption directly, or processed for a greater return." ).define( "enable_fluid_wells", true );
            fluidWellGenerationRadiusBase = COMMON_BUILDER.comment( "Starting radius of the generated mineral-water well." ).defineInRange( "wellSizeRadiusBase", 13, 13 - 2, 13 + 2 );
            fluidWellProbability = COMMON_BUILDER.comment( "Probability of generating a fluid well.  Larger values mean greater probability." ).defineInRange( "wellProbability", 5, 1, 2000 );


            COMMON_BUILDER.pop();

            COMMON_BUILDER.comment( "Nocified Stone" ).push( SUBCATEGORY_NOCIFIED_STONE );

            nocifiedStoneEnabled = COMMON_BUILDER.comment( "Enables generation of nocified stone in Mountain biomes." ).define( "enable_nocified_stone", true );
            nocifiedStoneGenerationMinY = COMMON_BUILDER.comment( "Minimum y-level for ore to spawn" ).defineInRange( "ymin", 10, 5, 200 );
            nocifiedStoneGenerationMaxY = COMMON_BUILDER.comment( "Maximum y=level for ore to spawn" ).defineInRange( "ymax", 50, 5, 200 );
            nocifiedStoneGenerationSizeBase = COMMON_BUILDER.comment( "Base size of an Nocified stone vein" ).defineInRange( "baseSize", 1, 1, 20 );
            nocifiedStoneGenerationSizeVariance = COMMON_BUILDER.comment( "Size variance of a Nocified stone vein" ).defineInRange( "variance", 0, -1, 15 );
            nocifiedStoneGenerationAttempts = COMMON_BUILDER.comment( "Attempt per chunk to spawn a vein" ).defineInRange( "attempts", 2, 1, 15 );

            COMMON_BUILDER.pop();

            COMMON_BUILDER.comment( "Pomegranate Generation" ).push( SUBCATEGORY_POMEGRANATE );

            pomegranateGenerationEnabled = COMMON_BUILDER.comment( "Enables generation of small pomegrante patches in savanna-type biomes." ).define( "enable_pomegranate", true );

            COMMON_BUILDER.pop();

            COMMON_BUILDER.comment( "Colza Generation" ).push( SUBCATEGORY_COLZA );

            colzaGenerationEnabled = COMMON_BUILDER.comment( "Enables generation of small colza patches in plains-type biomes." ).define( "enable_colza", true );

            COMMON_BUILDER.pop();

        }

        COMMON_BUILDER.pop();

    }

    private static void setupTransmitter() {

        COMMON_BUILDER.comment( "Transmitter Configuration" ).push( CATEGORY_TRANSMITTER );

        {

            transmitterCapacity = COMMON_BUILDER.comment( "Transmitter capacity in mb.  Larger values need less attention." ).defineInRange( "capacity", 64000, 1000, 128000 );

        }

        COMMON_BUILDER.pop();

    }

    private static void setupRainBarrel() {

        COMMON_BUILDER.comment( "Rainbarrel Configuration" ).push( CATEGORY_RAINBARREL );

        {

            rainbarrelCapacity = COMMON_BUILDER.comment( "Rain Barrel capacity in mb." ).defineInRange( "capacity", 32000, 1000, 128000 );

        }

        COMMON_BUILDER.pop();

    }

    private static void setupReceiver() {

        COMMON_BUILDER.comment( "Receiver Configuration" ).push( CATAGORY_RECEIVER );

        {

            receiverEnableChunkLoading = COMMON_BUILDER.comment( "Let the Receiver chunkload distant Transmitters when fluid amounts get low." ).define( "receiver_enable_chunkloading", true );

            clusterCollectorSearchHorizontal = COMMON_BUILDER.comment( "Horizontal distance a (radius) an unlinked cluster component will search when looking for a Collector" ).defineInRange( "cluster_search_horizontal", 9, 3, 18 );
            clusterCollectorSearchVerticalPos = COMMON_BUILDER.comment( ("Vertical distance above (+y) itself an unlinked cluster component will search when looking for a collector") ).defineInRange( "cluster_search_vertial_pos", 3, 1, 6 );
            clusterCollectorSearchVerticalNeg = COMMON_BUILDER.comment( ("Vertical distance below (-y) itself an unlinked cluster component will search when looking for a collector") ).defineInRange( "cluster_search_vertial_neg", 2, 1, 6 );

            receiverRequirePowerToOperate = COMMON_BUILDER.comment( "Require RF to operate.  This means a third-party will be required to jump-start energy generation." ).define( "receiver_require_power_base", false );
            receiverRequirePowerToChunkload = COMMON_BUILDER.comment( "Require RF to chunkload Transmitter chunks (as necessary)" ).define( "receiver_require_power_chunkloading", false );

            receiverPowerCapacity = COMMON_BUILDER.comment( "Receiver RF Capacity.  Only useful if one of the two 'Require RF' options is 'true'" ).defineInRange( "receiver_power_capacity", 100000, 1000, 1000000 );
            receiverPowerConsumptionBase = COMMON_BUILDER.comment( "Receiver baseline energy consumption.  RF/tick" ).defineInRange( "receiver_power_consumption_baseline", 1, 1, 1024 );
            receiverPowerConsumptionChunloading = COMMON_BUILDER.comment( "Receiver chunkloading energy consumption.  RF/tick" ).defineInRange( "receiver_power_consumption_chunkloading", 1, 1, 8192 );

        }

        COMMON_BUILDER.pop();

    }

    private static void setupPowerConfig() {

        COMMON_BUILDER.comment( "Power Generation" ).push( CATEGORY_POWER );

        {

            COMMON_BUILDER.comment( "Biomes" ).push( SUBCATEGORY_BIOMES );

            powerBiomeWhitelist = COMMON_BUILDER.defineList( "biomes", Arrays.asList( "minecraft:*" ), o -> o instanceof String );
            powerFluidWhitelist = COMMON_BUILDER.defineList( "fluids", Config::getDefaultFluidList, o -> o instanceof String );
            powerMaxBiomeCount = COMMON_BUILDER.comment( "Maximum number of unique biomes that will be counted.  May be useful if there is a biome-adding mod installed, and the ease of finding new biomes sends energy production through the roof" ).defineInRange( "max_biome_count", 20, 1, 128 );
            powerBiomeDiversity = COMMON_BUILDER.comment( "Bonus multiplier for each unique biome (from the biome list) with a paired Transmitter.\nUsed in conjunction with fluid strength to determine total energy produced." ).defineInRange( "biome_diversity", 1.088d, 1d, 3.0d );
            powerBiomeAdjustment = COMMON_BUILDER.comment( "Value to add to energy produced by biome_diversity.  Applied BEFORE final multiplier" ).defineInRange( "biome_adjustment", -11, -10000, +10000 );
            powerFinalMultiplier = COMMON_BUILDER.comment( "After all other calculations are done, the result is multiplied by this value.\nValues greater than 1.00 will increase energy production dramatically.\nLikewise, less than 1.00 will reduce energy produced, and 0 will effectively disable energy entirely." ).defineInRange( "final_multiplier", 1d, 0d, 100d );
            powerMaxReceiversPerFluid = COMMON_BUILDER.comment( "Maximum number of times a fluid can be used before a suffering a energy-generation penalty.  Encourages using multiple fluids." ).defineInRange( "max_fluid_receivers", 8, 1, 999 );
            powerRepeatedFluidPenalty = COMMON_BUILDER.comment( "Energy creation penalty for repeated fluids.  A percentage loss multiplied by number of receivers over the limit." ).defineInRange( "repeater_fluid_penalty", 0.046d, 0d, 0.5d );

            COMMON_BUILDER.pop();

        }

        COMMON_BUILDER.pop();

    }

    private static List<String> getDefaultFluidList() {

        List<String> fluids = new ArrayList<>();

        fluids.add( "minecraft:water:10" );
        fluids.add( BiomeDiversity.MODID + ":rainwater:30" );
        fluids.add( BiomeDiversity.MODID + ":mineralwater:60" );
        fluids.add( "minecraft:lava:120" );
        fluids.add( BiomeDiversity.MODID + ":byproduct:120" );
        fluids.add( BiomeDiversity.MODID + ":lightmineralwater:160" );
        fluids.add( BiomeDiversity.MODID + ":heavymineralwater:260" );
        fluids.add( BiomeDiversity.MODID + ":warmbiometic:540" );
        fluids.add( BiomeDiversity.MODID + ":coolbiometic:540" );
        fluids.add( BiomeDiversity.MODID + ":junglewater:640" );
        fluids.add( BiomeDiversity.MODID + ":neutralbiometic:1080" );

        // final String[] defaults = new String[] {    "freshwater:128", "murkywater:192", "enrichedwater:256", "natural:300", "paledeliquescent:320",
        //       "brightdeliquescent:510", "sparklingdeliquescent:715", "scintillatingdeliquescent:920",  "dilutenatural:1402" };

        return fluids;

    }

    private static void parseBiomeList() {

        powerBiomes = new HashSet<String>();

        for ( String s : powerBiomeWhitelist.get() ) {

            String[] s2 = s.split( ":" );

            if ( s2.length != 2 ) {
                BiomeDiversity.LOGGER.warn( "Unable to parse biome whitelist config entry " + s + ", skipping." );
                continue;
            }

            for ( Biome b : ForgeRegistries.BIOMES.getValues() ) {

                ResourceLocation resourceLocation = b.getRegistryName();

                if ( s2[0].equals( resourceLocation.getNamespace() ) && (s2[1].equals( "*" ) || s2[1].equalsIgnoreCase( resourceLocation.getPath() )) ) {
                    powerBiomes.add( b.getRegistryName().toString() );
                }

            }
        }

    }

    public static void parseFluidList() {

        FluidStrengths.clear();

        for ( String s : powerFluidWhitelist.get() ) {

            String[] s2 = s.split( ":" );

            if ( s2.length != 3 ) {
                BiomeDiversity.LOGGER.warn( "Unable to parse fluid whitelist config entry " + s + ", skipping.  Should be {namespace}:{fluidname}:{energy}" );
                continue;
            }

            for ( Fluid f : ForgeRegistries.FLUIDS.getValues() ) {

                ResourceLocation resourceLocation = f.getRegistryName();

                try {

                    if ( s2[0].equals( resourceLocation.getNamespace() ) &&
                         s2[1].equalsIgnoreCase( resourceLocation.getPath() ) &&
                         Integer.parseInt( s2[2] ) > 0 && Integer.parseInt( s2[2] ) < Constants.MAX_FLUID_STRENGTH ) {
                        FluidStrengths.add( f, Integer.parseInt( s2[2] ) );
                    }

                } catch ( NumberFormatException e ) {

                    BiomeDiversity.LOGGER.warn( "Unable to parse fluid energy value for config entry " + s + ", skipping." );

                }
            }

        }

    }

}
