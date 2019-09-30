package ommina.biomediversity.blocks.collector;

import net.minecraft.tileentity.TileEntity;
import ommina.biomediversity.blocks.ModTileEntities;

public class TileEntityCollector extends TileEntity implements IClusterComponent {

    public TileEntityCollector() {
        super( ModTileEntities.COLLECTOR );
    }

    @Override
    public TileEntityCollector getCollector() {
        return this;
    }

    @Override
    public boolean isClusterComponentConnected() {
        return true;
    }

    public boolean isCollectorTurnedOff() {
        return false; //TODO: Collector should be disabled by redstone
    }

}
