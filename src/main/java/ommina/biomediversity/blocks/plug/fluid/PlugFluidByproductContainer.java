package ommina.biomediversity.blocks.plug.fluid;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.gui.ModContainer;

public class PlugFluidByproductContainer extends ModContainer {

    public PlugFluidByproductContainer( int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player ) {
        super( ModBlocks.PLUG_FLUID_BYPRODUCT_CONTAINER, windowId, world, pos, playerInventory, player );
    }

    //region Overrides
    @Override
    public ItemStack transferStackInSlot( PlayerEntity playerIn, int index ) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canInteractWith( PlayerEntity playerIn ) {
        return isWithinUsableDistance( IWorldPosCallable.of( this.tileEntity.getWorld(), tileEntity.getPos() ), playerEntity, ModBlocks.PLUG_FLUID_BYPRODUCT );
    }
    //endregion Overrides

}
