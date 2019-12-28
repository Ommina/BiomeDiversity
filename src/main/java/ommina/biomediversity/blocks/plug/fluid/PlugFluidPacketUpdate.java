package ommina.biomediversity.blocks.plug.fluid;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class PlugFluidPacketUpdate {

    public int collectorTank;

    public BlockPos tilePos;
    public BlockPos collectorPos;

    public PlugFluidPacketUpdate() {
    }

    public PlugFluidPacketUpdate( TileEntity tile ) {
        this( (TileEntityPlugFluid) tile );
    }

    public PlugFluidPacketUpdate( TileEntityPlugFluid tile ) {

        this.tilePos = tile.getPos();
        this.collectorPos = tile.getCollectorPos();
        this.collectorTank = tile.collectorTank;

    }

    public static PlugFluidPacketUpdate fromBytes( PacketBuffer buf ) {

        PlugFluidPacketUpdate packet = new PlugFluidPacketUpdate();

        packet.tilePos = buf.readBlockPos();
        packet.collectorTank = buf.readInt();

        if ( buf.readBoolean() )
            packet.collectorPos = buf.readBlockPos();

        return packet;

    }

    public void toBytes( PacketBuffer buf ) {

        buf.writeBlockPos( this.tilePos );
        buf.writeInt( this.collectorTank );

        if ( this.collectorPos != null ) {
            buf.writeBoolean( true );
            buf.writeBlockPos( this.collectorPos );
        } else {
            buf.writeBoolean( false );
        }

    }

}
