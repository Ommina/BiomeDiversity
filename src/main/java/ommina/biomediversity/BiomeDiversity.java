package ommina.biomediversity;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.blocks.rainbarrel.FastTesrRainBarrel;
import ommina.biomediversity.blocks.rainbarrel.TileEntityRainBarrel;
import ommina.biomediversity.client.ClientProxy;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.fluids.ModFluids;
import ommina.biomediversity.server.ServerProxy;
import ommina.biomediversity.world.ModWorldGeneration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod( "biomediversity" )
public class BiomeDiversity {

    public static final IProxy PROXY = DistExecutor.runForDist( () -> ClientProxy::new, () -> ServerProxy::new );
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "biomediversity";
    public static final ItemGroup TAB = new CreativeTab();

    public static ResourceLocation getId( String path ) {

        return new ResourceLocation( MODID, path );

    }


    public BiomeDiversity() {

        ModLoadingContext.get().registerConfig( ModConfig.Type.CLIENT, Config.CLIENT_CONFIG );
        ModLoadingContext.get().registerConfig( ModConfig.Type.COMMON, Config.COMMON_CONFIG );

        FMLJavaModLoadingContext.get().getModEventBus().addListener( this::setup );

        Config.loadConfig( Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve( MODID + "-client.toml" ) );
        Config.loadConfig( Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve( MODID + "-common.toml" ) );

        // Register the enqueueIMC method for modloading
        //FMLJavaModLoadingContext.get().getModEventBus().addListener( this::enqueueIMC );
        // Register the processIMC method for modloading
        //FMLJavaModLoadingContext.get().getModEventBus().addListener( this::processIMC );

        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener( this::doClientStuff );


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register( this );
    }


    private void setup( final FMLCommonSetupEvent event ) {

        DeferredWorkQueue.runLater( ModWorldGeneration::generate );


    }


    private void doClientStuff( final FMLClientSetupEvent event ) {

        //Minecraft.getMinecraft().getItemColors().registerItemColorHandler( new DustTinter(), ModItems.fluidItems.values().toArray( new ItemBase[0] ) );

        Minecraft.getInstance().getItemColors().register( new BucketTinter(), ModFluids.RAINWATER_BUCKET );
        Minecraft.getInstance().getBlockColors().register( new WaterTinter(), ModFluids.RAINWATER_BLOCK );

    }

        /*


    private void enqueueIMC( final InterModEnqueueEvent event ) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo( "examplemod", "helloworld", () -> {
            LOGGER.info( "Hello world from the MDK" );
            return "Hello world";
        } );
    }

    private void processIMC( final InterModProcessEvent event ) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info( "Got IMC {}", event.getIMCStream().
             map( m -> m.getMessageSupplier().get() ).
             collect( Collectors.toList() ) );
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting( FMLServerStartingEvent event ) {
        // do something when the server starts
        LOGGER.info( "HELLO from server starting" );
    }

    */

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
    public static class RegistryEvents {


        @SubscribeEvent
        public static void onTileEntityRegistry( final RegistryEvent.Register<TileEntityType<?>> event ) {

            ModTileEntities.register( event );
        }


    }

    @Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
    public static class ForgeEvents {

        @SubscribeEvent
        public static void addSprites( final TextureStitchEvent.Pre event ) { // Fluids may be unnecessary with future Forge versions

            event.addSprite( BiomeDiversity.getId( "block/fluid/fluid_blank_flow" ) );
            event.addSprite( BiomeDiversity.getId( "block/fluid/fluid_blank_still" ) );
            event.addSprite( BiomeDiversity.getId( "block/fluid/molten_metal_flow" ) );
            event.addSprite( BiomeDiversity.getId( "block/fluid/molten_metal_still" ) );
            event.addSprite( BiomeDiversity.getId( "block/fluid/viscous_blank_flow" ) );
            event.addSprite( BiomeDiversity.getId( "block/fluid/viscous_blank_still" ) );

        }

    }

    @Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT )
    public static class ClientEvents {

        @SubscribeEvent
        public static void BindTesr( final FMLClientSetupEvent event ) {

            ClientRegistry.bindTileEntitySpecialRenderer( TileEntityRainBarrel.class, new FastTesrRainBarrel<>() );

        }

    }

}
