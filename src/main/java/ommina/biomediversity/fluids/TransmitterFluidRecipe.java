package ommina.biomediversity.fluids;

import net.minecraft.fluid.Fluid;

public class TransmitterFluidRecipe {

    /* Which seems like an odd recipe.  But in the end, the input is 'fluid', and the output is 'RF', which is calculated from strength */

    private final Fluid fluid;
    private final int strength;

    public TransmitterFluidRecipe( Fluid fluid, int strength ) {

        this.fluid = fluid;
        this.strength = strength;

    }

    public Fluid getFluid() {
        return fluid;
    }

    public int getStrength() {
        return strength;
    }

}
