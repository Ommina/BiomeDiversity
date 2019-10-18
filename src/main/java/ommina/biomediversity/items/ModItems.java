package ommina.biomediversity.items;

import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModBlocks;

@ObjectHolder( BiomeDiversity.MODID )
@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class ModItems {

    // Generic Items
    @ObjectHolder( "pomegranate_seeds" ) public static final Item POMEGRANATE_SEEDS = null;
    @ObjectHolder( "pomegranate" ) public static final Item POMEGRANATE = null;

    @ObjectHolder( "colza_seeds" ) public static final Item COLZA_SEEDS = null;
    @ObjectHolder( "colza" ) public static final Item COLZA = null;

    @ObjectHolder( "orinocite_ingot" ) public static final Item INGOT_ORINOCITE = null;

    // Cool Items
    @ObjectHolder( "linkstaff" ) public static final Item LINK_STAFF = null;

    // Block Items
    @ObjectHolder( "orinocite_ore" ) public static final Item ORE_ORINOCITE = null;
    @ObjectHolder( "nocified_stone_fractured" ) public static final Item NOCIFIED_STONE_FRACTURED = null;
    @ObjectHolder( "nocified_stone_undamaged" ) public static final Item NOCIFIED_STONE_UNDAMAGED = null;

    @ObjectHolder( "cluster_block_generic" ) public static final Item CLUSTER_BLOCK_GENERIC = null;
    @ObjectHolder( "cluster_block_tank" ) public static final Item CLUSTER_BLOCK_TANK = null;

    @ObjectHolder( "transmitter" ) public static final Item TRANSMITTER = null;
    @ObjectHolder( "receiver" ) public static final Item RECEIVER = null;
    @ObjectHolder( "collector" ) public static final Item COLLECTOR = null;
    @ObjectHolder( "peltier" ) public static final Item PELTIER = null;
    @ObjectHolder( "rainbarrel" ) public static final Item RAIN_BARREL = null;

    @SubscribeEvent
    public static void register( final RegistryEvent.Register<Item> event ) {

        register( event, "pomegranate_seeds", new BlockNamedItem( ModBlocks.POMEGRANATE, (new Item.Properties()).group( BiomeDiversity.TAB ) ) );
        register( event, "pomegranate", new Item( new Item.Properties().food( ModFoods.POMEGRANATE ).group( BiomeDiversity.TAB ) ) );
        register( event, "colza_seeds", new BlockNamedItem( ModBlocks.COLZA, (new Item.Properties()).group( BiomeDiversity.TAB ) ) );
        register( event, "colza", new Item( new Item.Properties().group( BiomeDiversity.TAB ) ) );

        register( event, "orinocite_ingot", new Item( new Item.Properties().group( BiomeDiversity.TAB ) ) );

        register( event, "linkstaff", new LinkStaff( new Item.Properties().group( BiomeDiversity.TAB ) ) );

        register( event, "orinocite_ore", new BlockItem( ModBlocks.ORE_ORINOCITE, new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "nocified_stone_fractured", new BlockItem( ModBlocks.STONE_NOCIFIED_FRACTURED, new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "nocified_stone_undamaged", new BlockItem( ModBlocks.STONE_NOCIFIED_UNDAMAGED, new Item.Properties().group( BiomeDiversity.TAB ) ) );

        register( event, "cluster_block_generic", new BlockItem( ModBlocks.CLUSTER_BLOCK_GENERIC, new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "cluster_block_tank", new BlockItem( ModBlocks.CLUSTER_BLOCK_TANK, new Item.Properties().group( BiomeDiversity.TAB ) ) );

        register( event, "transmitter", new BlockItem( ModBlocks.TRANSMITTER, new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "receiver", new BlockItem( ModBlocks.RECEIVER, new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "collector", new BlockItem( ModBlocks.COLLECTOR, new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "peltier", new BlockItem( ModBlocks.PELTIER, new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "rainbarrel", new BlockItem( ModBlocks.RAIN_BARREL, new Item.Properties().group( BiomeDiversity.TAB ) ) );

    }

    private static void register( final RegistryEvent.Register<Item> event, String name, Item item ) {

        item.setRegistryName( BiomeDiversity.getId( name ) );

        event.getRegistry().register( item );

    }

}
