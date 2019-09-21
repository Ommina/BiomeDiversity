package ommina.biomediversity.worlddata.capabilities;

import ommina.biomediversity.worlddata.TransmitterData;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.UUID;

public interface ITransmitterNetwork {

    TransmitterData getTransmitter( @Nonnull final UUID playerIdentifier, @Nonnull final UUID pillarIdentifier );

    Set<UUID> getPlayerList();

    Set<UUID> getTransmitterList( final UUID playerIdentifier );

    boolean isEmpty();

//    void markDirty( World world );


}
