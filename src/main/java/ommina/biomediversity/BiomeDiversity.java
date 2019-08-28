package ommina.biomediversity;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.client.ClientProxy;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.items.ModItems;
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
        //FMLJavaModLoadingContext.get().getModEventBus().addListener( this::doClientStuff );


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register( this );
    }


    private void setup( final FMLCommonSetupEvent event ) {

        DeferredWorkQueue.runLater( ModWorldGeneration::generate );

    }

    /*


    private void doClientStuff( final FMLClientSetupEvent event ) {
        // do something that can only be done on the client
        LOGGER.info( "Got game settings {}", event.getMinecraftSupplier().get().gameSettings );
    }

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
        public static void onBlocksRegistry( final RegistryEvent.Register<Block> event ) {

            ModBlocks.register( event );
        }

        @SubscribeEvent
        public static void onItemsRegistry( final RegistryEvent.Register<Item> event ) {

            ModItems.register( event );

        }

        @SubscribeEvent
        public static void onTileEntityRegistry( final RegistryEvent.Register<TileEntityType<?>> event ) {

            ModTileEntities.register( event );
        }

    }

}
