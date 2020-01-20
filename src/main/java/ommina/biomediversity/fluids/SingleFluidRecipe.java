package ommina.biomediversity.fluids;

import net.minecraft.fluid.Fluid;

public class SingleFluidRecipe {

    // The Collector has eight output tubes, for eight possible outputs.  It has four input tubes, for n possible inputs (the player must choose which four they like)
    //
    //   That means there is no 1:1 input:output for JEI to display.  As such, we'll have a CollectorInput category, and a CollectorOutput category.  */

    private final Fluid fluid;

    public SingleFluidRecipe( Fluid fluid ) {
        this.fluid = fluid;
    }

    public Fluid getFluid() {
        return fluid;
    }

}
