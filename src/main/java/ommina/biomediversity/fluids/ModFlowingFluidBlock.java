package ommina.biomediversity.fluids;

import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;

import javax.annotation.Nonnull;

public class ModFlowingFluidBlock extends FlowingFluidBlock {

    public ModFlowingFluidBlock( @Nonnull FlowingFluid flowingFluid, Properties properties ) {

        super( flowingFluid, properties );
    }

}
