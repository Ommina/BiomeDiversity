
package ommina.biomediversity.chunkloader;

public class ChunkLoader {

    /*

    private static Map<ChunkPos, Ticket> ticketList = new HashMap<ChunkPos, Ticket>();

    public static Ticket forceSingleChunk( World world, BlockPos pos ) {

        return forceSingleChunk( world, world.getChunkAt( pos ).getPos() );

    }

    public static Ticket forceSingleChunk( World world, ChunkPos pos ) {

        if ( ticketList.containsKey( pos ) )
            return ticketList.get( pos );

        Ticket chunkloadTicket = ForgeChunkManager.requestTicket( Biomediversity.instance, world, Type.NORMAL );

        ForgeChunkManager.forceChunk( chunkloadTicket, pos );

        ticketList.put( pos, chunkloadTicket );

        return chunkloadTicket;

    }

    public static Ticket forceNineChunk( World world, ChunkPos centrePos ) {

        if ( ticketList.containsKey( centrePos ) )
            return ticketList.get( centrePos );

        Ticket chunkloadTicket = ForgeChunkManager.requestTicket( BiomeDiversity.instance, world, Type.NORMAL );

        for ( int i = centrePos.x - 1; i <= centrePos.x + 1; i++ )
            for ( int j = centrePos.z - 1; j <= centrePos.z + 1; j++ )
                ForgeChunkManager.forceChunk( chunkloadTicket, world.getChunkFromChunkCoords( i, j ).getPos() );

        ticketList.put( centrePos, chunkloadTicket );

        return chunkloadTicket;

    }

    public static void releaseSingleChunk( ChunkPos pos ) {

        if ( !ticketList.containsKey( pos ) )
            return;

        Ticket chunkloadTicket = ticketList.get( pos );

        if ( chunkloadTicket != null )
            ForgeChunkManager.releaseTicket( chunkloadTicket );

        ticketList.remove( pos );

    }

    public static void releaseSingleChunk( World world, BlockPos pos ) {

        releaseSingleChunk( world.getChunkAt( pos ).getPos() );
    }

    */

}