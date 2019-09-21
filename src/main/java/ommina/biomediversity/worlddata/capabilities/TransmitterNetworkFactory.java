package ommina.biomediversity.worlddata.capabilities;

import ommina.biomediversity.worlddata.TransmitterNetwork;

import java.util.concurrent.Callable;

public class TransmitterNetworkFactory implements Callable<ITransmitterNetwork> {

    @Override
    public ITransmitterNetwork call() throws Exception {
        return new TransmitterNetwork();
    }

}



