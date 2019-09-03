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
import ommina.biomediversity.blocks.rainbarrel.RainBarrel;
import ommina.biomediversity.blocks.receiver.Receiver;
import ommina.biomediversity.blocks.transmitter.Transmitter;

@ObjectHolder( BiomeDiversity.MODID )
@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class ModBlocks {

    // Generic Blocks
    @ObjectHolder( "orinocite_ore" ) public static final Block ORE_ORINOCITE = new Block( Block.Properties.create( Material.ROCK ).hardnessAndResistance( 3.0f ) );

    // Crops
    @ObjectHolder( "colza" ) public static final Block COLZA = new PomegranateBlock( Block.Properties.create( Material.PLANTS ).doesNotBlockMovement().tickRandomly().hardnessAndResistance( 0f ).sound( SoundType.CROP ) );
    @ObjectHolder( "pomegranate" ) public static final Block POMEGRANATE = new PomegranateBlock( Block.Properties.create( Material.PLANTS ).doesNotBlockMovement().tickRandomly().hardnessAndResistance( 0f ).sound( SoundType.CROP ) );

    // Tile Entity Blocks
    @ObjectHolder( "collector" ) public static final Collector COLLECTOR = new Collector();
    @ObjectHolder( "peltier" ) public static final Peltier PELTIER = new Peltier();
    @ObjectHolder( "rainbarrel" ) public static final RainBarrel RAIN_BARREL = new RainBarrel();
    @ObjectHolder( "receiver" ) public static final Receiver RECEIVER = new Receiver();
    @ObjectHolder( "transmitter" ) public static final Transmitter TRANSMITTER = new Transmitter();


    @SubscribeEvent
    public static void register( final RegistryEvent.Register<Block> event ) {

        register( event, "orinocite_ore", ORE_ORINOCITE );

        register( event, "colza", COLZA );
        register( event, "pomegranate", POMEGRANATE );

        register( event, "collector", COLLECTOR );
        register( event, "peltier", PELTIER );
        register( event, "rainbarrel", RAIN_BARREL );
        register( event, "receiver", RECEIVER );
        register( event, "transmitter", TRANSMITTER );

    }

    private static void register( final RegistryEvent.Register<Block> event, final String name, final Block block ) {

        block.setRegistryName( BiomeDiversity.getId( name ) );

        event.getRegistry().register( block );

    }

}
