package ommina.biomediversity.blocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ommina.biomediversity.items.ModItems;

import javax.annotation.Nullable;

public class BlockNocifiedUndamaged extends Block {

    public BlockNocifiedUndamaged( Properties properties ) {
        super( properties );
    }

    /**
     * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via Block.removedByPlayer
     */
    @Override
    public void harvestBlock( World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack ) {

        player.addStat( Stats.BLOCK_MINED.get( this ) );
        player.addExhaustion( 0.015F );

        if ( player.getHeldItemMainhand().getItem() == Items.DIAMOND_PICKAXE ) { //TODO: This needs the correct custom tool, not the random diamond pickaxe.  Possibly move it to the loot table
            Block.spawnAsEntity( worldIn, pos, new ItemStack( ModItems.NOCIFIED_STONE_UNDAMAGED, 1 ) );
        } else {
            Block.spawnAsEntity( worldIn, pos, new ItemStack( ModItems.NOCIFIED_STONE_FRACTURED, 1 ) );
        }

    }

}
