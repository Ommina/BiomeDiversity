package ommina.biomediversity.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.collector.Collector;
import ommina.biomediversity.blocks.crops.PomegranateBlock;
import ommina.biomediversity.blocks.peltier.Peltier;
import ommina.biomediversity.blocks.receiver.Receiver;
import ommina.biomediversity.blocks.transmitter.Transmitter;

@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class ModBlocks {

    // Generic Blocks
    @ObjectHolder( BiomeDiversity.MODID + ":orinocite_ore" ) public static final Block ORE_ORINOCITE = new Block( Block.Properties.create( Material.ROCK ).hardnessAndResistance( 3.0f ) );

    // Crops
    @ObjectHolder( BiomeDiversity.MODID + ":pomegranate" ) public static final Block POMEGRANATE = new PomegranateBlock( Block.Properties.create( Material.PLANTS ).doesNotBlockMovement().tickRandomly().hardnessAndResistance( 0f ).sound( SoundType.CROP ) );
    @ObjectHolder( BiomeDiversity.MODID + ":colza" ) public static final Block COLZA = new PomegranateBlock( Block.Properties.create( Material.PLANTS ).doesNotBlockMovement().tickRandomly().hardnessAndResistance( 0f ).sound( SoundType.CROP ) );

    // Tile Entity Blocks
    @ObjectHolder( BiomeDiversity.MODID + ":transmitter" ) public static final Transmitter TRANSMITTER = new Transmitter();
    @ObjectHolder( BiomeDiversity.MODID + ":receiver" ) public static final Receiver RECEIVER = new Receiver();
    @ObjectHolder( BiomeDiversity.MODID + ":collector" ) public static final Collector COLLECTOR = new Collector();
    @ObjectHolder( BiomeDiversity.MODID + ":peltier" ) public static final Peltier PELTIER = new Peltier();

    @SubscribeEvent
    public static void onBlocksRegistry( final RegistryEvent.Register<Block> event ) {

        register( event, "orinocite_ore", ORE_ORINOCITE );

        register( event, "pomegranate", POMEGRANATE );
        register( event, "colza", COLZA );

        register( event, "transmitter", TRANSMITTER );
        register( event, "receiver", RECEIVER );
        register( event, "collector", COLLECTOR );
        register( event, "peltier", PELTIER );

    }

    private static void register( final RegistryEvent.Register<Block> event, final String name, final Block block ) {

        block.setRegistryName( BiomeDiversity.getId( name ) );

        event.getRegistry().register( block );

    }

}
