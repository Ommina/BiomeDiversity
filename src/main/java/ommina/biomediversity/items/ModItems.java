package ommina.biomediversity.items;

import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.blocks.blocks.BlockProgressive;

@ObjectHolder( BiomeDiversity.MODID )
@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class ModItems {

    // Generic Items
    @ObjectHolder( "pomegranate_seeds" ) public static Item POMEGRANATE_SEEDS;
    @ObjectHolder( "pomegranate" ) public static Item POMEGRANATE;

    @ObjectHolder( "colza_seeds" ) public static Item COLZA_SEEDS;
    @ObjectHolder( "colza" ) public static Item COLZA;

    @ObjectHolder( "orinocite_ingot" ) public static Item ORINOCITE_INGOT;
    @ObjectHolder( "orinocite_dust" ) public static Item ORINOCITE_DUST;
    @ObjectHolder( "orinocite_nugget" ) public static Item ORINOCITE_NUGGET;
    @ObjectHolder( "orinocite_plate" ) public static Item ORINOCITE_PLATE;

    // Block Items
    @ObjectHolder( "orinocite_ore" ) public static Item ORINOCITE_ORE;
    @ObjectHolder( "orinocite_block" ) public static Item ORINOCITE_BLOCK;
    @ObjectHolder( "nocified_stone_fractured" ) public static Item NOCIFIED_STONE_FRACTURED;
    @ObjectHolder( "nocified_stone_undamaged" ) public static Item NOCIFIED_STONE_UNDAMAGED;

    // Has Tile Entities
    @ObjectHolder( "rainbarrel" ) public static Item RAIN_BARREL;
    @ObjectHolder( "transmitter" ) public static Item TRANSMITTER;
    @ObjectHolder( "receiver" ) public static Item RECEIVER;
    @ObjectHolder( "collector" ) public static Item COLLECTOR;
    @ObjectHolder( "plug_energy" ) public static Item PLUG_ENERGY;
    @ObjectHolder( "plug_fluid" ) public static Item PLUG_FLUID;
    @ObjectHolder( "peltier" ) public static Item PELTIER;

    // Cool Items
    @ObjectHolder( "linkstaff" ) public static Item LINK_STAFF;
    @ObjectHolder( "cluster_block_generic" ) public static Item CLUSTER_BLOCK_GENERIC;
    @ObjectHolder( "cluster_block_tank" ) public static Item CLUSTER_BLOCK_TANK;
    @ObjectHolder( "cluster_block_sturdy" ) public static Item CLUSTER_BLOCK_STURDY;
    @ObjectHolder( "orinocite_plate_thin" ) public static Item ORINOCITE_PLATE_THIN;

    // Cluster Parts
    @ObjectHolder( "machine_antenna" ) public static Item MACHINE_ANTENNA;
    @ObjectHolder( "machine_base" ) public static Item MACHINE_BASE;
    @ObjectHolder( "machine_battery" ) public static Item MACHINE_BATTERY;
    @ObjectHolder( "machine_sensor" ) public static Item MACHINE_SENSOR;
    @ObjectHolder( "machine_tank" ) public static Item MACHINE_TANK;

    @SubscribeEvent
    public static void register( RegistryEvent.Register<Item> event ) {

        register( event, "pomegranate_seeds", new BlockNamedItem( ModBlocks.POMEGRANATE, (new Item.Properties()).group( BiomeDiversity.TAB ) ) );
        register( event, "pomegranate", new Item( new Item.Properties().food( ModFoods.POMEGRANATE ).group( BiomeDiversity.TAB ) ) );
        register( event, "colza_seeds", new BlockNamedItem( ModBlocks.COLZA, (new Item.Properties()).group( BiomeDiversity.TAB ) ) );
        register( event, "colza", new Item( new Item.Properties().group( BiomeDiversity.TAB ) ) );

        register( event, "orinocite_block", new BlockItem( ModBlocks.ORINOCITE_BLOCK, new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "orinocite_ingot", new Item( new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "orinocite_nugget", new Item( new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "orinocite_dust", new Item( new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "orinocite_plate", new Item( new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "orinocite_plate_thin", new Item( new Item.Properties().group( BiomeDiversity.TAB ) ) );

        register( event, "linkstaff", new LinkStaff( new Item.Properties().group( BiomeDiversity.TAB ) ) );

        register( event, "orinocite_ore", new BlockItem( ModBlocks.ORINOCITE_ORE, new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "nocified_stone_fractured", new BlockItem( ModBlocks.STONE_NOCIFIED_FRACTURED, new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "nocified_stone_undamaged", new BlockItem( ModBlocks.STONE_NOCIFIED_UNDAMAGED, new Item.Properties().group( BiomeDiversity.TAB ) ) );

        register( event, "machine_antenna", new Item( new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "machine_base", new Item( new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "machine_battery", new Item( new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "machine_sensor", new Item( new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "machine_tank", new Item( new Item.Properties().group( BiomeDiversity.TAB ) ) );

        register( event, "cluster_block_generic", new BlockItem( ModBlocks.CLUSTER_BLOCK_GENERIC, new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "cluster_block_tank", new BlockItem( ModBlocks.CLUSTER_BLOCK_TANK, new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "cluster_block_sturdy", new BlockItem( ModBlocks.CLUSTER_BLOCK_STURDY, new Item.Properties().group( BiomeDiversity.TAB ) ) );

        register( event, "transmitter", new BlockItem( ModBlocks.TRANSMITTER, new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "receiver", new BlockItem( ModBlocks.RECEIVER, new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "collector", new BlockItem( ModBlocks.COLLECTOR, new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "peltier", new BlockItem( ModBlocks.PELTIER, new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "rainbarrel", new BlockItem( ModBlocks.RAIN_BARREL, new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "plug_energy", new BlockItem( ModBlocks.PLUG_ENERGY, new Item.Properties().group( BiomeDiversity.TAB ) ) );
        register( event, "plug_fluid_byproduct", new BlockItem( ModBlocks.PLUG_FLUID_BYPRODUCT, new Item.Properties().group( BiomeDiversity.TAB ) ) );

        for ( int n = 1; n <= 12; n++ )
            register( event, BlockProgressive.PREFIX + n, new BlockItem( ForgeRegistries.BLOCKS.getValue( BiomeDiversity.getId( BlockProgressive.PREFIX + n ) ), new Item.Properties().group( BiomeDiversity.TAB ) ) );

    }

    private static void register( RegistryEvent.Register<Item> event, String name, Item item ) {

        item.setRegistryName( BiomeDiversity.getId( name ) );

        event.getRegistry().register( item );

    }

}
