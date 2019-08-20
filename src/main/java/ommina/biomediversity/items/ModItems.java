package ommina.biomediversity.items;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.blocks.ModBlocks;

public class ModItems {

    public static Item oreOrinocite;
    public static Item ingotOrinocite;

    @ObjectHolder( LinkStaff.NAME ) public static LinkStaff linkStaff;

    public static Item pillar;

    public static void register( final RegistryEvent.Register<Item> event ) {

        oreOrinocite = registerItem( event, ModBlocks.oreOrinocite, new Item.Properties() );
        ingotOrinocite = registerItem( event, "orinocite_ingot", new Item.Properties() );

        pillar = registerItem( event, ModBlocks.pillar, new Item.Properties() );

    }

    private static Item registerItem( final RegistryEvent.Register<Item> event, final Block fromBlock, Item.Properties properties ) {

        final GenericBlockItem item = new GenericBlockItem( fromBlock, properties );

        event.getRegistry().register( item );

        return item;

    }

    private static Item registerItem( final RegistryEvent.Register<Item> event, final String name, Item.Properties properties ) {

        final GenericItem item = new GenericItem( name, properties );

        event.getRegistry().register( item );

        return item;

    }

}
