package ommina.biomediversity.world.chunkloader;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

import java.util.UUID;

public class ChunkLoader {

    public static UUID forceSingle( World world, BlockPos pos ) {

        return forceSingle( world, new ChunkPos( pos ) );

    }

    public static UUID forceSingle( World world, ChunkPos pos ) {

        return forceChunk( world, pos, true );

    }

    private static UUID forceChunk( World world, ChunkPos pos, boolean doForceLoad ) {

        if ( pos.x < -1875000 || pos.x > 1875000 || pos.z < -1875000 || pos.z > 1875000 )
            return null;

        MinecraftServer server = world.getServer();

        if ( server == null )
            return null;

        ServerWorld serverworld = server.getWorld( DimensionType.OVERWORLD ); //TODO: Can DimensionManager somehow let us use other dimensions as well?

        serverworld.forceChunk( pos.x, pos.z, doForceLoad );

        return UUID.randomUUID();

    }

    public static boolean releaseSingle( World world, BlockPos pos ) {

        return releaseSingle( world, new ChunkPos( pos ) );

    }

    public static boolean releaseSingle( World world, ChunkPos pos ) {

        return (forceChunk( world, pos, false ) != null);

    }

}