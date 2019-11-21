package ommina.biomediversity.blocks.receiver;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.tile.TileEntityAssociation;
import ommina.biomediversity.config.Constants;
import ommina.biomediversity.items.ModItems;
import ommina.biomediversity.worlddata.TransmitterData;

import javax.annotation.Nullable;

public class Receiver extends Block {

    private static final VoxelShape SHAPE = Block.makeCuboidShape( 0.0D, 0.0D, 0.0D, 16.0D, 32.0D, 16.0D );
    private static final BooleanProperty IS_CONNECTED = BooleanProperty.create( "connected" );


    public Receiver() {

        super( Block.Properties.create( Material.ROCK ).harvestLevel( 2 ).harvestTool( ToolType.PICKAXE ).hardnessAndResistance( Constants.DEFAULT_TILE_ENTITY_HARDNESS ) );

        this.setDefaultState( this.getDefaultState()
             .with( IS_CONNECTED, false ) );

    }

    //region Overrides
    @Override
    public VoxelShape getShape( BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context ) {
        return SHAPE;
    }

    @Override
    @Deprecated
    public VoxelShape getRaytraceShape( BlockState state, IBlockReader worldIn, BlockPos pos ) {
        return SHAPE;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean onBlockActivated( BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult ) {

        if ( world.isRemote )
            return true;

        TileEntityReceiver tile = (TileEntityReceiver) world.getTileEntity( pos );

        if ( tile == null )
            return super.onBlockActivated( blockState, world, pos, player, hand, rayTraceResult );

        ItemStack heldItem = player.getHeldItem( hand );

        if ( !heldItem.isEmpty() ) {

            if ( heldItem.getItem() == Items.CARROT ) {
                debuggingCarrot( tile );
                return true;
            } else if ( heldItem.getItem() == ModItems.LINK_STAFF )
                return false;

        }

        NetworkHooks.openGui( (ServerPlayerEntity) player, tile, tile.getPos() );

        return true;

    }

    @Nullable
    @Override
    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        return this.getDefaultState().with( IS_CONNECTED, false );
    }

    @Override
    public void onBlockPlacedBy( World world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack stack ) {

        if ( livingEntity instanceof PlayerEntity ) {

            PlayerEntity player = (PlayerEntity) livingEntity;
            TileEntityReceiver tile = (TileEntityReceiver) world.getTileEntity( pos );

            tile.setOwner( player.getUniqueID() );
            tile.markDirty();

        }

        super.onBlockPlacedBy( world, pos, state, livingEntity, stack );

    }

    @Override
    public void onBlockHarvested( World world, BlockPos pos, BlockState blockState, PlayerEntity playerEntity ) {

        TileEntityReceiver tile = (TileEntityReceiver) world.getTileEntity( pos );

        if ( tile.hasLink() ) {
            TileEntityAssociation.removeLink( world, tile, true );
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
        return new TileEntityReceiver();
    }
//endregion Overrides

    private static void debuggingCarrot( TileEntityReceiver tile ) {

        BiomeDiversity.LOGGER.info( "Receiver identifier: " + tile.getIdentifier().toString() );

        if ( tile.getOwner() != null )
            BiomeDiversity.LOGGER.info( " Owner: " + tile.getOwner().toString() );

        if ( tile.getAssociatedIdentifier() != null )
            BiomeDiversity.LOGGER.info( " Associated: " + tile.getAssociatedIdentifier().toString() );

        if ( tile.getAssociatedPos() != null )
            BiomeDiversity.LOGGER.info( " Position: " + tile.getAssociatedPos().toString() );

        if ( tile.getTank( 0 ).getFluid() != null )
            BiomeDiversity.LOGGER.info( " Fluid: " + tile.getTank( 0 ).getFluid().getFluid().getRegistryName().toString() + " (" + tile.getTank( 0 ).getFluid().getAmount() + ")" );

        if ( tile.getCollector().getCollector() == null )
            BiomeDiversity.LOGGER.info( "No Collector stored." );
        else
            BiomeDiversity.LOGGER.info( "Collector at " + tile.getCollector().getCollector().getPos().toString() );

        if ( tile.getOwner() != null && tile.getAssociatedIdentifier() != null ) {

            tile.getWorld().getCapability( BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY, null ).ifPresent( cap -> {

                TransmitterData pd = cap.getTransmitter( tile.getOwner(), tile.getAssociatedIdentifier() );

                BiomeDiversity.LOGGER.info( " TRANSMITTER_NETWORK" );
                BiomeDiversity.LOGGER.info( "   Biome id : " + pd.biomeId );
                BiomeDiversity.LOGGER.info( "   Biome temperature : " + pd.temperature );

                if ( pd.fluid != null )
                    BiomeDiversity.LOGGER.info( "   Fluid: " + pd.fluid.getRegistryName().toString() + " (" + pd.getAmount() + ")" );


            } );

        }

    }

}
