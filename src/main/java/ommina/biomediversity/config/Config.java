package ommina.biomediversity.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;
import ommina.biomediversity.BiomeDiversity;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public static ForgeConfigSpec.BooleanValue nocifiedStoneEnabled;
    public static ForgeConfigSpec.IntValue nocifiedStoneGenerationMinY;
    public static ForgeConfigSpec.IntValue nocifiedStoneGenerationMaxY;
    public static ForgeConfigSpec.IntValue nocifiedStoneGenerationSizeBase;
    public static ForgeConfigSpec.IntValue nocifiedStoneGenerationSizeVariance;
    public static ForgeConfigSpec.IntValue nocifiedStoneGenerationAttempts;

    // Power Generation
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> powerBiomeWhitelist;// = new ArrayList<? extends String>();
    public static Set<String> powerBiomes;

    // Transmitters
    public static ForgeConfigSpec.IntValue transmitterCapacity;

    // Receivers
    public static ForgeConfigSpec.BooleanValue receiverEnableChunkLoading;
    public static ForgeConfigSpec.BooleanValue receiverRequirePowerToOperate;
    public static ForgeConfigSpec.BooleanValue receiverRequirePowerToChunkload;
    public static ForgeConfigSpec.IntValue receiverPowerCapacity;
    public static ForgeConfigSpec.IntValue receiverPowerConsumptionBase;
    public static ForgeConfigSpec.IntValue receiverPowerConsumptionChunloading;
    public static ForgeConfigSpec.IntValue receiverCollectorSearchHorizontal;
    public static ForgeConfigSpec.IntValue receiverCollectorSearchVerticalPos;
    public static ForgeConfigSpec.IntValue receiverCollectorSearchVerticalNeg;

    // Rain Barrel
    public static ForgeConfigSpec.IntValue rainbarrelCapacity;

    static {

        setupWorldGenConfig();
        setupPowerConfig();
        setupTransmitter();
        setupReceiver();
        setupRainBarrel();

        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();

        parseBiomeList();

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

    @SubscribeEvent
    public static void onLoad( final ModConfig.Loading configEvent ) {

        System.out.println( "hello" );

    }

    @SubscribeEvent
    public static void onReload( final ModConfig.ConfigReloading configEvent ) {

        System.out.println( "hello" );

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

            fluidWellGenerationEnabled = COMMON_BUILDER.comment( "Enables generation of (fairly large) underground pools of 'silt-water' fluid.  Suitable for consumption directly, or processed for a greater return." ).define( "enable_fluid_wells", true );
            fluidWellGenerationRadiusBase = COMMON_BUILDER.comment( "Starting radius of the generated silt-water well." ).defineInRange( "wellSizeRadiusBase", 13, 13 - 2, 13 + 2 );

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

            receiverEnableChunkLoading = COMMON_BUILDER.comment( "Let the receiver chunkload distant transmitters when fluid amounts get low." ).define( "receiver_enable_chunkloading", true );

            receiverCollectorSearchHorizontal = COMMON_BUILDER.comment( "Horizontal distance a (radius) an unlinked receiver will search when looking for a Collector" ).defineInRange( "receiver_search_horizontal", 9, 3, 18 );
            receiverCollectorSearchVerticalPos = COMMON_BUILDER.comment( ("Vertical distance above (+y) itself an unlinked receiver will search when looking for a collector") ).defineInRange( "receiver_search_vertial_pos", 3, 1, 6 );
            receiverCollectorSearchVerticalNeg = COMMON_BUILDER.comment( ("Vertical distance below (-y) itself an unlinked receiver will search when looking for a collector") ).defineInRange( "receiver_search_vertial_neg", 2, 1, 6 );

            receiverRequirePowerToOperate = COMMON_BUILDER.comment( "Require RF to operate.  This means a third-party will be required to jump-start power generation." ).define( "receiver_require_power_base", false );
            receiverRequirePowerToChunkload = COMMON_BUILDER.comment( "Require RF to chunkload transmitter chunks (as necessary)" ).define( "receiver_require_power_chunkloading", false );

            receiverPowerCapacity = COMMON_BUILDER.comment( "Receiver RF Capacity.  Only useful if one of the two 'Require RF' options is 'true'" ).defineInRange( "receiver_power_capacity", 100000, 1000, 1000000 );
            receiverPowerConsumptionBase = COMMON_BUILDER.comment( "Receiver baseline power consumption.  RF/tick" ).defineInRange( "receiver_power_consumption_baseline", 1, 1, 1024 );
            receiverPowerConsumptionChunloading = COMMON_BUILDER.comment( "Receiver chunkloading power conumption.  RF/tick" ).defineInRange( "receiver_power_consumption_chunkloading", 1, 1, 8192 );

        }

        COMMON_BUILDER.pop();

    }

    private static void setupPowerConfig() {

        COMMON_BUILDER.comment( "Power Generation" ).push( CATEGORY_POWER );

        {

            COMMON_BUILDER.comment( "Biomes" ).push( SUBCATEGORY_BIOMES );

            powerBiomeWhitelist = COMMON_BUILDER.defineList( "biomes", Arrays.asList( "minecraft:*" ), o -> o instanceof String );

            //pristineOutputs.put(MobKey.BLAZE, builder.defineList(MobKey.BLAZE, Arrays.asList(DeepConstants.LOOT.BLAZE), o -> o instanceof String));


            COMMON_BUILDER.pop();

        }

        COMMON_BUILDER.pop();

    }

    private static void parseBiomeList() {

        powerBiomes = new HashSet<String>();

        for ( String s : powerBiomeWhitelist.get() ) {

            String[] s2 = s.split( ":" );

            if ( s2.length != 2 ) {
                BiomeDiversity.LOGGER.warn( "Unable to parse config entry " + s + ", skipping." );
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

}
