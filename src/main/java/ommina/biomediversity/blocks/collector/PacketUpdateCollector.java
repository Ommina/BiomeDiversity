package ommina.biomediversity.blocks.collector;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class PacketUpdateCollector {

    public BlockPos tilePos;

    public PacketUpdateCollector() {
    }

    public PacketUpdateCollector( TileEntity tile ) {
        this( (TileEntityCollector) tile );
    }

    public PacketUpdateCollector( TileEntityCollector tile ) {

        this.tilePos = tile.getPos();

    }

    public static PacketUpdateCollector fromBytes( PacketBuffer buf ) {

        PacketUpdateCollector packet = new PacketUpdateCollector();

        packet.tilePos = buf.readBlockPos();

        return packet;

    }

    public void toBytes( PacketBuffer buf ) {

        buf.writeBlockPos( tilePos );

    }

}
