package ommina.biomediversity.blocks.receiver;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import ommina.biomediversity.network.ITankBroadcast;

public class PacketUpdateReceiver {

    public FluidStack fluid;
    public BlockPos tilePos;
    public BlockPos collectorPos;
    public String biomeRegistryName;
    public float temperature;

    public PacketUpdateReceiver() {
    }

    public PacketUpdateReceiver( TileEntity tile ) {
        this( (TileEntityReceiver) tile );
    }

    public PacketUpdateReceiver( TileEntityReceiver tile ) {

        this.fluid = ((ITankBroadcast) tile).getTank( 0 ).getFluid();
        this.tilePos = tile.getPos();
        this.temperature = tile.getTemperature();
        this.biomeRegistryName = tile.getBiomeRegistryName();

        if ( tile.getCollector() != null )
            this.collectorPos = tile.getCollector().getPos();

    }

    public static PacketUpdateReceiver fromBytes( PacketBuffer buf ) {

        PacketUpdateReceiver packet = new PacketUpdateReceiver();

        packet.tilePos = buf.readBlockPos();
        packet.temperature = buf.readFloat();
        packet.biomeRegistryName = buf.readString();

        if ( buf.readBoolean() )
            packet.collectorPos = buf.readBlockPos();

        packet.fluid = FluidStack.readFromPacket( buf );

        return packet;

    }

    public void toBytes( PacketBuffer buf ) {

        buf.writeBlockPos( this.tilePos );
        buf.writeFloat( this.temperature );
        buf.writeString( this.biomeRegistryName );

        if ( this.collectorPos != null ) {
            buf.writeBoolean( true );
            buf.writeBlockPos( this.collectorPos );
        } else {
            buf.writeBoolean( false );
        }

        this.fluid.writeToPacket( buf );

    }

}
