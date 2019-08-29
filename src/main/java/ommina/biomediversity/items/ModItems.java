package ommina.biomediversity.items;

import net.minecraft.item.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.fluids.ModFluids;

@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class ModItems {

    // Generic Items
    @ObjectHolder( BiomeDiversity.MODID + ":pomegranate_seeds" ) public static final Item POMEGRANATE_SEEDS = new BlockNamedItem( ModBlocks.POMEGRANATE, (new Item.Properties()).group( BiomeDiversity.TAB ) );
    @ObjectHolder( BiomeDiversity.MODID + ":pomegranate" ) public static final Item POMEGRANATE = new Item( new Item.Properties().food( ModFoods.POMEGRANATE ).group( BiomeDiversity.TAB ) );

    @ObjectHolder( BiomeDiversity.MODID + ":colza_seeds" ) public static final Item COLZA_SEEDS = new BlockNamedItem( ModBlocks.COLZA, (new Item.Properties()).group( BiomeDiversity.TAB ) );
    @ObjectHolder( BiomeDiversity.MODID + ":colza" ) public static final Item COLZA = new Item( new Item.Properties().group( BiomeDiversity.TAB ) );

    @ObjectHolder( BiomeDiversity.MODID + ":orinocite_ingot" ) public static final Item INGOT_ORINOCITE = new Item( new Item.Properties().group( BiomeDiversity.TAB ) );


    // Cool Items
    @ObjectHolder( BiomeDiversity.MODID + ":linkstaff" ) public static final Item LINK_STAFF = new LinkStaff( new Item.Properties().group( BiomeDiversity.TAB ) );

    // Block Items
    @ObjectHolder( BiomeDiversity.MODID + ":orinocite_ore" ) public static final Item ORE_ORINOCITE = new BlockItem( ModBlocks.ORE_ORINOCITE, new Item.Properties().group( BiomeDiversity.TAB ) );

    @ObjectHolder( BiomeDiversity.MODID + ":transmitter" ) public static final Item TRANSMITTER = new BlockItem( ModBlocks.TRANSMITTER, new Item.Properties().group( BiomeDiversity.TAB ) );
    @ObjectHolder( BiomeDiversity.MODID + ":receiver" ) public static final Item RECEIVER = new BlockItem( ModBlocks.RECEIVER, new Item.Properties().group( BiomeDiversity.TAB ) );
    @ObjectHolder( BiomeDiversity.MODID + ":collector" ) public static final Item COLLECTOR = new BlockItem( ModBlocks.COLLECTOR, new Item.Properties().group( BiomeDiversity.TAB ) );
    @ObjectHolder( BiomeDiversity.MODID + ":peltier" ) public static final Item PELTIER = new BlockItem( ModBlocks.PELTIER, new Item.Properties().group( BiomeDiversity.TAB ) );

    // Buckets

    @ObjectHolder( BiomeDiversity.MODID + ":rainwater_bucket" ) public static final Item RAINWATER_BUCKET = new BucketItem( ModFluids.RAINWATER, new Item.Properties().containerItem( Items.BUCKET ).maxStackSize( 1 ).group( BiomeDiversity.TAB ) );

//    public static final Item WATER_BUCKET = register("water_bucket", new BucketItem( Fluids.WATER, (new Item.Properties()).containerItem(BUCKET).maxStackSize(1).group( ItemGroup.MISC)));


    @SubscribeEvent
    public static void register( final RegistryEvent.Register<Item> event ) {

        register( event, "pomegranate_seeds", POMEGRANATE_SEEDS );
        register( event, "pomegranate", POMEGRANATE );
        register( event, "colza_seeds", COLZA_SEEDS );
        register( event, "colza", COLZA );
        register( event, "orinocite_ingot", INGOT_ORINOCITE );

        register( event, "linkstaff", LINK_STAFF );

        register( event, "orinocite_ore", ORE_ORINOCITE );

        register( event, "transmitter", TRANSMITTER );
        register( event, "receiver", RECEIVER );
        register( event, "collector", COLLECTOR );
        register( event, "peltier", PELTIER );

        register( event, "rainwater_bucket", RAINWATER_BUCKET );

    }

    private static void register( final RegistryEvent.Register<Item> event, String name, Item item ) {

        item.setRegistryName( BiomeDiversity.getId( name ) );

        event.getRegistry().register( item );

    }

}
