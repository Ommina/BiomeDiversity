package ommina.biomediversity.blocks.collector;

public class FluidProduct {

    public static final FluidProduct EMPTY = new FluidProduct();

    private int warm;
    private int cool;
    private int byproduct;

    public int getWarm() {
        return warm;
    }

    public FluidProduct setWarm( int warm ) {

        this.warm = warm;
        return this;

    }

    public int getCool() {
        return cool;
    }

    public FluidProduct setCool( int cool ) {

        this.cool = cool;
        return this;

    }

    public int getByproduct() {
        return byproduct;
    }

    public FluidProduct setByproduct( int byproduct ) {

        this.byproduct = byproduct;
        return this;

    }

}
