package ommina.biomediversity.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModBlocks;

public class ModItems {

    public static Item oreOrinocite;
    public static Item ingotOrinocite;

    @ObjectHolder( BiomeDiversity.MODID + ":linkstaff" ) public static Item linkStaff = new LinkStaff( LinkStaff.NAME, new Item.Properties() ).setRegistryName( BiomeDiversity.getId( "linkstaff" ) );

    public static Item pillar;
    public static Item receiver;
    public static Item collector;
    public static Item peltier;

    @ObjectHolder( BiomeDiversity.MODID + ":pomegranate_seeds" ) public static final Item POMEGRANATE_SEEDS = new BlockNamedItem( ModBlocks.POMEGRANATE, (new Item.Properties()).group( BiomeDiversity.TAB ) ).setRegistryName( BiomeDiversity.getId( "pomegranate_seeds" ) );
    @ObjectHolder( BiomeDiversity.MODID + ":pomegranate" ) public static final Item POMEGRANATE = new Item( new Item.Properties().food( ModFoods.POMEGRANATE ).group( BiomeDiversity.TAB ) ).setRegistryName( BiomeDiversity.getId( "pomegranate" ) );

    @ObjectHolder( BiomeDiversity.MODID + ":colza_seeds" ) public static final Item COLZA_SEEDS = new BlockNamedItem( ModBlocks.COLZA, (new Item.Properties()).group( BiomeDiversity.TAB ) ).setRegistryName( BiomeDiversity.getId( "colza_seeds" ) );
    @ObjectHolder( BiomeDiversity.MODID + ":colza" ) public static final Item COLZA = new Item( new Item.Properties().group( BiomeDiversity.TAB ) ).setRegistryName( BiomeDiversity.getId( "colza" ) );


    public static void register( final RegistryEvent.Register<Item> event ) {

        oreOrinocite = registerItem( event, ModBlocks.oreOrinocite, new Item.Properties() );
        ingotOrinocite = registerItem( event, "orinocite_ingot", new Item.Properties() );

        pillar = registerItem( event, ModBlocks.pillar, new Item.Properties() );
        receiver = registerItem( event, ModBlocks.receiver, new Item.Properties() );
        collector = registerItem( event, ModBlocks.collector, new Item.Properties() );
        peltier = registerItem( event, ModBlocks.peltier, new Item.Properties() );

        event.getRegistry().registerAll( POMEGRANATE, POMEGRANATE_SEEDS, COLZA, COLZA_SEEDS, linkStaff );

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
