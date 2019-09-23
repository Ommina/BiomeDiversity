package ommina.biomediversity.blocks.transmitter;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.BlockTileEntity;
import ommina.biomediversity.items.ModItems;

import javax.annotation.Nullable;

public class Transmitter extends BlockTileEntity<TileEntityTransmitter> { // implements IRightClickable {

    private static final BooleanProperty IS_CONNECTED = BooleanProperty.create( "connected" );

    public Transmitter() {

        super( Block.Properties.create( Material.ROCK ).harvestLevel( 2 ).harvestTool( ToolType.PICKAXE ).hardnessAndResistance( 10f ) );

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

            LazyOptional<IFluidHandler> foo = tile.getCapability( CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null );

            if ( foo.isPresent() ) {

                IFluidHandler tfi = foo.orElse( null );
                IItemHandler playerInv = new InvWrapper( player.inventory );

                FluidActionResult far = FluidUtil.tryEmptyContainerAndStow( heldItem, tfi, playerInv, 1000, player, true );

                if ( far.isSuccess() )
                    player.setHeldItem( hand, far.getResult() );

                BiomeDiversity.LOGGER.warn( "splish!" );

                return true;
            }
        }

        //player.openGui( Biomediversity.instance, ModGuiHandler.PILLAR, world, pos.getX(), pos.getY(), pos.getZ() );

        return true;


    }

    @Nullable
    @Override
    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        return this.getDefaultState().with( IS_CONNECTED, true );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( IS_CONNECTED );
    }


    private void debuggingCarrot( final World world, final BlockPos pos, final TileEntityTransmitter tile ) {

        BiomeDiversity.LOGGER.info( "Pillar identifier: " + tile.getIdentifier().toString() );
        BiomeDiversity.LOGGER.info( " Position: " + tile.getPos().toString() );

        if ( tile.getOwner() != null )
            BiomeDiversity.LOGGER.info( " Owner: " + tile.getOwner().toString() );

        if ( tile.getAssociatedIdentifier() != null )
            BiomeDiversity.LOGGER.info( " Associated: " + tile.getAssociatedIdentifier().toString() );

        if ( tile.getAssociatedPos() != null )
            BiomeDiversity.LOGGER.info( "  Position  : " + tile.getAssociatedPos().toString() );

        //if ( tile.getTank().getFluid() != null )
        //    BiomeDiversity.LOGGER.info( " Fluid: " + tile.getTank().getFluid().getFluid().getAttributes().getTranslationKey() + " (" + tile.getTank().getFluid().getAmount() + ")" );

        Biome b = world.getBiome( pos );

        BiomeDiversity.LOGGER.info( String.format( " Biome: %s temp: (%.1f) ", b.getRegistryName(), b.getDefaultTemperature() ) );

    }

        /*


    @Override
    public void breakBlock( World world, BlockPos pos, IBlockState state ) {

        TileEntityPillar tile = getTileEntity( world, pos );

        if ( tile.hasLink() ) {
            TileEntityAssociation.removeLink( world, tile, true );
        } else {
            PillarNetwork.removeTransmitter( tile );
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

                PillarData pd = PillarNetwork.getTransmitter( player.getUniqueID(), tile.getIdentifier() );

                pd.setAmount( 0 );
                pd.temperature = MathHelper.clamp( biome.getTemperature( pos ), 0.0f, 1.0f );
                pd.rainfall = MathHelper.clamp( biome.getRainfall(), 0.0f, 1.0f );
                pd.biomeId = Biome.getIdForBiome( biome );

                WorldData.get( world ).markDirty();

            }

        }

        super.onBlockPlacedBy( world, pos, state, placer, stack );

    }

*/

}
