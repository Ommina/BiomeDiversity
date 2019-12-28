package ommina.biomediversity.blocks.plug.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import ommina.biomediversity.blocks.collector.Tubes;
import ommina.biomediversity.blocks.plug.PlugBase;

public class PlugFluidByproduct extends PlugBase implements ITube {

    private final Tubes tube = Tubes.Byproduct;

    public PlugFluidByproduct() {
        super();
    }

    //region Overrides
    @Override
    public TileEntity createTileEntity( BlockState state, IBlockReader world ) {
        return new TileEntityPlugFluid( tube );
    }

    @Override
    public Tubes getTube() {
        return tube;
    }
//endregion Overrides

}
