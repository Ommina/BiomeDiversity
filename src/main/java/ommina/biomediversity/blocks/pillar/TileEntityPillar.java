package ommina.biomediversity.blocks.pillar;

import net.minecraft.nbt.CompoundNBT;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.blocks.tile.TileEntityAssociation;

public class TileEntityPillar extends TileEntityAssociation {

    public TileEntityPillar() {
        super( ModTileEntities.PILLAR, 1000 );
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
