package ommina.biomediversity.fluids;

import net.minecraft.fluid.Fluid;

public class Recipe {

    Fluid fluid;
    int strength;

    public Recipe( Fluid fluid, int strength ) {

        this.fluid = fluid;
        this.strength = strength;

    }

}
