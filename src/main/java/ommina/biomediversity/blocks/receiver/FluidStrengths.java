package ommina.biomediversity.blocks.receiver;

import net.minecraft.fluid.Fluid;
import ommina.biomediversity.BiomeDiversity;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FluidStrengths {

    private static final Map<Integer, FluidStrength> fluids = new HashMap<Integer, FluidStrength>();

    public static class FluidStrength {

        final int strength;
        private final Fluid fluid;
        private final int hash;

        public FluidStrength( @Nonnull Fluid fluid, int strength ) {

            this.fluid = fluid;
            this.hash = fluid.hashCode();
            this.strength = strength;

        }

        public Fluid getFluid() {

            return fluid;
        }

        public int getHash() {

            return hash;
        }

    }

    public static void add( Fluid fluid, int strength ) {

        int hash = fluid.hashCode();

        if ( fluids.containsKey( hash ) ) {
            BiomeDiversity.LOGGER.warn( String.format( "Attempted to add the same fluid twice: %s", fluid.getAttributes().toString() ) );
            return;
        }

        BiomeDiversity.LOGGER.info( String.format( "Adding fluid %s with strength %d and hash %d", fluid.getAttributes(), strength, hash ) );

        fluids.put( hash, new FluidStrength( fluid, strength ) );

    }

    public static void clear() {
        fluids.clear();
    }

    public static int getStrength( Fluid fluid ) {

        return getStrength( fluid.hashCode() );

    }

    public static int getStrength( int hashCode ) {

        if ( !fluids.containsKey( hashCode ) ) {
            //BiomeDiversity.LOGGER.info( "FluidStrength hash not found.  Returning 0" );
            return 0;
        }

        return fluids.get( hashCode ).strength;

    }

    public static Collection<FluidStrength> getAll() {

        return fluids.values();

    }

    public static boolean contains( int hashCode ) {

        return fluids.containsKey( hashCode );

    }

}
