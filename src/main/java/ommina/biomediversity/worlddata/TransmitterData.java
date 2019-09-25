package ommina.biomediversity.worlddata;


import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import ommina.biomediversity.config.Config;

import java.util.UUID;

public class TransmitterData {

    public Fluid fluid;
    public float rainfall;
    public float temperature;
    public ResourceLocation biomeId;
    public UUID receiver;
    private int amount;

    public void drain( int amount ) {

        adjustAmount( amount * -1 );

    }

    private void adjustAmount( int delta ) {

        this.amount = Math.min( Math.max( 0, this.amount + delta ), Config.transmitterCapacity.get() );

        if ( this.amount <= 0 )
            this.fluid = null;

    }

    public void fill( int amount ) {

        adjustAmount( amount );

    }

    public boolean isValid() {

        return (biomeId != null); //TODO: Are there reasonable sanity checks for temperature / rainfall that we can include here?

    }

    public int getAmount() {

        return this.amount;

    }

    public void setAmount( int amount ) {

        this.amount = amount;

    }

}
