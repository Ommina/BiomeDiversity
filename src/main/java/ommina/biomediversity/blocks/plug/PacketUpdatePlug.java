package ommina.biomediversity.blocks.plug;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class PacketUpdatePlug {

    public BlockPos tilePos;
    public BlockPos collectorPos;

    public PacketUpdatePlug() {
    }

    public PacketUpdatePlug( TileEntity tile ) {
        this( (TileEntityPlugBase) tile );
    }

    public PacketUpdatePlug( TileEntityPlugBase tile ) {

        this.tilePos = tile.getPos();
        this.collectorPos = tile.getCollectorPos();

    }

    public static PacketUpdatePlug fromBytes( PacketBuffer buf ) {

        PacketUpdatePlug packet = new PacketUpdatePlug();

        packet.tilePos = buf.readBlockPos();

        if ( buf.readBoolean() )
            packet.collectorPos = buf.readBlockPos();

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

    }

}
