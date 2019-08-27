package ommina.biomediversity.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.blocks.GenericBlock;
import ommina.biomediversity.blocks.collector.Collector;
import ommina.biomediversity.blocks.peltier.Peltier;
import ommina.biomediversity.blocks.pillar.Pillar;
import ommina.biomediversity.blocks.receiver.Receiver;

public class ModBlocks {

    public static GenericBlock oreOrinocite;

    @ObjectHolder( BiomeDiversity.MODID + ":pillar" ) public static Pillar pillar = new Pillar();
    @ObjectHolder( BiomeDiversity.MODID + ":receiver" ) public static Receiver receiver = new Receiver();
    @ObjectHolder( BiomeDiversity.MODID + ":collector" ) public static Collector collector = new Collector();
    @ObjectHolder( BiomeDiversity.MODID + ":peltier" ) public static Peltier peltier = new Peltier();

    public static void register( final RegistryEvent.Register<Block> event ) {

        oreOrinocite = registerBlock( event, "orinocite_ore", Block.Properties.create( Material.ROCK ).hardnessAndResistance( 3.0f ) );

        event.getRegistry().registerAll( pillar, receiver, collector, peltier );

    }

    private static GenericBlock registerBlock( final RegistryEvent.Register<Block> event, final String name, final Block.Properties properties ) {

        GenericBlock block = new GenericBlock( name, properties );

        event.getRegistry().register( block );

        return block;

    }

}
