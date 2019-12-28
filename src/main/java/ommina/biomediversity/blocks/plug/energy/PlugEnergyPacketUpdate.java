package ommina.biomediversity.blocks.plug.energy;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import ommina.biomediversity.blocks.plug.TileEntityPlugBase;

public class PlugEnergyPacketUpdate {

    public BlockPos tilePos;
    public BlockPos collectorPos;

    public PlugEnergyPacketUpdate() {
    }

    public PlugEnergyPacketUpdate( TileEntity tile ) {
        this( (TileEntityPlugBase) tile );
    }

    public PlugEnergyPacketUpdate( TileEntityPlugBase tile ) {

        this.tilePos = tile.getPos();
        this.collectorPos = tile.getCollectorPos();

    }

    public static PlugEnergyPacketUpdate fromBytes( PacketBuffer buf ) {

        PlugEnergyPacketUpdate packet = new PlugEnergyPacketUpdate();

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
