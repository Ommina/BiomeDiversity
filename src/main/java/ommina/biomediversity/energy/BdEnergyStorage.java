package ommina.biomediversity.energy;

import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;
import ommina.biomediversity.BiomeDiversity;

public class BdEnergyStorage extends EnergyStorage {

    public BdEnergyStorage( int capacity ) {
        super( capacity, capacity, capacity, 0 );
    }

    public BdEnergyStorage( int capacity, int maxTransfer ) {
        super( capacity, maxTransfer, maxTransfer, 0 );
    }

    public BdEnergyStorage( int capacity, int maxReceive, int maxExtract ) {
        super( capacity, maxReceive, maxExtract, 0 );
    }

    public BdEnergyStorage( int capacity, int maxReceive, int maxExtract, int energy ) {
        super( capacity, maxReceive, maxExtract, energy );
    }

    public void setStoredEnergy( int energy ) {

        BiomeDiversity.LOGGER.info( "Setting energy to " + energy );

        if ( Thread.currentThread().getThreadGroup() != SidedThreadGroups.SERVER )
            this.energy = energy;

    }

}
