package ommina.biomediversity.blocks.plug.energy;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import ommina.biomediversity.blocks.plug.PlugBase;

public class PlugEnergy extends PlugBase {

    public PlugEnergy() {
        super();
    }

//region Overrides
    @Override
    public TileEntity createTileEntity( BlockState state, IBlockReader world ) {
        return new TileEntityPlugEnergy();
    }
//endregion Overrides

}
