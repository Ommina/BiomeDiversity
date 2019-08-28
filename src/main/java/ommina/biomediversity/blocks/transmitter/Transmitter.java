package ommina.biomediversity.blocks.transmitter;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import ommina.biomediversity.blocks.BlockTileEntity;

import javax.annotation.Nullable;

public class Transmitter extends BlockTileEntity<TileEntityTransmitter> { // implements IRightClickable {

    private static final BooleanProperty IS_CONNECTED = BooleanProperty.create( "connected" );

    public Transmitter() {

        super( "transmitter", Block.Properties.create( Material.ROCK ).harvestLevel( 2 ).harvestTool( ToolType.PICKAXE ).hardnessAndResistance( 10f ) );

        this.setDefaultState( this.getDefaultState()
             .with( IS_CONNECTED, true ) );

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

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( IS_CONNECTED );
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        return this.getDefaultState().with( IS_CONNECTED, true );
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }



/*

    @Override
    public boolean onBlockActivated( final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ ) {

        if ( world.isRemote )
            return true;

        TileEntityPillar tile = getTileEntity( world, pos );
        ItemStack heldItem = player.getHeldItem( hand );

        if ( !heldItem.isEmpty() ) {

            if ( heldItem.getItem() == Items.CARROT ) {
                debuggingCarrot( world, pos, tile );
                return true;
            } else if ( heldItem.getItem() == ModItems.linkstaff )
                return false;

            if ( heldItem.hasCapability( CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null ) ) {

                IFluidHandler tfi = tile.getCapability( CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side );
                IItemHandler playerInv = new InvWrapper( player.inventory );

                FluidActionResult far = FluidUtil.tryEmptyContainerAndStow( heldItem, tfi, playerInv, Fluid.BUCKET_VOLUME, player );

                if ( far.isSuccess() )
                    player.setHeldItem( hand, far.getResult() );

                return true;
            }
        }

        player.openGui( Biomediversity.instance, ModGuiHandler.PILLAR, world, pos.getX(), pos.getY(), pos.getZ() );

        return true;

    }

    private void debuggingCarrot( final World world, final BlockPos pos, final TileEntityPillar tile ) {

        BiomeDiversity.logger.info( "Pillar identifier: " + tile.getIdentifier().toString() );
        BiomeDiversity.logger.info( " Position: " + tile.getPos().toString() );

        if ( tile.getOwner() != null )
            BiomeDiversity.logger.info( " Owner: " + tile.getOwner().toString() );

        if ( tile.getAssociatedIdentifier() != null )
            BiomeDiversity.logger.info( " Associated: " + tile.getAssociatedIdentifier().toString() );

        if ( tile.getAssociatedPos() != null )
            BiomeDiversity.logger.info( "  Position  : " + tile.getAssociatedPos().toString() );

        if ( tile.getTank().getFluid() != null )
            BiomeDiversity.logger.info( " Fluid: " + tile.getTank().getFluid().getFluid().getName() + " (" + tile.getTank().getFluid().amount + ")" );

        Biome b = world.getBiome( pos );

        BiomeDiversity.logger.info( String.format( " Biome: %s (%d) temp: (%.1f) ", b.getRegistryName(), Biome.getIdForBiome( b ), b.getDefaultTemperature() ) );

    }

    @Override
    public void breakBlock( World world, BlockPos pos, IBlockState state ) {

        TileEntityPillar tile = getTileEntity( world, pos );

        if ( tile.hasLink() ) {
            TileEntityAssociation.removeLink( world, tile, true );
        } else {
            PillarNetwork.removePillar( tile );
            PillarNetwork.markDirty( world );
        }

        super.breakBlock( world, pos, state );

    }

    @Override
    public void onBlockPlacedBy( World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack ) {

        if ( placer instanceof EntityPlayer ) {

            EntityPlayer player = (EntityPlayer) placer;
            TileEntityPillar tile = getTileEntity( world, pos );
            Biome biome = world.getBiome( pos );

            tile.setOwner( player.getUniqueID() );

            if ( !world.isRemote ) {

                PillarData pd = PillarNetwork.getPillar( player.getUniqueID(), tile.getIdentifier() );

                pd.setAmount( 0 );
                pd.temperature = MathHelper.clamp( biome.getTemperature( pos ), 0.0f, 1.0f );
                pd.rainfall = MathHelper.clamp( biome.getRainfall(), 0.0f, 1.0f );
                pd.biomeId = Biome.getIdForBiome( biome );

                WorldData.get( world ).markDirty();

            }

        }

        super.onBlockPlacedBy( world, pos, state, placer, stack );

    }

    @Override
    public boolean isOpaqueCube( IBlockState state ) {

        return false;
    }

    @Override
    public boolean isFullCube( IBlockState state ) {

        return false;
    }

    @SideOnly( Side.CLIENT )
    @Override
    public BlockRenderLayer getBlockLayer() {

        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public Class<TileEntityPillar> getTileEntityClass() {

        return TileEntityPillar.class;
    }

    @Nullable
    @Override
    public TileEntityPillar createTileEntity( World world, IBlockState state ) {

        return new TileEntityPillar();
    }

    // --------------------
    // Blockstates

    private static final int DISCONNECTED = 0;
    private static final int CONNECTED = 1;

    @Override
    public IBlockState getStateFromMeta( int meta ) {

        return this.getDefaultState().withProperty( TileEntityAssociation.IS_CONNECTED, Boolean.valueOf( (meta & CONNECTED) == CONNECTED ) );
    }

    @Override
    public int getMetaFromState( IBlockState state ) {

        return state.getValue( TileEntityAssociation.IS_CONNECTED ) ? CONNECTED : DISCONNECTED;
    }

    @Override
    protected BlockStateContainer createBlockState() {

        return new BlockStateContainer( this, new IProperty[]{ TileEntityAssociation.IS_CONNECTED } );
    }

    // --------------------

*/

}
