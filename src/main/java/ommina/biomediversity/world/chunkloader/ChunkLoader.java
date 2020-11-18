package ommina.biomediversity.world.chunkloader;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ChunkLoader {

    public static void forceSingle( World world, BlockPos pos ) {

        forceSingle( world, new ChunkPos( pos ) );

    }

    public static void forceSingle( World world, ChunkPos pos ) {

        forceChunk( world, pos, true );

    }

    private static void forceChunk( World world, ChunkPos pos, boolean doForceLoad ) {

        if ( pos.x < -1875000 || pos.x > 1875000 || pos.z < -1875000 || pos.z > 1875000 )
            return;

        MinecraftServer server = world.getServer();

        if ( server == null )
            return;

        ServerWorld serverworld = server.getWorld( World.OVERWORLD ); //TODO: Can DimensionManager somehow let us use other dimensions as well?

        serverworld.getChunkProvider().forceChunk( pos, doForceLoad );

        //serverworld.forceChunk( pos.x, pos.z, doForceLoad );

    }

    public static void releaseSingle( World world, BlockPos pos ) {

        releaseSingle( world, new ChunkPos( pos ) );

    }

    public static void releaseSingle( World world, ChunkPos pos ) {

        forceChunk( world, pos, false );

    }

}