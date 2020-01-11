package ommina.biomediversity.fluids;

import net.minecraft.fluid.Fluid;

public class Recipe {

    private final Fluid fluid;
    private final int strength;

    public Recipe( Fluid fluid, int strength ) {

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
