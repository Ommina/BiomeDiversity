package ommina.biomediversity.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import ommina.biomediversity.blocks.blocks.GenericBlock;
import ommina.biomediversity.blocks.pillar.Pillar;

public class ModBlocks {

    public static GenericBlock oreOrinocite;
    public static Pillar pillar;

    public static void register( final RegistryEvent.Register<Block> event ) {

        oreOrinocite = registerBlock( event, "orinocite_ore", Block.Properties.create( Material.ROCK ).hardnessAndResistance( 3.0f ) );

        pillar = registerPillar( event );

    }

    private static GenericBlock registerBlock( final RegistryEvent.Register<Block> event, final String name, final Block.Properties properties ) {

        GenericBlock block = new GenericBlock( name, properties );

        event.getRegistry().register( block );

        return block;

    }

    private static Pillar registerPillar( final RegistryEvent.Register<Block> event ) {

        Pillar block = new Pillar();

        event.getRegistry().register( block );

        return block;

    }


}
