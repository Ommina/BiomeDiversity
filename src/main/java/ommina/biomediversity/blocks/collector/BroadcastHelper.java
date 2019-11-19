package ommina.biomediversity.blocks.collector;

import ommina.biomediversity.energy.BdEnergyStorage;
import ommina.biomediversity.network.ITankBroadcast;

public class BroadcastHelper extends ommina.biomediversity.network.BroadcastHelper {

    private final BdEnergyStorage energyStorage;
    private final int minimumEnergyDelta;

    private int energyStored;

    public BroadcastHelper( int tankCount, int minimumTankDelta, ITankBroadcast tanker, BdEnergyStorage energyStorage, int minimumEnergyDelta ) {
        super( tankCount, minimumTankDelta, tanker );

        this.energyStorage = energyStorage;
        this.minimumEnergyDelta = minimumEnergyDelta;

    }

    //region Overrides
    @Override
    public boolean needsBroadcast() {

        return super.needsBroadcast() || Math.abs( energyStored - energyStorage.getEnergyStored() ) >= minimumEnergyDelta;

    }

    @Override
    public void reset() {
        super.reset();

        energyStored = energyStorage.getEnergyStored();

    }
//endregion Overrides

}
