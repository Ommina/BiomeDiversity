package ommina.biomediversity.blocks.plug;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;
import ommina.biomediversity.blocks.BlockTileEntity;
import ommina.biomediversity.blocks.collector.TileEntityCollector;
import ommina.biomediversity.config.Constants;

public abstract class PlugBase extends BlockTileEntity<TileEntityPlugBase> {

    public PlugBase() {

        super( Block.Properties.create( Material.ROCK ).harvestLevel( 2 ).harvestTool( ToolType.PICKAXE ).hardnessAndResistance( Constants.DEFAULT_TILE_ENTITY_HARDNESS ) );

    }

    //region Overrides
    @Override
    public BlockRenderType getRenderType( BlockState state ) {
        return BlockRenderType.MODEL;
    }

    //@Override
    //public BlockRenderLayer getRenderLayer() {
    //    return BlockRenderLayer.CUTOUT;
    //}

    @Override
    public ActionResultType onBlockActivated( BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult ) {

        if ( world.isRemote )
            return super.onBlockActivated( blockState, world, pos, player, hand, rayTraceResult );

        TileEntityPlugBase tile = (TileEntityPlugBase) world.getTileEntity( pos );

        if ( tile == null )
            return super.onBlockActivated( blockState, world, pos, player, hand, rayTraceResult );

        BlockPos collectorPos = tile.getCollectorPos();

        if ( collectorPos != null && world.getTileEntity( collectorPos ) instanceof TileEntityCollector && world.getTileEntity( pos ) instanceof INamedContainerProvider )
            ((TileEntityCollector) world.getTileEntity( collectorPos )).forceBroadcast();

        ItemStack heldItem = player.getHeldItem( hand );

        if ( !heldItem.isEmpty() ) {

            if ( heldItem.getItem() == Items.CARROT ) {
                debuggingCarrot( tile );
                return ActionResultType.SUCCESS;
            }

        }

        NetworkHooks.openGui( (ServerPlayerEntity) player, tile, tile.getPos() );

        return ActionResultType.SUCCESS;

    }
//endregion Overrides

    private void debuggingCarrot( TileEntityPlugBase tileEntityPlugBase ) {

        //nop

    }

}
