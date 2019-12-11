package ommina.biomediversity.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.blocks.BlockNocifiedUndamaged;
import ommina.biomediversity.blocks.cluster.ClusterBlock;
import ommina.biomediversity.blocks.collector.Collector;
import ommina.biomediversity.blocks.crops.ColzaBlock;
import ommina.biomediversity.blocks.crops.FakePlantBlock;
import ommina.biomediversity.blocks.crops.PomegranateBlock;
import ommina.biomediversity.blocks.peltier.Peltier;
import ommina.biomediversity.blocks.plug.energy.PlugEnergy;
import ommina.biomediversity.blocks.plug.energy.PlugEnergyContainer;
import ommina.biomediversity.blocks.rainbarrel.RainBarrel;
import ommina.biomediversity.blocks.receiver.Receiver;
import ommina.biomediversity.blocks.receiver.ReceiverContainer;
import ommina.biomediversity.blocks.transmitter.Transmitter;
import ommina.biomediversity.blocks.transmitter.TransmitterContainer;


@ObjectHolder( BiomeDiversity.MODID )
@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class ModBlocks {

    // Generic Blocks
    @ObjectHolder( "orinocite_ore" ) public static final Block ORINOCITE_ORE = new Block( Block.Properties.create( Material.ROCK ).hardnessAndResistance( 3.0f ) );
    @ObjectHolder( "orinocite_block" ) public static final Block ORINOCITE_BLOCK = new Block( Block.Properties.create( Material.ROCK ).hardnessAndResistance( 3.0f ) );
    @ObjectHolder( "nocified_stone_undamaged" ) public static final Block STONE_NOCIFIED_UNDAMAGED = new BlockNocifiedUndamaged( Block.Properties.create( Material.ROCK ).hardnessAndResistance( 15f ) );
    @ObjectHolder( "nocified_stone_fractured" ) public static final Block STONE_NOCIFIED_FRACTURED = new Block( Block.Properties.create( Material.ROCK ).hardnessAndResistance( 15f ) );

    // Cluster Blocks
    @ObjectHolder( "cluster_block_generic" ) public static final ClusterBlock CLUSTER_BLOCK_GENERIC = new ClusterBlock( Block.Properties.create( Material.ROCK ).hardnessAndResistance( 2.8f ) );
    @ObjectHolder( "cluster_block_tank" ) public static final ClusterBlock CLUSTER_BLOCK_TANK = new ClusterBlock( Block.Properties.create( Material.ROCK ).hardnessAndResistance( 2.8f ) );
    @ObjectHolder( "cluster_block_sturdy" ) public static final ClusterBlock CLUSTER_BLOCK_STURDY = new ClusterBlock( Block.Properties.create( Material.ROCK ).hardnessAndResistance( 3.2f ) );

    // Crops
    @ObjectHolder( "colza" ) public static final Block COLZA = new ColzaBlock( Block.Properties.create( Material.PLANTS ).doesNotBlockMovement().tickRandomly().hardnessAndResistance( 0.05f ).sound( SoundType.CROP ) );
    @ObjectHolder( "pomegranate" ) public static final Block POMEGRANATE = new PomegranateBlock( Block.Properties.create( Material.PLANTS ).doesNotBlockMovement().tickRandomly().hardnessAndResistance( 0.05f ).sound( SoundType.CROP ) );
    @ObjectHolder( "world_colza" ) public static final Block WORLD_COLZA = new FakePlantBlock( Block.Properties.create( Material.PLANTS ).doesNotBlockMovement().hardnessAndResistance( 0.05f ).sound( SoundType.PLANT ) );
    @ObjectHolder( "world_pomegranate" ) public static final Block WORLD_POMEGRANATE = new FakePlantBlock( Block.Properties.create( Material.PLANTS ).doesNotBlockMovement().hardnessAndResistance( 0.05f ).sound( SoundType.PLANT ) );

    // Tile Entity Blocks
    @ObjectHolder( "collector" ) public static Collector COLLECTOR;
    @ObjectHolder( "peltier" ) public static Peltier PELTIER;
    @ObjectHolder( "rainbarrel" ) public static RainBarrel RAIN_BARREL;
    @ObjectHolder( "receiver" ) public static Receiver RECEIVER;
    @ObjectHolder( "transmitter" ) public static Transmitter TRANSMITTER;
    @ObjectHolder( "plug_energy" ) public static PlugEnergy PLUG_ENERGY;

    // Containers
    @ObjectHolder( "receiver" ) public static ContainerType<ReceiverContainer> RECEIVER_CONTAINER;
    @ObjectHolder( "plug_energy" ) public static ContainerType<PlugEnergyContainer> PLUG_ENERGY_CONTAINER;
    @ObjectHolder( "transmitter" ) public static ContainerType<TransmitterContainer> TRANSMITTER_CONTAINER;

    // Fluid Blocks  (Only those that we care about)
    @ObjectHolder( "mineralwater" ) public static FlowingFluidBlock MINERALWATER;
    @ObjectHolder( "junglewater" ) public static FlowingFluidBlock JUNGLEWATER;

    @SubscribeEvent
    public static void onContainerRegistry( final RegistryEvent.Register<ContainerType<?>> event ) {

        event.getRegistry().register( IForgeContainerType.create( ( windowId, inv, data ) -> {
            BlockPos pos = data.readBlockPos();
            return new ReceiverContainer( windowId, BiomeDiversity.PROXY.getClientWorld(), pos, inv, BiomeDiversity.PROXY.getClientPlayer() );
        } ).setRegistryName( "receiver" ) );

        event.getRegistry().register( IForgeContainerType.create( ( windowId, inv, data ) -> {
            BlockPos pos = data.readBlockPos();
            return new TransmitterContainer( windowId, BiomeDiversity.PROXY.getClientWorld(), pos, inv, BiomeDiversity.PROXY.getClientPlayer() );
        } ).setRegistryName( "transmitter" ) );

        event.getRegistry().register( IForgeContainerType.create( ( windowId, inv, data ) -> {
            BlockPos pos = data.readBlockPos();
            return new PlugEnergyContainer( windowId, BiomeDiversity.PROXY.getClientWorld(), pos, inv, BiomeDiversity.PROXY.getClientPlayer() );
        } ).setRegistryName( "plug_energy" ) );

    }

    @SubscribeEvent
    public static void register( final RegistryEvent.Register<Block> event ) {

        register( event, "orinocite_ore", ORINOCITE_ORE );
        register( event, "orinocite_block", ORINOCITE_BLOCK );
        register( event, "nocified_stone_fractured", STONE_NOCIFIED_FRACTURED );
        register( event, "nocified_stone_undamaged", STONE_NOCIFIED_UNDAMAGED );

        register( event, "cluster_block_generic", CLUSTER_BLOCK_GENERIC );
        register( event, "cluster_block_tank", CLUSTER_BLOCK_TANK );
        register( event, "cluster_block_sturdy", CLUSTER_BLOCK_STURDY );

        register( event, "colza", COLZA );
        register( event, "pomegranate", POMEGRANATE );
        register( event, "world_colza", WORLD_COLZA );
        register( event, "world_pomegranate", WORLD_POMEGRANATE );

        register( event, "collector", new Collector() );
        register( event, "peltier", new Peltier() );
        register( event, "rainbarrel", new RainBarrel() );
        register( event, "receiver", new Receiver() );
        register( event, "transmitter", new Transmitter() );
        register( event, "plug_energy", new PlugEnergy() );

    }

    private static void register( final RegistryEvent.Register<Block> event, final String name, final Block block ) {

        block.setRegistryName( BiomeDiversity.getId( name ) );

        event.getRegistry().register( block );

    }

}
