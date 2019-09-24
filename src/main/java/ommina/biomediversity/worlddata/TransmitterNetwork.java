package ommina.biomediversity.worlddata;

import ommina.biomediversity.blocks.transmitter.TileEntityTransmitter;
import ommina.biomediversity.worlddata.capabilities.ITransmitterNetwork;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TransmitterNetwork implements ITransmitterNetwork {

    private static final Map<UUID, HashMap<UUID, TransmitterData>> players = new HashMap<UUID, HashMap<UUID, TransmitterData>>();

    @Override
    public boolean removeTransmitter( final TileEntityTransmitter tile ) {

        return removeTransmitter( tile.getOwner(), tile.getIdentifier() );

    }

    @Override
    public boolean removeTransmitter( final UUID playerIdentifier, final UUID transmitterIdentifier ) {

        if ( !containsTransmitter( playerIdentifier, transmitterIdentifier ) )
            return false;

        return (players.get( playerIdentifier ).remove( transmitterIdentifier ) != null);

    }

    @Override
    public TransmitterData getTransmitter( final UUID playerIdentifier, final UUID transmitterIdentifier ) {

        Map<UUID, TransmitterData> playerTransmitters = getPlayer( playerIdentifier );

        if ( !playerTransmitters.containsKey( transmitterIdentifier ) )
            playerTransmitters.put( transmitterIdentifier, new TransmitterData() );

        return playerTransmitters.get( transmitterIdentifier );

    }

    private static Map<UUID, TransmitterData> getPlayer( final UUID playerIdentifier ) {

        if ( !containsPlayer( playerIdentifier ) )
            players.put( playerIdentifier, new HashMap<UUID, TransmitterData>() );

        return players.get( playerIdentifier );

    }

    @Override
    public Set<UUID> getPlayerList() {

        return players.keySet();
    }

    @Override
    public Set<UUID> getTransmitterList( final UUID playerIdentifier ) {

        return getPlayer( playerIdentifier ).keySet();
    }

    @Override
    public boolean isEmpty() {

        return players.isEmpty();
    }

    private static boolean containsTransmitter( final UUID playerIdentifier, final UUID transmitterIdentifier ) {

        if ( !containsPlayer( playerIdentifier ) )
            return false;

        return players.get( playerIdentifier ).containsKey( transmitterIdentifier );

    }

    private static boolean containsPlayer( final UUID playerIdentifier ) {

        return players.containsKey( playerIdentifier );
    }

}
