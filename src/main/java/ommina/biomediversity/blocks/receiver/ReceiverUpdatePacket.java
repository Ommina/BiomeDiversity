package ommina.biomediversity.blocks.receiver;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import ommina.biomediversity.network.ITankBroadcast;

public class ReceiverUpdatePacket {

    public FluidStack fluid;
    public BlockPos tilePos;
    public BlockPos collectorPos;

    public ReceiverUpdatePacket() {
    }

    public ReceiverUpdatePacket( TileEntityReceiver tile ) {

        this.fluid = ((ITankBroadcast) tile).getTank( 0 ).getFluid();
        this.tilePos = tile.getPos();

        if ( tile.getCollector() != null )
            this.collectorPos = tile.getCollector().getPos();

    }

    public static ReceiverUpdatePacket fromBytes( PacketBuffer buf ) {

        ReceiverUpdatePacket packet = new ReceiverUpdatePacket();

        packet.tilePos = buf.readBlockPos();

        if ( buf.readBoolean() )
            packet.collectorPos = buf.readBlockPos();

        packet.fluid = FluidStack.readFromPacket( buf );

        return packet;

    }

    public void toBytes( PacketBuffer buf ) {

        buf.writeBlockPos( this.tilePos );

        if ( this.collectorPos != null ) {
            buf.writeBoolean( true );
            buf.writeBlockPos( this.collectorPos );
        } else {
            buf.writeBoolean( false );
        }

        this.fluid.writeToPacket( buf );

    }

}
