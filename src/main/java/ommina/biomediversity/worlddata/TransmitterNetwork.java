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
    public boolean removeTransmitter( final UUID playerIdentifier, final UUID pillarIdentifier ) {

        if ( !containsPillar( playerIdentifier, pillarIdentifier ) )
            return false;

        return (players.get( playerIdentifier ).remove( pillarIdentifier ) != null);

    }

    @Override
    public TransmitterData getTransmitter( final UUID playerIdentifier, final UUID pillarIdentifier ) {

        Map<UUID, TransmitterData> playerPillars = getPlayer( playerIdentifier );

        if ( !playerPillars.containsKey( pillarIdentifier ) )
            playerPillars.put( pillarIdentifier, new TransmitterData() );

        return playerPillars.get( pillarIdentifier );

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

    private static boolean containsPillar( final UUID playerIdentifier, final UUID pillarIdentifier ) {

        if ( !containsPlayer( playerIdentifier ) )
            return false;

        return players.get( playerIdentifier ).containsKey( pillarIdentifier );

    }

    private static boolean containsPlayer( final UUID playerIdentifier ) {

        return players.containsKey( playerIdentifier );
    }

    /*

    @Override
    public void markDirty( World world ) {

        WorldData.get( world ).markDirty();

    }

    */

}
