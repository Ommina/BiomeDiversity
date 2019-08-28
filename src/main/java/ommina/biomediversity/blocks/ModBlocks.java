package ommina.biomediversity.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.collector.Collector;
import ommina.biomediversity.blocks.crops.PomegranateBlock;
import ommina.biomediversity.blocks.peltier.Peltier;
import ommina.biomediversity.blocks.transmitter.Transmitter;
import ommina.biomediversity.blocks.receiver.Receiver;

public class ModBlocks {


    @ObjectHolder( BiomeDiversity.MODID + ":orinocite_ore" ) public static Block ORE_ORINOCITE = new Block( Block.Properties.create( Material.ROCK ).hardnessAndResistance( 3.0f ) ).setRegistryName( BiomeDiversity.getId( "orinocite_ore" ) );

    @ObjectHolder( BiomeDiversity.MODID + ":transmitter" ) public static final Transmitter TRANSMITTER = new Transmitter();
    @ObjectHolder( BiomeDiversity.MODID + ":receiver" ) public static final Receiver receiver = new Receiver();
    @ObjectHolder( BiomeDiversity.MODID + ":collector" ) public static final Collector collector = new Collector();
    @ObjectHolder( BiomeDiversity.MODID + ":peltier" ) public static final Peltier peltier = new Peltier();

    @ObjectHolder( BiomeDiversity.MODID + ":pomegranate" )
    public static final Block POMEGRANATE = new PomegranateBlock( Block.Properties.create( Material.PLANTS ).doesNotBlockMovement().tickRandomly().hardnessAndResistance( 0f ).sound( SoundType.CROP ) ).setRegistryName( BiomeDiversity.getId( "pomegranate" ) );

    @ObjectHolder( BiomeDiversity.MODID + ":colza" )
    public static final Block COLZA = new PomegranateBlock( Block.Properties.create( Material.PLANTS ).doesNotBlockMovement().tickRandomly().hardnessAndResistance( 0f ).sound( SoundType.CROP ) ).setRegistryName( BiomeDiversity.getId( "colza" ) );


    //public static final Block WHEAT = register("wheat", new CropsBlock(Block.Properties.create( Material.PLANTS).doesNotBlockMovement().tickRandomly().zeroHardnessAndResistance().sound( SoundType.CROP)));


    public static void register( final RegistryEvent.Register<Block> event ) {


        event.getRegistry().registerAll( TRANSMITTER, receiver, collector, peltier );
        event.getRegistry().registerAll( POMEGRANATE, COLZA, ORE_ORINOCITE );

    }

}
