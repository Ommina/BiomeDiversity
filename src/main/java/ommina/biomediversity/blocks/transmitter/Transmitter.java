package ommina.biomediversity.blocks.transmitter;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.BlockTileEntity;
import ommina.biomediversity.blocks.tile.TileEntityAssociation;
import ommina.biomediversity.config.Constants;
import ommina.biomediversity.items.ModItems;
import ommina.biomediversity.worlddata.TransmitterData;

import javax.annotation.Nullable;

public class Transmitter extends BlockTileEntity<TileEntityTransmitter> { // implements IRightClickable {

    private static final BooleanProperty IS_CONNECTED = BooleanProperty.create( "connected" );

    public Transmitter() {

        super( Block.Properties.create( Material.ROCK ).harvestLevel( 2 ).harvestTool( ToolType.PICKAXE ).hardnessAndResistance( Constants.DEFAULT_TILE_ENTITY_HARDNESS ) );

        this.setDefaultState( this.getDefaultState()
             .with( IS_CONNECTED, true ) );

    }

    private void debuggingCarrot( final World world, final BlockPos pos, final TileEntityTransmitter tile ) {

        BiomeDiversity.LOGGER.info( "Transmitter identifier: " + tile.getIdentifier().toString() );
        BiomeDiversity.LOGGER.info( " Position: " + tile.getPos().toString() );

        if ( tile.getOwner() != null )
            BiomeDiversity.LOGGER.info( " Owner: " + tile.getOwner().toString() );

        if ( tile.getAssociatedIdentifier() != null )
            BiomeDiversity.LOGGER.info( " Associated: " + tile.getAssociatedIdentifier().toString() );

        if ( tile.getAssociatedPos() != null )
            BiomeDiversity.LOGGER.info( "  Position  : " + tile.getAssociatedPos().toString() );

        if ( tile.getTank( 0 ).getFluid() != null )
            BiomeDiversity.LOGGER.info( " Fluid: " + tile.getTank( 0 ).getFluid().getFluid().getAttributes().getTranslationKey() + " (" + tile.getTank( 0 ).getFluid().getAmount() + ")" );

        Biome b = world.getBiome( pos );

        BiomeDiversity.LOGGER.info( String.format( " Biome: %s temp: (%.1f) ", b.getRegistryName(), b.getDefaultTemperature() ) );

    }

    // Overrides
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean onBlockActivated( BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult ) {

        if ( world.isRemote )
            return true;

        TileEntityTransmitter tile = (TileEntityTransmitter) world.getTileEntity( pos );
        ItemStack heldItem = player.getHeldItem( hand );

        if ( !heldItem.isEmpty() ) {

            if ( heldItem.getItem() == Items.CARROT ) {
                debuggingCarrot( world, pos, tile );
                return true;
            } else if ( heldItem.getItem() == ModItems.LINK_STAFF )
                return false;

            LazyOptional<IFluidHandler> capability = tile.getCapability( CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null );

            if ( capability.isPresent() ) {

                IFluidHandler tfi = capability.orElse( null );
                IItemHandler playerInv = new InvWrapper( player.inventory );

                FluidActionResult far = FluidUtil.tryEmptyContainerAndStow( heldItem, tfi, playerInv, 1000, player, true );

                if ( far.isSuccess() ) {
                    world.playSound( null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 0F );
                    player.setHeldItem( hand, far.getResult() );
                }

                return true;

            }
        }

        NetworkHooks.openGui( (ServerPlayerEntity) player, tile, tile.getPos() );

        return true;

    }

    @Nullable
    @Override
    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        return this.getDefaultState().with( IS_CONNECTED, true );
    }

    @Override
    public void onBlockPlacedBy( World world, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack ) {

        if ( livingEntity instanceof PlayerEntity ) {

            PlayerEntity player = (PlayerEntity) livingEntity;
            TileEntityTransmitter tile = (TileEntityTransmitter) world.getTileEntity( blockPos );
            Biome biome = world.getBiome( blockPos );

            tile.setOwner( player.getUniqueID() );

            if ( !world.isRemote ) {

                world.getCapability( BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY, null ).ifPresent( cap -> {

                    TransmitterData pd = cap.getTransmitter( player.getUniqueID(), tile.getIdentifier() );
                    pd.setAmount( 0 );
                    pd.temperature = MathHelper.clamp( biome.getTemperature( blockPos ), 0.0f, 1.0f );
                    pd.rainfall = MathHelper.clamp( biome.getDownfall(), 0.0f, 1.0f );
                    pd.biomeId = biome.getRegistryName();

                } );

            }

        }

        super.onBlockPlacedBy( world, blockPos, blockState, livingEntity, itemStack );
    }

    @Override
    public void onBlockHarvested( World world, BlockPos pos, BlockState blockState, PlayerEntity playerEntity ) {

        TileEntityTransmitter tile = (TileEntityTransmitter) world.getTileEntity( pos );

        if ( tile.hasLink() ) {
            TileEntityAssociation.removeLink( world, tile, true );
            world.getCapability( BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY, null ).ifPresent( cap -> cap.removeTransmitter( tile ) );
        }

        super.onBlockHarvested( world, pos, blockState, playerEntity );

    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( IS_CONNECTED );
    }

    @Override
    public boolean hasTileEntity( BlockState state ) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity( BlockState state, IBlockReader world ) {
        return new TileEntityTransmitter();
    }
//endregion Overrides

}
