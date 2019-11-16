package ommina.biomediversity.energy;

import net.minecraftforge.energy.EnergyStorage;
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

    public void setEnergyStored( int energy ) {

        //BiomeDiversity.LOGGER.info( "Setting energy to " + energy );

        this.energy = energy;

    }

    public int receiveEnergyInternal( int maxReceive, boolean simulate ) { // Ignores the "maxReceive" setting, so the owning-tile can add to the storage, but a connecting tile can not

        int energyReceived = Math.min( capacity - energy, maxReceive );

        if ( !simulate )
            energy += energyReceived;

        return energyReceived;

    }

}
