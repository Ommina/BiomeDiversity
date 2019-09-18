
package ommina.biomediversity.worlddata;


import net.minecraft.fluid.Fluid;
import ommina.biomediversity.config.Config;

import java.util.UUID;

public class TransmitterData {

    private int amount;

    public Fluid fluid;
    public float rainfall;
    public float temperature;
    public int biomeId;
    public UUID receiver;

    private void adjustAmount( int delta ) {

        this.amount = Math.min( Math.max( 0, this.amount + delta ), Config.transmitterCapacity.get() );

        if ( this.amount <= 0 )
            this.fluid = null;

    }

    public void drain( int amount ) {

        adjustAmount( amount * -1 );

    }

    public void fill( int amount ) {

        adjustAmount( amount );

    }

    public void setAmount( int amount ) {

        this.amount = amount;

    }

    public int getAmount() {

        return this.amount;

    }

}
