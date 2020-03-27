package ommina.biomediversity.network;

import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class GenericTankUpdatePacket {

    private FluidStack fluid;
    private BlockPos pos;

    public GenericTankUpdatePacket() {
    }

    public GenericTankUpdatePacket( Fluid fluid, int amount ) {

        this.fluid = new FluidStack( fluid, amount );

    }

    public GenericTankUpdatePacket( TileEntity tile ) {

        if ( !(tile instanceof ITankBroadcast) )
            throw new RuntimeException( "Tried to create a generic tank packet for a tile without a tank" );

        fluid = ((ITankBroadcast) tile).getTank( 0 ).getFluid();
        pos = tile.getPos();

    }

    public static GenericTankUpdatePacket fromBytes( PacketBuffer buf ) {

        GenericTankUpdatePacket packet = new GenericTankUpdatePacket();

        packet.pos = buf.readBlockPos();
        packet.fluid = FluidStack.readFromPacket( buf );

        return packet;

    }

    public static void handle( GenericTankUpdatePacket packet, Supplier<NetworkEvent.Context> ctx ) {

        ctx.get().enqueueWork( () -> {

            Optional<World> world = LogicalSidedProvider.CLIENTWORLD.get( ctx.get().getDirection().getReceptionSide() );

            if ( world.get().isBlockLoaded( packet.pos ) ) {
                TileEntity tile = world.get().getTileEntity( packet.pos );

                if ( tile instanceof ITankBroadcast )
                    ((ITankBroadcast) tile).getTank( 0 ).setFluid( packet.fluid );

            }

        } );

        ctx.get().setPacketHandled( true );

    }

    public void toBytes( PacketBuffer buf ) {

        buf.writeBlockPos( this.pos );
        this.fluid.writeToPacket( buf );

    }

}
