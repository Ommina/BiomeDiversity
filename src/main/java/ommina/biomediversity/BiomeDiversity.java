package ommina.biomediversity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
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
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.blocks.cluster.ClusterBlock;
import ommina.biomediversity.blocks.collector.FastTesrCollector;
import ommina.biomediversity.blocks.plug.FastTesrPlug;
import ommina.biomediversity.blocks.plug.energy.PlugEnergyScreen;
import ommina.biomediversity.blocks.rainbarrel.FastTesrRainBarrel;
import ommina.biomediversity.blocks.receiver.FastTesrReceiver;
import ommina.biomediversity.blocks.receiver.ReceiverScreen;
import ommina.biomediversity.blocks.transmitter.FastTesrTransmitter;
import ommina.biomediversity.blocks.transmitter.TransmitterScreen;
import ommina.biomediversity.client.ClientProxy;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.fluids.DeferredRegistration;
import ommina.biomediversity.fluids.FluidFactory;
import ommina.biomediversity.network.Network;
import ommina.biomediversity.server.ServerProxy;
import ommina.biomediversity.world.gen.ModWorldGeneration;
import ommina.biomediversity.worlddata.TransmitterNetwork;
import ommina.biomediversity.worlddata.capabilities.ITransmitterNetwork;
import ommina.biomediversity.worlddata.capabilities.TransmitterNetworkProvider;
import ommina.biomediversity.worlddata.capabilities.TransmitterNetworkStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod( BiomeDiversity.MODID )
public class BiomeDiversity {

    public static final boolean DEBUG = true;
    public static final IProxy PROXY = DistExecutor.runForDist( () -> ClientProxy::new, () -> ServerProxy::new );
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "biomediversity";
    public static final ItemGroup TAB = new CreativeTab();

    @CapabilityInject( ITransmitterNetwork.class )
    public static Capability<ITransmitterNetwork> TRANSMITTER_NETWORK_CAPABILITY;

    public BiomeDiversity() {

        DeferredRegistration.setup();

        FluidFactory.init();

        ModLoadingContext.get().registerConfig( ModConfig.Type.CLIENT, Config.CLIENT_CONFIG );
        ModLoadingContext.get().registerConfig( ModConfig.Type.COMMON, Config.COMMON_CONFIG );

        FMLJavaModLoadingContext.get().getModEventBus().addListener( this::CommonSetup );

        Config.loadConfig( Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve( MODID + "-client.toml" ) );
        Config.loadConfig( Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve( MODID + "-common.toml" ) );

        // Register the enqueueIMC method for modloading
        //FMLJavaModLoadingContext.get().getModEventBus().addListener( this::enqueueIMC );
        // Register the processIMC method for modloading
        //FMLJavaModLoadingContext.get().getModEventBus().addListener( this::processIMC );

        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener( this::ClientSetup );

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register( this );
    }

    @Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
    public static class ForgeEvents {


    }

    @Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT )
    public static class ClientEvents {

        @SubscribeEvent
        public static void BindTesr( final FMLClientSetupEvent event ) {

            ClientRegistry.bindTileEntityRenderer( ModTileEntities.COLLECTOR, FastTesrCollector::new );
            ClientRegistry.bindTileEntityRenderer( ModTileEntities.RAIN_BARREL, FastTesrRainBarrel::new );
            ClientRegistry.bindTileEntityRenderer( ModTileEntities.TRANSMITTER, FastTesrTransmitter::new );
            ClientRegistry.bindTileEntityRenderer( ModTileEntities.RECEIVER, FastTesrReceiver::new );
            ClientRegistry.bindTileEntityRenderer( ModTileEntities.PLUG_ENERGY, FastTesrPlug::new );
            ClientRegistry.bindTileEntityRenderer( ModTileEntities.PLUG_FLUID, FastTesrPlug::new );

        }

        @SubscribeEvent
        public static void addSprites( final TextureStitchEvent.Pre event ) {

            event.addSprite( BiomeDiversity.getId( "block/cluster/cluster_glow_internal" ) );
            event.addSprite( BiomeDiversity.getId( "block/cluster/cluster_glow_external" ) );
            event.addSprite( BiomeDiversity.getId( "gui/overlay" ) );
            //event.addSprite( BiomeDiversity.getId( "gui/biome" ) );
            event.addSprite( BiomeDiversity.getId( "gui/log_gauge" ) );

        }

    }

    @Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.FORGE )
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onBlockHighlight( final DrawHighlightEvent event ) {

            if ( event.getTarget().getType() == RayTraceResult.Type.BLOCK && Minecraft.getInstance().world.getBlockState( new BlockPos( event.getTarget().getHitVec() ) ).getBlock() instanceof ClusterBlock )
                event.setCanceled( true );

        }

    }

    public static ResourceLocation getId( String path ) {

        return new ResourceLocation( MODID, path );

    }

    @SubscribeEvent
    public void onAttachCapabilities( AttachCapabilitiesEvent<World> event ) {

        if ( event.getObject().isRemote )
            return;

        event.addCapability( BiomeDiversity.getId( "transmitternetwork" ), new TransmitterNetworkProvider() );

        //event.addListener(() -> inst.invalidate())

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

    private void ClientSetup( final FMLClientSetupEvent event ) {

        ScreenManager.registerFactory( ModBlocks.RECEIVER_CONTAINER, ReceiverScreen::new );
        ScreenManager.registerFactory( ModBlocks.PLUG_ENERGY_CONTAINER, PlugEnergyScreen::new );
        ScreenManager.registerFactory( ModBlocks.TRANSMITTER_CONTAINER, TransmitterScreen::new );

        //Minecraft.getMinecraft().getItemColors().registerItemColorHandler( new DustTinter(), ModItems.fluidItems.values().toArray( new ItemBase[0] ) );

    }

    private void CommonSetup( final FMLCommonSetupEvent event ) {

        DeferredWorkQueue.runLater( ModWorldGeneration::generate );

        CapabilityManager.INSTANCE.register( ITransmitterNetwork.class, new TransmitterNetworkStorage(), TransmitterNetwork::new );

        Network.init();

        Config.parseFluidList();

    }

}
