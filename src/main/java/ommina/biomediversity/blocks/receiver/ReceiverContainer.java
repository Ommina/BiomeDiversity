package ommina.biomediversity.blocks.receiver;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.gui.ModContainer;

public class ReceiverContainer extends ModContainer {

    public ReceiverContainer( int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player ) {
        super( ModBlocks.RECEIVER_CONTAINER, windowId, world, pos, playerInventory, player );

    }

//region Overrides

    @Override
    public boolean canInteractWith( PlayerEntity playerIn ) {
        return isWithinUsableDistance( IWorldPosCallable.of( this.tileEntity.getWorld(), tileEntity.getPos() ), playerEntity, ModBlocks.RECEIVER );
    }

//endregion Overrides

}
