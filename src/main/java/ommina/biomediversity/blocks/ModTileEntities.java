package ommina.biomediversity.blocks;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.collector.TileEntityCollector;
import ommina.biomediversity.blocks.pillar.TileEntityPillar;
import ommina.biomediversity.blocks.receiver.TileEntityReceiver;

public class ModTileEntities {

    @ObjectHolder( BiomeDiversity.MODID + ":pillar" ) public static TileEntityType<TileEntityPillar> PILLAR;
    @ObjectHolder( BiomeDiversity.MODID + ":receiver" ) public static TileEntityType<TileEntityReceiver> RECEIVER;
    @ObjectHolder( BiomeDiversity.MODID + ":collector" ) public static TileEntityType<TileEntityCollector> COLLECTOR;

    public static void register( final RegistryEvent.Register<TileEntityType<?>> event ) {

        event.getRegistry().register( TileEntityType.Builder.create( TileEntityPillar::new, ModBlocks.pillar ).build( null ).setRegistryName( "pillar" ) );
        event.getRegistry().register( TileEntityType.Builder.create( TileEntityReceiver::new, ModBlocks.receiver ).build( null ).setRegistryName( "receiver" ) );
        event.getRegistry().register( TileEntityType.Builder.create( TileEntityCollector::new, ModBlocks.collector ).build( null ).setRegistryName( "collector" ) );

    }

}
