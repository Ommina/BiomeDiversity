package ommina.biomediversity.items;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.blocks.blocks.GenericBlock;

public class ModItems {

    public static Item oreOrinocite;

    public static void register( final RegistryEvent.Register<Item> event ) {

        oreOrinocite = registerItem( event, ModBlocks.oreOrinocite, new Item.Properties() );

    }

    private static Item registerItem( final RegistryEvent.Register<Item> event, final GenericBlock fromBlock, Item.Properties properties ) {

        final GenericBlockItem item = new GenericBlockItem( fromBlock, properties );

        event.getRegistry().register( item );

        return item;

    }

}
