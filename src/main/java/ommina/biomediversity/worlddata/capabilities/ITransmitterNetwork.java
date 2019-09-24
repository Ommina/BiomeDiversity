package ommina.biomediversity.worlddata.capabilities;

import ommina.biomediversity.blocks.transmitter.TileEntityTransmitter;
import ommina.biomediversity.worlddata.TransmitterData;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.UUID;

public interface ITransmitterNetwork {

    boolean removeTransmitter( @Nonnull final TileEntityTransmitter tile );

    boolean removeTransmitter( @Nonnull final UUID playerIdentifier, @Nonnull final UUID transmitterIdentifier );

    TransmitterData getTransmitter( @Nonnull final UUID playerIdentifier, @Nonnull final UUID pillarIdentifier );

    Set<UUID> getPlayerList();

    Set<UUID> getTransmitterList( final UUID playerIdentifier );

    boolean isEmpty();

}
