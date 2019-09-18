package ommina.biomediversity.blocks.transmitter;

import net.minecraft.nbt.CompoundNBT;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.blocks.tile.TileEntityAssociation;

public class TileEntityTransmitter extends TileEntityAssociation {

    public static final int LINKING_SOURCE_TRANSMITTER = 1;

    public TileEntityTransmitter() {
        super( ModTileEntities.TRANSMITTER );
    }

    @Override
    public void read( CompoundNBT compound ) {
        super.read( compound );
    }

    @Override
    public CompoundNBT write( CompoundNBT compound ) {
        return super.write( compound );
    }

}
