package ommina.biomediversity.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import ommina.biomediversity.blocks.tile.TileEntityAssociation;
import ommina.biomediversity.blocks.transmitter.TileEntityTransmitter;
import ommina.biomediversity.util.NbtUtils;
import ommina.biomediversity.util.Translator;
import ommina.biomediversity.worlddata.capabilities.ITransmitterNetwork;
import ommina.biomediversity.worlddata.capabilities.TransmitterNetworkProvider;

public class LinkStaff extends Item {

    public LinkStaff( Item.Properties properties ) {
        super( properties );
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick( World world, PlayerEntity player, Hand hand ) {

        if ( player.isSneaking() ) {

            removeCopiedSettings( player.getHeldItem( hand ) );
            player.sendStatusMessage( new StringTextComponent( Translator.translateToLocal( "text.biomediversity.linkstaff.cleared" ) ), true );

        }

        return ActionResult.newResult( ActionResultType.SUCCESS, player.getHeldItem( hand ) );

    }

    private void removeCopiedSettings( ItemStack item ) {

        item.clearCustomName();
        item.setTag( null );

    }

    @Override
    public ActionResultType onItemUseFirst( ItemStack stack, ItemUseContext context ) {

        if ( context.getWorld().isRemote )
            return ActionResultType.SUCCESS;

        TileEntity te = context.getWorld().getTileEntity( context.getPos() );

        if ( !(te instanceof TileEntityAssociation) )
            return ActionResultType.SUCCESS;

        ItemStack item = context.getPlayer().getHeldItem( context.getHand() );

        TileEntityAssociation tile = (TileEntityAssociation) te;

        if ( isAlreadyCopying( item, tile ) )
            return ActionResultType.SUCCESS;

        if ( !context.getPlayer().isSneaking() )
            return useUnSneaking( context, tile, item );

        return useSneaking( context, tile, item );

    }

    private ActionResultType useUnSneaking( ItemUseContext context, TileEntityAssociation te, ItemStack item ) {

        if ( !isCopying( item ) )
            copy( context.getPlayer().getHeldItem( context.getHand() ), te, context.getPlayer() );
        else
            paste( context.getWorld(), context.getPlayer().getHeldItem( context.getHand() ), te, context.getPlayer() );

        return ActionResultType.SUCCESS;

    }

    private ActionResultType useSneaking( ItemUseContext context, TileEntityAssociation te, ItemStack item ) {

        if ( !isCopying( item ) && te.hasAssociation() )
            copyAssociation( context.getPlayer().getHeldItem( context.getHand() ), te, context.getPlayer() );

        return ActionResultType.SUCCESS;

    }

    private boolean isCopying( ItemStack item ) {

        return item.getTag() != null && item.getTag().contains( "copyfrom" );

    }

    private boolean isAlreadyCopying( ItemStack item, TileEntityAssociation tile ) {

        return isCopying( item ) && item.getTag().getInt( "copyfrom" ) == tile.getSource();

    }

    private void copy( ItemStack item, TileEntityAssociation tile, PlayerEntity player ) {

        CompoundNBT compound;
        BlockPos pos = tile.getPos();

        compound = new CompoundNBT();
        compound.putInt( "copyfrom", tile.getSource() );
        compound.putUniqueId( "identifier", tile.getIdentifier() );

        NbtUtils.addBlockPosToNbt( compound, tile.getPos() );

        item.setTag( compound );

        player.sendStatusMessage( new StringTextComponent( Translator.translateToLocal( "text.biomediversity.linkstaff.settingscopied" ) ), true );

        item.setDisplayName( new StringTextComponent( String.format( Translator.translateToLocal( "text.biomediversity.linkstaff.copyfrom" ), tile.getSourceName(), pos.getX(), pos.getY(), pos.getZ() ) ) );

    }

    private void copyAssociation( ItemStack item, TileEntityAssociation tile, PlayerEntity player ) {

        CompoundNBT compound;
        BlockPos pos = tile.getAssociatedPos();

        compound = new CompoundNBT();

        compound.putInt( "copyfrom", 1 );
        compound.putUniqueId( "identifier", tile.getAssociatedIdentifier() );

        NbtUtils.addBlockPosToNbt( compound, pos );

        item.setTag( compound );

        player.sendStatusMessage( new StringTextComponent( Translator.translateToLocal( "text.biomediversity.linkstaff.transmittersettingscopied" ) ), true );

        item.setDisplayName( new StringTextComponent( String.format( Translator.translateToLocal( "text.biomediversity.linkstaff.copyfrom" ), tile.getTargetName(), pos.getX(), pos.getY(), pos.getZ() ) ) );

    }

    private void paste( World world, ItemStack item, TileEntityAssociation tile, PlayerEntity player ) {

        CompoundNBT compound = item.getTag();

        TileEntityAssociation.createLink( world, tile, compound.getUniqueId( "identifier" ), NbtUtils.getBlockPosFromNbt( compound ) );

        item.clearCustomName();
        item.setTag( null );

        player.sendStatusMessage( new StringTextComponent( Translator.translateToLocal( "text.biomediversity.linkstaff.settingspasted" ) ), true );

        if ( tile instanceof TileEntityTransmitter ) {

            //world.getCapability( TransmitterNetworkProvider.TRANSMITTER_NETWORK_CAPABILITY ).isPresent( c -> {
            //    ((ITransmitterNetwork) c).getTransmitter( tile.getOwner(), tile.getIdentifier() ).receiver = tile.getAssociatedIdentifier(); });

            //handler.ifPresent(h -> {
            //    CompoundNBT compound = ((INBTSerializable<CompoundNBT>)h).serializeNBT();
            //    tag.put("inv", compound);
            //});

            LazyOptional<ITransmitterNetwork> t = world.getCapability( TransmitterNetworkProvider.TRANSMITTER_NETWORK_CAPABILITY, null );

            ((ITransmitterNetwork) t).getTransmitter( tile.getOwner(), tile.getIdentifier() ).receiver = tile.getAssociatedIdentifier();

        }

        //WorldData.get( world ).markDirty();

    }

}
