package ommina.biomediversity.blocks.collector;

import ommina.biomediversity.energy.BdEnergyStorage;
import ommina.biomediversity.network.ITankBroadcast;

public class BroadcastHelper extends ommina.biomediversity.network.BroadcastHelper {

    private final BdEnergyStorage energyStorage;
    private int energyStored;

    public BroadcastHelper( int tankCount, int minimumDelta, ITankBroadcast tanker, BdEnergyStorage energyStorage ) {

        super( tankCount, minimumDelta, tanker );

        this.energyStorage = energyStorage;

    }

    //region Overrides
    @Override
    public boolean needsBroadcast() {

        return super.needsBroadcast() || Math.abs( energyStored - energyStorage.getEnergyStored() ) > 1000;

    }

    @Override
    public void reset() {
        super.reset();

        energyStored = energyStorage.getEnergyStored();

    }
//endregion Overrides

}
