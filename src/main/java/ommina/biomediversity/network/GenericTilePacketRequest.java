package ommina.biomediversity.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import ommina.biomediversity.blocks.collector.PacketUpdateCollector;
import ommina.biomediversity.blocks.collector.TileEntityCollector;
import ommina.biomediversity.blocks.plug.energy.PlugEnergyPacketUpdate;
import ommina.biomediversity.blocks.plug.energy.TileEntityPlugEnergy;
import ommina.biomediversity.blocks.plug.fluid.PlugFluidPacketUpdate;
import ommina.biomediversity.blocks.plug.fluid.TileEntityPlugFluid;
import ommina.biomediversity.blocks.receiver.PacketUpdateReceiver;
import ommina.biomediversity.blocks.receiver.TileEntityReceiver;

import java.util.function.Supplier;

public class GenericTilePacketRequest {

    private BlockPos pos;

    public GenericTilePacketRequest() {
    }

    public GenericTilePacketRequest( BlockPos pos ) {

        this.pos = pos;

    }

    public static GenericTilePacketRequest fromBytes( PacketBuffer buf ) {

        GenericTilePacketRequest packet = new GenericTilePacketRequest();

        packet.pos = buf.readBlockPos();

        return packet;

    }

    public static void handle( GenericTilePacketRequest packet, Supplier<NetworkEvent.Context> ctx ) {

        ctx.get().enqueueWork( () -> {

            World world = ctx.get().getSender().world;

            BlockPos pos = packet.pos;

            if ( world.isBlockLoaded( pos ) ) {

                TileEntity tile = world.getTileEntity( pos );
                PacketDistributor.TargetPoint pd = new PacketDistributor.TargetPoint( tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), 64.0f, World.OVERWORLD );

                if ( tile instanceof TileEntityReceiver ) {
                    Network.channel.send( PacketDistributor.NEAR.with( () -> pd ), new PacketUpdateReceiver( tile ) );
                } else if ( tile instanceof TileEntityPlugEnergy ) {
                    Network.channel.send( PacketDistributor.NEAR.with( () -> pd ), new PlugEnergyPacketUpdate( tile ) );
                } else if ( tile instanceof TileEntityPlugFluid ) {
                    Network.channel.send( PacketDistributor.NEAR.with( () -> pd ), new PlugFluidPacketUpdate( tile ) );
                } else if ( tile instanceof TileEntityCollector ) {
                    Network.channel.send( PacketDistributor.NEAR.with( () -> pd ), new PacketUpdateCollector( tile ) );
                } else if ( tile instanceof ITankBroadcast ) {
                    Network.channel.send( PacketDistributor.NEAR.with( () -> pd ), new GenericTankUpdatePacket( tile ) );
                }

            }

        } );

        ctx.get().setPacketHandled( true );

    }

    public void toBytes( PacketBuffer buf ) {

        buf.writeBlockPos( this.pos );

    }

}
