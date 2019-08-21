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

    public static ForgeConfigSpec.IntValue Orinocite_Generation_MinY;
    public static ForgeConfigSpec.IntValue Orinocite_Generation_MaxY;
    public static ForgeConfigSpec.IntValue Orinocite_Generation_Base_Size;
    public static ForgeConfigSpec.IntValue Orinocite_Generation_Variance;
    public static ForgeConfigSpec.IntValue Orinocite_Generation_Chances;

    // Pillars
    public static final String CATEGORY_PILLAR = "pillars";

    public static ForgeConfigSpec.IntValue Pillar_Capacity;

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    static {

        COMMON_BUILDER.comment( "Worldgen Settings" ).push( CATEGORY_WORLD_GEN );
        setupWorldGenConfig();
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment( "Pillar Configuration" ).push( CATEGORY_PILLAR );
        setupPillar();
        COMMON_BUILDER.pop();


        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private Config() {
    }

    private static void setupWorldGenConfig() {

        COMMON_BUILDER.comment( "Orinocite Ore" ).push( SUBCATEGORY_ORINOCITE_ORE );

        Orinocite_Generation_MinY = COMMON_BUILDER.comment( "Minimum y-level for ore to spawn" ).defineInRange( "ymin", 11, 5, 200 );
        Orinocite_Generation_MaxY = COMMON_BUILDER.comment( "Maximum y=level for ore to spawn" ).defineInRange( "ymax", 18, 5, 200 );
        Orinocite_Generation_Base_Size = COMMON_BUILDER.comment( "Base size of an Orinocite ore vein" ).defineInRange( "baseSize", 5, 1, 20 );
        Orinocite_Generation_Variance = COMMON_BUILDER.comment( "Size variance of an Orinocite ore vein" ).defineInRange( "variance", 3, 0, 15 );
        Orinocite_Generation_Chances = COMMON_BUILDER.comment( "Attempt per chunk to spawn a vein (0 to disable)" ).defineInRange( "attempts", 3, 0, 10 );

        COMMON_BUILDER.pop();

    }

    private static void setupPillar() {

        Pillar_Capacity = COMMON_BUILDER.comment( "Pillar capacity in mb.  Larger values need less attention." ).defineInRange( "capacity", 64000, 1000, 128000 );

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
