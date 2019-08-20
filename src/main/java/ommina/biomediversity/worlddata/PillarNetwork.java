
package ommina.biomediversity.worlddata;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.world.World;
import ommina.biomediversity.Biomediversity;
import ommina.biomediversity.block.pillar.TileEntityPillar;

public class PillarNetwork {

    public static final Map<UUID, HashMap<UUID, PillarData>> players = new HashMap<UUID, HashMap<UUID, PillarData>>();

    public static Map<UUID, PillarData> getPlayer( @Nonnull final UUID playerIdentifier ) {

        if ( playerIdentifier == null ) {
            Biomediversity.logger.warn( "!* playerIdentifier is null adding to PillarNetowrk players" );
            for ( StackTraceElement ste : Thread.currentThread().getStackTrace() )
                Biomediversity.logger.warn( ste );
        }

        if ( !containsPlayer( playerIdentifier ) )
            players.put( playerIdentifier, new HashMap<UUID, PillarData>() );

        return players.get( playerIdentifier );

    }

    @Nonnull
    public static PillarData getPillar( @Nonnull final UUID playerIdentifier, @Nonnull final UUID pillarIdentifier ) {

        Map<UUID, PillarData> playerPillars = getPlayer( playerIdentifier );

        if ( !playerPillars.containsKey( pillarIdentifier ) )
            playerPillars.put( pillarIdentifier, new PillarData() );

        return playerPillars.get( pillarIdentifier );

    }

    public static boolean removePillar( final UUID playerIdentifier, final UUID pillarIdentifier ) {

        if ( !containsPillar( playerIdentifier, pillarIdentifier ) )
            return false;

        return (players.get( playerIdentifier ).remove( pillarIdentifier ) != null);

    }

    public static boolean removePillar( final TileEntityPillar tile ) {

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

    public static Set<UUID> getPillarList( final UUID playerIdentifier ) {

        return getPlayer( playerIdentifier ).keySet();
    }

    public static boolean isEmpty() {

        return players.isEmpty();
    }

    public static void markDirty( World world ) {

        WorldData.get( world ).markDirty();

    }

}
