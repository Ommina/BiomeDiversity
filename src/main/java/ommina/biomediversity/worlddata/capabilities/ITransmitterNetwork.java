package ommina.biomediversity.worlddata.capabilities;

import ommina.biomediversity.blocks.transmitter.TileEntityTransmitter;
import ommina.biomediversity.worlddata.TransmitterData;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.UUID;

public interface ITransmitterNetwork {

    Set<UUID> getPlayerList();

    TransmitterData getTransmitter( @Nonnull final UUID playerIdentifier, @Nonnull final UUID transmitterIdentifier );

    Set<UUID> getTransmitterList( final UUID playerIdentifier );

    boolean isEmpty();

    boolean removeTransmitter( @Nonnull final TileEntityTransmitter tile );

    boolean removeTransmitter( @Nonnull final UUID playerIdentifier, @Nonnull final UUID transmitterIdentifier );

}
