package ommina.biomediversity.blocks;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.collector.TileEntityCollector;
import ommina.biomediversity.blocks.peltier.TileEntityPeltier;
import ommina.biomediversity.blocks.plug.TileEntityPlugBase;
import ommina.biomediversity.blocks.plug.energy.TileEntityPlugEnergy;
import ommina.biomediversity.blocks.rainbarrel.TileEntityRainBarrel;
import ommina.biomediversity.blocks.receiver.TileEntityReceiver;
import ommina.biomediversity.blocks.transmitter.TileEntityTransmitter;

@ObjectHolder( BiomeDiversity.MODID )
@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class ModTileEntities {

    @ObjectHolder( "transmitter" ) public static TileEntityType<TileEntityTransmitter> TRANSMITTER;
    @ObjectHolder( "receiver" ) public static TileEntityType<TileEntityReceiver> RECEIVER;
    @ObjectHolder( "collector" ) public static TileEntityType<TileEntityCollector> COLLECTOR;
    @ObjectHolder( "peltier" ) public static TileEntityType<TileEntityPeltier> PELTIER;
    @ObjectHolder( "rainbarrel" ) public static TileEntityType<TileEntityRainBarrel> RAIN_BARREL;
    @ObjectHolder( "plug_energy" ) public static TileEntityType<TileEntityPlugBase> PLUG;

    @SubscribeEvent
    public static void register( final RegistryEvent.Register<TileEntityType<?>> event ) {

        event.getRegistry().register( TileEntityType.Builder.create( TileEntityTransmitter::new, ModBlocks.TRANSMITTER ).build( null ).setRegistryName( "transmitter" ) );
        event.getRegistry().register( TileEntityType.Builder.create( TileEntityReceiver::new, ModBlocks.RECEIVER ).build( null ).setRegistryName( "receiver" ) );
        event.getRegistry().register( TileEntityType.Builder.create( TileEntityCollector::new, ModBlocks.COLLECTOR ).build( null ).setRegistryName( "collector" ) );
        event.getRegistry().register( TileEntityType.Builder.create( TileEntityPeltier::new, ModBlocks.PELTIER ).build( null ).setRegistryName( "peltier" ) );
        event.getRegistry().register( TileEntityType.Builder.create( TileEntityRainBarrel::new, ModBlocks.RAIN_BARREL ).build( null ).setRegistryName( "rainbarrel" ) );
        event.getRegistry().register( TileEntityType.Builder.create( TileEntityPlugEnergy::new, ModBlocks.PLUG_ENERGY ).build( null ).setRegistryName( "plug_energy" ) );

    }

}
