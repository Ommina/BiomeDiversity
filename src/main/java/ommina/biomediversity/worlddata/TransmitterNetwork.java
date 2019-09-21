package ommina.biomediversity.worlddata;

import ommina.biomediversity.blocks.transmitter.TileEntityTransmitter;
import ommina.biomediversity.worlddata.capabilities.ITransmitterNetwork;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TransmitterNetwork implements ITransmitterNetwork {

    private static final Map<UUID, HashMap<UUID, TransmitterData>> players = new HashMap<UUID, HashMap<UUID, TransmitterData>>();

    private static Map<UUID, TransmitterData> getPlayer( final UUID playerIdentifier ) {

        if ( !containsPlayer( playerIdentifier ) )
            players.put( playerIdentifier, new HashMap<UUID, TransmitterData>() );

        return players.get( playerIdentifier );

    }

    @Override
    public TransmitterData getTransmitter( final UUID playerIdentifier, final UUID pillarIdentifier ) {

        Map<UUID, TransmitterData> playerPillars = getPlayer( playerIdentifier );

        if ( !playerPillars.containsKey( pillarIdentifier ) )
            playerPillars.put( pillarIdentifier, new TransmitterData() );

        return playerPillars.get( pillarIdentifier );

    }

    private static boolean removeTransmitter( final UUID playerIdentifier, final UUID pillarIdentifier ) {

        if ( !containsPillar( playerIdentifier, pillarIdentifier ) )
            return false;

        return (players.get( playerIdentifier ).remove( pillarIdentifier ) != null);

    }

    private static boolean removeTransmitter( final TileEntityTransmitter tile ) {

        return removeTransmitter( tile.getOwner(), tile.getIdentifier() );

    }

    private static boolean containsPlayer( final UUID playerIdentifier ) {

        return players.containsKey( playerIdentifier );
    }

    private static boolean containsPillar( final UUID playerIdentifier, final UUID pillarIdentifier ) {

        if ( !containsPlayer( playerIdentifier ) )
            return false;

        return players.get( playerIdentifier ).containsKey( pillarIdentifier );

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

    /*

    @Override
    public void markDirty( World world ) {

        WorldData.get( world ).markDirty();

    }

    */

}
