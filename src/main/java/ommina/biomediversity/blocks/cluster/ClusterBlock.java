package ommina.biomediversity.blocks.cluster;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.GlassBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.blocks.collector.TileEntityCollector;
import ommina.biomediversity.fluids.BdFluidTank;

import javax.annotation.Nullable;

public class ClusterBlock extends GlassBlock {

    public static final BooleanProperty FORMED = BooleanProperty.create( "formed" );

    public static final VoxelShape SHAPE = Block.makeCuboidShape( 0.03125D, 0d, 0.03125D, 16.0D - 0.03125D, 16d, 16.0D - 0.03125D );

    public ClusterBlock( Properties properties ) {
        super( properties );
    }

    //region Overrides
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context ) {

        return SHAPE;
    }

    @Override
    public VoxelShape getRaytraceShape( BlockState state, IBlockReader worldIn, BlockPos pos ) {
        return SHAPE;
    }

    @Override
    public void onPlayerDestroy( IWorld world, BlockPos blockPos, BlockState blockState ) {

        if ( !world.isRemote() )
            checkForController( world.getWorld(), blockPos );

        super.onPlayerDestroy( world, blockPos, blockState );

    }

    @Override
    public boolean onBlockActivated( BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit ) {

        if ( player.isSneaking() || !state.get( FORMED ) )
            return super.onBlockActivated( state, world, pos, player, hand, hit );

        if ( !world.isRemote && player.getHeldItem( hand ).getItem() == Items.BUCKET ) {

            BlockPos controllerPos = checkForController( world, pos );

            if ( controllerPos == null )
                return false;

            Vec3d collectorVector = new Vec3d( controllerPos.getX(), controllerPos.getY(), controllerPos.getZ() );
            int tankActivated = ModBlocks.COLLECTOR.getTankActivated( player, hit.getHitVec(), collectorVector );

            //BiomeDiversity.LOGGER.info( "  tankActivated: " + tankActivated );

            if ( tankActivated == -1 )
                return super.onBlockActivated( state, world, pos, player, hand, hit );

            TileEntity controller = world.getTileEntity( controllerPos );

            if ( !(controller instanceof TileEntityCollector) )
                return false;

            ItemStack heldItem = player.getHeldItem( hand );
            BdFluidTank tank = ((TileEntityCollector) controller).getTank( tankActivated );
            IItemHandler playerInv = new InvWrapper( player.inventory );

            FluidActionResult far = FluidUtil.tryFillContainerAndStow( heldItem, tank, playerInv, 1000, player, true );

            if ( far.isSuccess() ) {
                world.playSound( null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 0F );
                player.setHeldItem( hand, far.getResult() );
            }

            return true;

        }

        return super.onBlockActivated( state, world, pos, player, hand, hit );

    }

    @Override
    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        return this.getDefaultState().with( FORMED, false );
    }

    @Override
    public void onBlockPlacedBy( World world, BlockPos blockPos, BlockState state, @Nullable LivingEntity entity, ItemStack itemStack ) {

        checkForController( world, blockPos );

        super.onBlockPlacedBy( world, blockPos, state, entity, itemStack );

    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( FORMED );
    }
//endregion Overrides

    @Nullable
    private BlockPos checkForController( World world, BlockPos blockPos ) {

        if ( !world.isRemote )
            for ( int y = -1; y <= 1; y++ )
                for ( int x = -1; x <= 1; x++ )
                    for ( int z = -1; z <= 1; z++ )
                        if ( world.getBlockState( blockPos.add( x, y, z ) ).getBlock() instanceof IClusterController ) {
                            ((IClusterController) world.getBlockState( blockPos.add( x, y, z ) ).getBlock()).checkMultiblock( world, blockPos.add( x, y, z ) );
                            return blockPos.add( x, y, z );
                        }

        return null;

    }

}
