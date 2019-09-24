package ommina.biomediversity.network;

import ommina.biomediversity.fluids.BdFluidTank;

public interface ITankBroadcast {

    void doBroadcast();

    BdFluidTank getTank( int index );

}
