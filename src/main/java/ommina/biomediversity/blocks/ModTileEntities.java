package ommina.biomediversity.blocks;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.collector.TileEntityCollector;
import ommina.biomediversity.blocks.peltier.TileEntityPeltier;
import ommina.biomediversity.blocks.receiver.TileEntityReceiver;
import ommina.biomediversity.blocks.transmitter.TileEntityTransmitter;

public class ModTileEntities {

    @ObjectHolder( BiomeDiversity.MODID + ":transmitter" ) public static TileEntityType<TileEntityTransmitter> TRANSMITTER;
    @ObjectHolder( BiomeDiversity.MODID + ":receiver" ) public static TileEntityType<TileEntityReceiver> RECEIVER;
    @ObjectHolder( BiomeDiversity.MODID + ":collector" ) public static TileEntityType<TileEntityCollector> COLLECTOR;
    @ObjectHolder( BiomeDiversity.MODID + ":peltier" ) public static TileEntityType<TileEntityPeltier> PELTIER;

    public static void register( final RegistryEvent.Register<TileEntityType<?>> event ) {

        event.getRegistry().register( TileEntityType.Builder.create( TileEntityTransmitter::new, ModBlocks.TRANSMITTER ).build( null ).setRegistryName( "transmitter" ) );
        event.getRegistry().register( TileEntityType.Builder.create( TileEntityReceiver::new, ModBlocks.receiver ).build( null ).setRegistryName( "receiver" ) );
        event.getRegistry().register( TileEntityType.Builder.create( TileEntityCollector::new, ModBlocks.collector ).build( null ).setRegistryName( "collector" ) );
        event.getRegistry().register( TileEntityType.Builder.create( TileEntityTransmitter::new, ModBlocks.peltier ).build( null ).setRegistryName( "peltier" ) );

    }

}
