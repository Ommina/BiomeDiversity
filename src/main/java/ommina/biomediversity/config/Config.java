package ommina.biomediversity.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class Config {

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    // WorldGen
    public static final String CATEGORY_WORLD_GEN = "worldgen";

    public static final String SUBCATEGORY_ORINOCITE_ORE = "orinocite_ore";
    public static ForgeConfigSpec.BooleanValue orinociteOreEnabled;
    public static ForgeConfigSpec.IntValue orinociteOreGenerationMinY;
    public static ForgeConfigSpec.IntValue orinociteOreGenerationMaxY;
    public static ForgeConfigSpec.IntValue orinociteOreGenerationSizeBase;
    public static ForgeConfigSpec.IntValue orinociteOreGenerationSizeVariance;
    public static ForgeConfigSpec.IntValue orinociteOreGenerationAttempts;

    public static final String SUBCATEGORY_JUNGLE_POOLS = "jungle_pools";
    public static ForgeConfigSpec.BooleanValue junglePoolsEnabled;

    public static final String SUBCATEGORY_FLUID_WELLS = "fluid_wells";
    public static ForgeConfigSpec.BooleanValue fluidWellsEnabled;
    public static ForgeConfigSpec.IntValue fluidWellGenerationRadiusBase;

    public static final String SUBCATEGORY_NOCIFIED_STONE = "nocified_stone";
    public static ForgeConfigSpec.BooleanValue nocifiedStoneEnabled;
    public static ForgeConfigSpec.IntValue nocifiedStoneGenerationMinY;
    public static ForgeConfigSpec.IntValue nocifiedStoneGenerationMaxY;
    public static ForgeConfigSpec.IntValue nocifiedStoneGenerationSizeBase;
    public static ForgeConfigSpec.IntValue nocifiedStoneGenerationSizeVariance;
    public static ForgeConfigSpec.IntValue nocifiedStoneGenerationAttempts;


    // Pillars
    public static final String CATEGORY_TRANSMITTER = "transmitters";

    public static ForgeConfigSpec.IntValue Transmitter_Capacity;

    // Rain Barrel
    public static final String CATEGORY_RAINBARREL = "rainbarrel";

    public static ForgeConfigSpec.IntValue Rainbarrel_Capacity;

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    static {

        COMMON_BUILDER.comment( "Worldgen Settings" ).push( CATEGORY_WORLD_GEN );
        setupWorldGenConfig();
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment( "Pillar Configuration" ).push( CATEGORY_TRANSMITTER );
        setupTransmitter();
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment( "Rainbarrel Configuration" ).push( CATEGORY_RAINBARREL );
        setupRainBarrel();
        COMMON_BUILDER.pop();


        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private Config() {
    }

    private static void setupWorldGenConfig() {

        COMMON_BUILDER.comment( "Orinocite Ore" ).push( SUBCATEGORY_ORINOCITE_ORE );

        orinociteOreEnabled = COMMON_BUILDER.comment( "Enabled generation of Orinocite ore" ).define( "enableOrinociteOre", true );
        orinociteOreGenerationMinY = COMMON_BUILDER.comment( "Minimum y-level for ore to spawn" ).defineInRange( "ymin", 11, 5, 200 );
        orinociteOreGenerationMaxY = COMMON_BUILDER.comment( "Maximum y=level for ore to spawn" ).defineInRange( "ymax", 18, 5, 200 );
        orinociteOreGenerationSizeBase = COMMON_BUILDER.comment( "Base size of an Orinocite ore vein" ).defineInRange( "baseSize", 5, 1, 20 );
        orinociteOreGenerationSizeVariance = COMMON_BUILDER.comment( "Size variance of an Orinocite ore vein" ).defineInRange( "variance", 3, 0, 15 );
        orinociteOreGenerationAttempts = COMMON_BUILDER.comment( "Attempt per chunk to spawn a vein" ).defineInRange( "attempts", 3, 1, 15 );

        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment( "Jungle Pools" ).push( SUBCATEGORY_JUNGLE_POOLS );

        junglePoolsEnabled = COMMON_BUILDER.comment( "Enables generation of semi-common single-block pools in Jungle biomes." ).define( "enable_jungle_pools", true );

        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment( "Fluid Wells" ).push( SUBCATEGORY_FLUID_WELLS );

        fluidWellsEnabled = COMMON_BUILDER.comment( "Enables generation of (fairly large) underground pools of 'silt-water' fluid.  Suitable for consumption directly, or processed for a greater return." ).define( "enable_fluid_wells", true );
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

    }

    private static void setupTransmitter() {

        Transmitter_Capacity = COMMON_BUILDER.comment( "Transmitter capacity in mb.  Larger values need less attention." ).defineInRange( "capacity", 64000, 1000, 128000 );

    }

    private static void setupRainBarrel() {

        Rainbarrel_Capacity = COMMON_BUILDER.comment( "Rain Barrel capacity in mb." ).defineInRange( "capacity", 32000, 1000, 128000 );

    }

    public static void loadConfig( ForgeConfigSpec spec, Path path ) {

        final CommentedFileConfig configData = CommentedFileConfig.builder( path )
             .sync()
             .autosave()
             .writingMode( WritingMode.REPLACE )
             .build();

        configData.load();
        spec.setConfig( configData );
    }

    @SubscribeEvent
    public static void onLoad( final ModConfig.Loading configEvent ) {

    }

    @SubscribeEvent
    public static void onReload( final ModConfig.ConfigReloading configEvent ) {
    }

}
