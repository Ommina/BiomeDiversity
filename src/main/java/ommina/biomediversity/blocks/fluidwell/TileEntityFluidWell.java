package ommina.biomediversity.blocks.fluidwell;

import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.world.gen.FluidWellGeneration;

/*

OK, so about this.  This is worldgen.  This is not the place for it.  It will, ideally, eventually be replaced with a Carver, but working through carvers will have to be done by somebody
with more skill in sifting through obfuscated code than I.  I gave up.

For now, this is a TE that is created and placed during WorldGen as a Feature.  Every five seconds, it checks if the chunks around it are loaded.  If they all are, it creates the fluid sphere,
destroys itself, and moves on.

Peformance impact will be close to, if not actually, zero.  The only time the tile will live long enough to do its already super-cheap tick is if it is _right_ on the edge of an unloaded chunk.
Since fluid spheres are supposed to be rare in the first place, this isn't exceptionally likely.  Greater than zero, sure, but not much.

*/

public class TileEntityFluidWell extends TileEntity implements ITickableTileEntity {

    private static final int DELAY = 20 * 5; // 5.00s

    private int delay = DELAY;

    public TileEntityFluidWell() {
        super( ModTileEntities.FLUID_WELL );
    }

    //region Overrides
    @Override
    public void tick() {

        if ( world.isRemote )
            return;

        if ( delay > 0 ) {
            delay--;
            return;
        }

        delay = DELAY;

        if ( !hasWorld() || !areSurroundingChunksLoaded( getWorld(), pos ) )
            return;

        BiomeDiversity.LOGGER.warn( "Splash!" );

        FluidWellGeneration.createSphere( world, pos  );

        world.setBlockState( pos, Blocks.STONE.getDefaultState() );

        BiomeDiversity.LOGGER.warn( "Still here!" );

    }
//endregion Overrides

    private static boolean areSurroundingChunksLoaded( World world, BlockPos pos ) {

        for ( Direction direction : Direction.Plane.HORIZONTAL ) {
            BlockPos p2 = pos.offset( direction, 16 );
            BiomeDiversity.LOGGER.warn( "I'm at " + pos.toString() + " and am checking " + p2.toString() );
            if ( !world.isBlockLoaded( pos.offset( direction, 16 ) ) )
                return false;
        }

        return true;

    }

}
