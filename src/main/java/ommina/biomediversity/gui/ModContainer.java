package ommina.biomediversity.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public abstract class ModContainer extends Container {

    protected TileEntity tileEntity;
    protected PlayerEntity playerEntity;
    protected IItemHandler playerInventory;

    public ModContainer( @Nullable ContainerType<?> type, int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player ) {
        super( type, windowId );

        this.tileEntity = world.getTileEntity( pos );
        this.playerEntity = player;
        this.playerInventory = new InvWrapper( playerInventory );

        layoutPlayerInventorySlots( 8, 84 );

    }

    public TileEntity getTileEntity() {
        return this.tileEntity;
    }

    protected int addSlotRange( IItemHandler handler, int index, int x, int y, int amount, int dx ) {

        for ( int i = 0; i < amount; i++ ) {
            addSlot( new SlotItemHandler( handler, index, x, y ) );
            x += dx;
            index++;
        }

        return index;

    }

    protected void layoutPlayerInventorySlots( int leftCol, int topRow ) {

        addSlot( playerInventory, 9, leftCol, topRow, 9, 18, 3, 18 ); // Player inventory

        topRow += 58;
        addSlotRange( playerInventory, 0, leftCol, topRow, 9, 18 ); // Hotbar

    }

    protected int addSlot( IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy ) {

        for ( int j = 0; j < verAmount; j++ ) {
            index = addSlotRange( handler, index, x, y, horAmount, dx );
            y += dy;
        }

        return index;

    }

}
