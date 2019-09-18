package ommina.biomediversity.worlddata;

import net.minecraft.world.World;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.transmitter.TileEntityTransmitter;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TransmitterNetwork {

    public static final Map<UUID, HashMap<UUID, TransmitterData>> players = new HashMap<UUID, HashMap<UUID, TransmitterData>>();

    public static Map<UUID, TransmitterData> getPlayer( @Nonnull final UUID playerIdentifier ) {

        if ( playerIdentifier == null ) {
            BiomeDiversity.LOGGER.warn( "!* playerIdentifier is null adding to PillarNetowrk players" );
            for ( StackTraceElement ste : Thread.currentThread().getStackTrace() )
                BiomeDiversity.LOGGER.warn( ste );
        }

        if ( !containsPlayer( playerIdentifier ) )
            players.put( playerIdentifier, new HashMap<UUID, TransmitterData>() );

        return players.get( playerIdentifier );

    }

    @Nonnull
    public static TransmitterData getPillar( @Nonnull final UUID playerIdentifier, @Nonnull final UUID pillarIdentifier ) {

        Map<UUID, TransmitterData> playerPillars = getPlayer( playerIdentifier );

        if ( !playerPillars.containsKey( pillarIdentifier ) )
            playerPillars.put( pillarIdentifier, new TransmitterData() );

        return playerPillars.get( pillarIdentifier );

    }

    public static boolean removePillar( final UUID playerIdentifier, final UUID pillarIdentifier ) {

        if ( !containsPillar( playerIdentifier, pillarIdentifier ) )
            return false;

        return (players.get( playerIdentifier ).remove( pillarIdentifier ) != null);

    }

    public static boolean removePillar( final TileEntityTransmitter tile ) {

        return removePillar( tile.getOwner(), tile.getIdentifier() );

    }

    public static boolean containsPlayer( final UUID playerIdentifier ) {

        return players.containsKey( playerIdentifier );
    }

    public static boolean containsPillar( final UUID playerIdentifier, final UUID pillarIdentifier ) {

        if ( !containsPlayer( playerIdentifier ) )
            return false;

        return players.get( playerIdentifier ).containsKey( pillarIdentifier );

    }

    public static Set<UUID> getPlayerList() {

        return players.keySet();
    }

    public static Set<UUID> getTransmitterList( final UUID playerIdentifier ) {

        return getPlayer( playerIdentifier ).keySet();
    }

    public static boolean isEmpty() {

        return players.isEmpty();
    }

    public static void markDirty( World world ) {

        WorldData.get( world ).markDirty();

    }

}
