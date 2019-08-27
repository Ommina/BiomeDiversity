package ommina.biomediversity.items;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.blocks.ModBlocks;

public class ModItems {

    public static Item oreOrinocite;
    public static Item ingotOrinocite;

    @ObjectHolder( LinkStaff.NAME ) public static LinkStaff linkStaff = new LinkStaff( LinkStaff.NAME, new Item.Properties() );

    public static Item pillar;
    public static Item receiver;
    public static Item collector;
    public static Item peltier;

    public static void register( final RegistryEvent.Register<Item> event ) {

        oreOrinocite = registerItem( event, ModBlocks.oreOrinocite, new Item.Properties() );
        ingotOrinocite = registerItem( event, "orinocite_ingot", new Item.Properties() );

        pillar = registerItem( event, ModBlocks.pillar, new Item.Properties() );
        receiver = registerItem( event, ModBlocks.receiver, new Item.Properties() );
        collector = registerItem( event, ModBlocks.collector, new Item.Properties() );
        peltier = registerItem( event, ModBlocks.peltier, new Item.Properties() );

        event.getRegistry().register( linkStaff );

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
