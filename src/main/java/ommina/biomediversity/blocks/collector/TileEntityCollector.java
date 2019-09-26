package ommina.biomediversity.blocks.collector;

import net.minecraft.tileentity.TileEntity;
import ommina.biomediversity.blocks.ModTileEntities;

public class TileEntityCollector extends TileEntity {

    public TileEntityCollector() {
        super( ModTileEntities.COLLECTOR );
    }

    public boolean isCollectorTurnedOff() {
        return false; //TODO: Collector should be disabled by redstone
    }

}
