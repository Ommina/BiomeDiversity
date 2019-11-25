package ommina.biomediversity.blocks.plug.energy;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import ommina.biomediversity.blocks.collector.TileEntityCollector;
import ommina.biomediversity.blocks.plug.PlugBase;
import ommina.biomediversity.blocks.plug.TileEntityPlug;

public class PlugEnergy extends PlugBase {

    public PlugEnergy() {
        super();
    }

    //region Overrides
    @Override
    public TileEntity createTileEntity( BlockState state, IBlockReader world ) {
        return new TileEntityPlug();
    }

    @Override
    public boolean onBlockActivated( BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult ) {

        if ( world.isRemote )
            return true;

        TileEntityPlug tile = (TileEntityPlug) world.getTileEntity( pos );

        if ( tile == null )
            return super.onBlockActivated( blockState, world, pos, player, hand, rayTraceResult );

        BlockPos collectorPos = tile.getCollectorPos();

        if ( collectorPos != null && world.getTileEntity( collectorPos ) instanceof TileEntityCollector )
            ((TileEntityCollector) world.getTileEntity( collectorPos )).forceBroadcast();

        ItemStack heldItem = player.getHeldItem( hand );

        if ( !heldItem.isEmpty() ) {

            if ( heldItem.getItem() == Items.CARROT ) {
                debuggingCarrot( tile );
                return true;
            }

        }

        NetworkHooks.openGui( (ServerPlayerEntity) player, tile, tile.getPos() );

        return true;

    }
//endregion Overrides

    private void debuggingCarrot( TileEntityPlug tileEntityPlug ) {

        //nop

    }


}
