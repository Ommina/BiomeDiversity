package ommina.biomediversity.blocks.collector;

import ommina.biomediversity.config.Config;
import ommina.biomediversity.config.Constants;

import java.util.HashMap;
import java.util.Map;

class Emission {

    private final Map<Integer, Integer> fluidCounts = new HashMap<Integer, Integer>();
    private int powerTotal;
    private int hits;
    private float coolTotal;
    private float warmTotal;

    public Emission() {

    }

    //region Overrides
    @Override
    public String toString() {

        return String.format( "hits: %d, temperatureTotal: %f ", hits, getTemperatureTotal() );

    }
//endregion Overrides

    public void add( int power, float temperature, int fluidHash ) {

        add( temperature );
        powerTotal += power;
        hits++;

        Integer hash = fluidHash;

        if ( fluidCounts.containsKey( hash ) )
            fluidCounts.put( hash, fluidCounts.get( hash ) + 1 );
        else
            fluidCounts.put( hash, 1 );

    }

    public void add( float temperature ) {

        coolTotal += temperature < 0 ? temperature : 0;
        warmTotal += temperature > 0 ? temperature : 0;

    }

    public FluidProduct getFluidCreated() {

        int power = getEnergy();

        if ( power < Constants.COLLECTOR_MINIMUM_ENERGY_TO_PRODUCE_FLUID )
            return FluidProduct.EMPTY;

        float base = power * Constants.COLLECTOR_ENERGY_TO_FLUID_MULTIPLIER;
        float equib = isOutOfEquilibrium() ? 0f : 1 - getTemperatureTotal() / Constants.COLLECTOR_EQUILIBRIUM_THRESHOLD;

        FluidProduct product = new FluidProduct();

        product
             .setWarm( (int) (warmTotal / getAbsoluteTemperatureTotal() * base + (base * (Constants.COLLECTOR_EQUILIBRIUM_BONUS_BUCKET * equib))) )
             .setCool( (int) (-coolTotal / getAbsoluteTemperatureTotal() * base + (base * (Constants.COLLECTOR_EQUILIBRIUM_BONUS_BUCKET * equib))) )
             .setByproduct( (int) (power * Constants.COLLECTOR_ENERGY_TO_BYPRODUCT_MULTIPLIER) );

        return product;

        //return new Pair( (int) (warmTotal / getAbsoluteTemperatureTotal() * base + (base * (Constants.COLLECTOR_EQUILIBRIUM_BONUS_BUCKET * equib))), (int) (-coolTotal / getAbsoluteTemperatureTotal() * base +
        //     (base * (Constants.COLLECTOR_EQUILIBRIUM_BONUS_BUCKET * equib))) );

    }

    public int getHits() {

        return hits;
    }

    public int getEnergy() {

        if ( hits == 0 )
            return 0;

        hits = Math.min( hits, Config.powerMaxBiomeCount.get() );

        double powerPrePenalty = (float) ((powerTotal * Math.pow( Config.powerBiomeDiversity.get(), hits ) + Config.powerBiomeAdjustment.get()) + (powerTotal * Math.pow( Constants.FLUID_DIVERSITY, hits ) + Constants.FLUID_ADJUSTMENT));
        int maxFluidCount = 0;

        for ( int n : fluidCounts.values() )
            maxFluidCount = Math.max( maxFluidCount, n );

        double powerPostPenalty = maxFluidCount <= Config.powerMaxReceiversPerFluid.get() ? powerPrePenalty : powerPrePenalty - (powerPrePenalty * ((maxFluidCount - Config.powerMaxReceiversPerFluid.get()) * Config.powerRepeatedFluidPenalty.get()));

        return (int) (powerPostPenalty * Config.powerFinalMultiplier.get() * Constants.CLUSTER_FLUID_CONSUMPTION);

    }

    public float getTemperatureTotal() {

        return coolTotal + warmTotal;
    }

    public boolean isOutOfEquilibrium() {

        return (hits == 1 || getTemperatureTotal() <= -Constants.COLLECTOR_EQUILIBRIUM_THRESHOLD || getTemperatureTotal() >= Constants.COLLECTOR_EQUILIBRIUM_THRESHOLD);
    }

    private float getAbsoluteTemperatureTotal() {

        return 0 - coolTotal + warmTotal;
    }

}
