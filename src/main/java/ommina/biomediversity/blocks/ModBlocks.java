package ommina.biomediversity.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.blocks.BlockNocifiedUndamaged;
import ommina.biomediversity.blocks.collector.Collector;
import ommina.biomediversity.blocks.crops.PomegranateBlock;
import ommina.biomediversity.blocks.peltier.Peltier;
import ommina.biomediversity.blocks.rainbarrel.RainBarrel;
import ommina.biomediversity.blocks.receiver.Receiver;
import ommina.biomediversity.blocks.transmitter.Transmitter;


@ObjectHolder( BiomeDiversity.MODID )
@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class ModBlocks {

    // Generic Blocks
    @ObjectHolder( "orinocite_ore" ) public static final Block ORE_ORINOCITE = new Block( Block.Properties.create( Material.ROCK ).hardnessAndResistance( 3.0f ) );
    @ObjectHolder( "nocified_stone_undamaged" ) public static final Block STONE_NOCIFIED_UNDAMAGED = new BlockNocifiedUndamaged( Block.Properties.create( Material.ROCK ).hardnessAndResistance( 15f ) );
    @ObjectHolder( "nocified_stone_fractured" ) public static final Block STONE_NOCIFIED_FRACTURED = new Block( Block.Properties.create( Material.ROCK ).hardnessAndResistance( 15f ) );

    // Crops
    @ObjectHolder( "colza" ) public static final Block COLZA = new PomegranateBlock( Block.Properties.create( Material.PLANTS ).doesNotBlockMovement().tickRandomly().hardnessAndResistance( 0f ).sound( SoundType.CROP ) );
    @ObjectHolder( "pomegranate" ) public static final Block POMEGRANATE = new PomegranateBlock( Block.Properties.create( Material.PLANTS ).doesNotBlockMovement().tickRandomly().hardnessAndResistance( 0f ).sound( SoundType.CROP ) );

    // Tile Entity Blocks
    @ObjectHolder( "collector" ) public static Collector COLLECTOR;
    @ObjectHolder( "peltier" ) public static Peltier PELTIER;
    @ObjectHolder( "rainbarrel" ) public static RainBarrel RAIN_BARREL;
    @ObjectHolder( "receiver" ) public static Receiver RECEIVER;
    @ObjectHolder( "transmitter" ) public static Transmitter TRANSMITTER;

    // Fluid Blocks  (Only those that we care about)
    @ObjectHolder( "mineralwater" ) public static FlowingFluidBlock MINERALWATER;
    @ObjectHolder( "junglewater" ) public static FlowingFluidBlock JUNGLEWATER;


    @SubscribeEvent
    public static void register( final RegistryEvent.Register<Block> event ) {

        register( event, "orinocite_ore", ORE_ORINOCITE );
        register( event, "nocified_stone_fractured", STONE_NOCIFIED_FRACTURED );
        register( event, "nocified_stone_undamaged", STONE_NOCIFIED_UNDAMAGED );

        register( event, "colza", COLZA );
        register( event, "pomegranate", POMEGRANATE );

        register( event, "collector", new Collector() );
        register( event, "peltier", new Peltier() );
        register( event, "rainbarrel", new RainBarrel() );
        register( event, "receiver", new Receiver() );
        register( event, "transmitter", new Transmitter() );

    }

    private static void register( final RegistryEvent.Register<Block> event, final String name, final Block block ) {

        block.setRegistryName( BiomeDiversity.getId( name ) );

        event.getRegistry().register( block );

    }

}
