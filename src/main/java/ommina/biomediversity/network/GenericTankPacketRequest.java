package ommina.biomediversity.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import ommina.biomediversity.fluids.IHasFluidTank;

import java.util.function.Supplier;

public class GenericTankPacketRequest {

    private BlockPos pos;

    public GenericTankPacketRequest() {
    }

    public GenericTankPacketRequest( BlockPos pos ) {

        this.pos = pos;

    }

    public static GenericTankPacketRequest fromBytes( PacketBuffer buf ) {

        GenericTankPacketRequest packet = new GenericTankPacketRequest();

        packet.pos = buf.readBlockPos();

        return packet;

    }

    public static void handle( GenericTankPacketRequest packet, Supplier<NetworkEvent.Context> ctx ) {

        ctx.get().enqueueWork( () -> {

            World world = ctx.get().getSender().world;

            BlockPos pos = packet.pos;

            if ( world.isBlockLoaded( pos ) ) {

                TileEntity tile = world.getTileEntity( pos );

                if ( tile instanceof IHasFluidTank ) {

                    //PacketDistributor.TargetPoint squidPoint = new PacketDistributor.TargetPoint( this.squid.posX, this.squid.posY, this.squid.posZ, 16.0F, this.squid.dimension );

                    Network.channel.send( PacketDistributor.NEAR.with( () -> new PacketDistributor.TargetPoint( tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), 64.0f, DimensionType.OVERWORLD ) ), new GenericTankPacket( tile ) );
                    //Network.channel.send( PacketDistributor.ALL.noArg(), new GenericTankPacket( tile ) );   //TODO: Figure out how to send this to those nearby, instead of all
                }

            }

        } );

        ctx.get().setPacketHandled( true );

    }

    public void toBytes( PacketBuffer buf ) {

        buf.writeBlockPos( this.pos );

    }

}
