
package ommina.biomediversity.chunkloader;

import java.util.List;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public class ChunkManagerCallback implements LoadingCallback {

    @Override
    public void ticketsLoaded( List<Ticket> tickets, World world ) {

        for ( Ticket ticket : tickets ) {
            ForgeChunkManager.releaseTicket( ticket );

        }
    }

}
