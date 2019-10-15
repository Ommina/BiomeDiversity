package ommina.biomediversity.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.collector.TileEntityCollector;
import ommina.biomediversity.blocks.collector.PacketUpdateCollector;
import ommina.biomediversity.blocks.receiver.PacketUpdateReceiver;
import ommina.biomediversity.blocks.receiver.TileEntityReceiver;

public class Network {

    private static final ResourceLocation NAME = BiomeDiversity.getId( "network" );
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel channel;
    private static int channelId = 0;

    static {

        channel = NetworkRegistry.ChannelBuilder.named( NAME )
             .clientAcceptedVersions( PROTOCOL_VERSION::equals )
             .serverAcceptedVersions( PROTOCOL_VERSION::equals )
             .networkProtocolVersion( () -> PROTOCOL_VERSION )
             .simpleChannel();

        channel.messageBuilder( GenericTankUpdatePacket.class, channelId++ )
             .decoder( GenericTankUpdatePacket::fromBytes )
             .encoder( GenericTankUpdatePacket::toBytes )
             .consumer( GenericTankUpdatePacket::handle )
             .add();

        channel.messageBuilder( PacketUpdateReceiver.class, channelId++ )
             .decoder( PacketUpdateReceiver::fromBytes )
             .encoder( PacketUpdateReceiver::toBytes )
             .consumer( TileEntityReceiver::handle )
             .add();

        channel.messageBuilder( PacketUpdateCollector.class, channelId++ )
             .decoder( PacketUpdateCollector::fromBytes )
             .encoder( PacketUpdateCollector::toBytes )
             .consumer( TileEntityCollector::handle )
             .add();

        channel.messageBuilder( GenericTilePacketRequest.class, channelId++ )
             .decoder( GenericTilePacketRequest::fromBytes )
             .encoder( GenericTilePacketRequest::toBytes )
             .consumer( GenericTilePacketRequest::handle )
             .add();

    }

    private Network() {
    }

    public static void init() {
        // Hi!
    }

    /*

        network.registerMessage( new PacketUpdatePump.Handler(), PacketUpdatePump.class, channelId++, Side.CLIENT );
        network.registerMessage( new PacketUpdateMixer.Handler(), PacketUpdateMixer.class, channelId++, Side.CLIENT );
        network.registerMessage( new PacketUpdatePillar.Handler(), PacketUpdatePillar.class, channelId++, Side.CLIENT );
        network.registerMessage( new PacketUpdatePeltier.Handler(), PacketUpdatePeltier.class, channelId++, Side.CLIENT );
        network.registerMessage( new PacketUpdateReceiver.Handler(), PacketUpdateReceiver.class, channelId++, Side.CLIENT );
        network.registerMessage( new PacketUpdateCollector.Handler(), PacketUpdateCollector.class, channelId++, Side.CLIENT );
        network.registerMessage( new PacketUpdateGenerator.Handler(), PacketUpdateGenerator.class, channelId++, Side.CLIENT );
        network.registerMessage( new PacketUpdateDeliquesce.Handler(), PacketUpdateDeliquesce.class, channelId++, Side.CLIENT );
        network.registerMessage( new PacketUpdateRainBarrel.Handler(), PacketUpdateRainBarrel.class, channelId++, Side.CLIENT );
        network.registerMessage( new PacketUpdatePrecipitator.Handler(), PacketUpdatePrecipitator.class, channelId++, Side.CLIENT );

        network.registerMessage( new PacketRequestUpdatePump.Handler(), PacketRequestUpdatePump.class, channelId++, Side.SERVER );
        network.registerMessage( new PacketRequestUpdateMixer.Handler(), PacketRequestUpdateMixer.class, channelId++, Side.SERVER );
        network.registerMessage( new PacketRequestUpdatePillar.Handler(), PacketRequestUpdatePillar.class, channelId++, Side.SERVER );
        network.registerMessage( new PacketRequestUpdatePeltier.Handler(), PacketRequestUpdatePeltier.class, channelId++, Side.SERVER );
        network.registerMessage( new PacketRequestUpdateReceiver.Handler(), PacketRequestUpdateReceiver.class, channelId++, Side.SERVER );
        network.registerMessage( new PacketRequestUpdateCollector.Handler(), PacketRequestUpdateCollector.class, channelId++, Side.SERVER );
        network.registerMessage( new PacketRequestUpdateGenerator.Handler(), PacketRequestUpdateGenerator.class, channelId++, Side.SERVER );
        network.registerMessage( new PacketRequestUpdateDeliquesce.Handler(), PacketRequestUpdateDeliquesce.class, channelId++, Side.SERVER );
        network.registerMessage( new PacketRequestUpdateRainBarrel.Handler(), PacketRequestUpdateRainBarrel.class, channelId++, Side.SERVER );
        network.registerMessage( new PacketRequestUpdatePrecipitator.Handler(), PacketRequestUpdatePrecipitator.class, channelId++, Side.SERVER );

        */

}
