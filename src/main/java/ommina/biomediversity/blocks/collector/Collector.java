package ommina.biomediversity.blocks.collector;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.blocks.cluster.ClusterBlock;
import ommina.biomediversity.blocks.cluster.IClusterController;
import ommina.biomediversity.config.Constants;
import ommina.biomediversity.fluids.BdFluidTank;

import java.util.HashMap;
import java.util.Map;

public class Collector extends ClusterBlock implements IClusterController {

    private static final VoxelShape RENDER_SHAPE_FORMED = Block.makeCuboidShape( -16f, -16f, -16f, 32f, 32f, 32f );

    private final Rectangles rectangles = new Rectangles();
    private Map<String, ClusterBlock> multiblock;

    public Collector() {
        super( Block.Properties.create( Material.ROCK ).harvestLevel( 2 ).harvestTool( ToolType.PICKAXE ).hardnessAndResistance( Constants.DEFAULT_TILE_ENTITY_HARDNESS ).notSolid() );

        addRectangles();

    }

    //region Overrides
    @Override
    public boolean checkMultiblock( IWorld world, BlockPos pos ) {

        if ( multiblock == null )
            defineMultiblock();

        boolean formed = true;

        for ( int y = -1; y <= 1; y++ )
            for ( int x = -1; x <= 1; x++ )
                for ( int z = -1; z <= 1; z++ ) {
                    if ( multiblock.get( "" + x + y + z ) != world.getBlockState( pos.add( x, y, z ) ).getBlock() ) {
                        formed = false;
                        break;
                    }
                }

        TileEntity te = world.getTileEntity( pos );

        if ( te != null )
            te.onChunkUnloaded();

        updateClusterBlockStates( world, pos, formed );

        return formed;

    }

    @Override
    public void defineMultiblock() {

        multiblock = new HashMap<>( 27 );

        //Bottom Row
        multiblock.put( "-1-1-1", ModBlocks.CLUSTER_BLOCK_GENERIC );
        multiblock.put( "-1-10", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "-1-11", ModBlocks.CLUSTER_BLOCK_GENERIC );

        multiblock.put( "0-1-1", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "0-10", ModBlocks.CLUSTER_BLOCK_STURDY );
        multiblock.put( "0-11", ModBlocks.CLUSTER_BLOCK_TANK );

        multiblock.put( "1-1-1", ModBlocks.CLUSTER_BLOCK_GENERIC );
        multiblock.put( "1-10", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "1-11", ModBlocks.CLUSTER_BLOCK_GENERIC );

        //Middle Row
        multiblock.put( "-10-1", ModBlocks.CLUSTER_BLOCK_GENERIC );
        multiblock.put( "-100", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "-101", ModBlocks.CLUSTER_BLOCK_GENERIC );

        multiblock.put( "00-1", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "000", ModBlocks.COLLECTOR );
        multiblock.put( "001", ModBlocks.CLUSTER_BLOCK_TANK );

        multiblock.put( "10-1", ModBlocks.CLUSTER_BLOCK_GENERIC );
        multiblock.put( "100", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "101", ModBlocks.CLUSTER_BLOCK_GENERIC );

        //Top Row
        multiblock.put( "-11-1", ModBlocks.CLUSTER_BLOCK_GENERIC );
        multiblock.put( "-110", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "-111", ModBlocks.CLUSTER_BLOCK_GENERIC );

        multiblock.put( "01-1", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "010", ModBlocks.CLUSTER_BLOCK_STURDY );
        multiblock.put( "011", ModBlocks.CLUSTER_BLOCK_TANK );

        multiblock.put( "11-1", ModBlocks.CLUSTER_BLOCK_GENERIC );
        multiblock.put( "110", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "111", ModBlocks.CLUSTER_BLOCK_GENERIC );

    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context ) {
        return state.get( FORMED ) ? RENDER_SHAPE_FORMED : VoxelShapes.fullCube();
    }

    @Override
    public void onPlayerDestroy( IWorld world, BlockPos blockPos, BlockState blockState ) {
        super.onPlayerDestroy( world, blockPos, blockState );

        if ( !world.isRemote() && world.getTileEntity( blockPos ) instanceof TileEntityCollector )
            world.getTileEntity( blockPos ).remove();

    }

    @Override
    public ActionResultType onBlockActivated( BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit ) { // onBlockActivated

        if ( world.isRemote )
            return ActionResultType.CONSUME;

        if ( player.getHeldItem( hand ).getItem() == Items.BUCKET ) {

            Vector3d collectorVector = new Vector3d( pos.getX(), pos.getY(), pos.getZ() );
            int tankActivated = ModBlocks.COLLECTOR.getTankActivated( player, hit.getHitVec(), collectorVector );

            if ( tankActivated == -1 )
                return ActionResultType.PASS;

            TileEntity controller = world.getTileEntity( pos );

            if ( !(controller instanceof TileEntityCollector) )
                return ActionResultType.PASS;

            ItemStack heldItem = player.getHeldItem( hand );
            BdFluidTank tank = ((TileEntityCollector) controller).getTank( tankActivated );
            IItemHandler playerInv = new InvWrapper( player.inventory );

            FluidActionResult far = FluidUtil.tryFillContainerAndStow( heldItem, tank, playerInv, 1000, player, true );

            if ( far.isSuccess() ) {
                world.playSound( null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 0F );
                player.setHeldItem( hand, far.getResult() );
                return ActionResultType.SUCCESS;
            }

        }

        return ActionResultType.PASS;

    }

    @Override
    public boolean hasTileEntity( BlockState state ) {

        return state.get( FORMED );

    }

    @Override
    public TileEntity createTileEntity( BlockState state, IBlockReader world ) {
        return new TileEntityCollector();
    }

    @Override
    public boolean isSideInvisible( BlockState state, BlockState adjacentState, Direction side ) {

        if ( state.get( FORMED ) )
            return true;

        return super.isSideInvisible( state, adjacentState, side );
    }
//endregion Overrides

    private void addRectangles() {

        addRectangle( Tubes.Unused0 );
        addRectangle( Tubes.Unused1 );
        addRectangle( Tubes.Unused2 );
        addRectangle( Tubes.Cool );
        addRectangle( Tubes.Warm );
        addRectangle( Tubes.Unused5 );
        addRectangle( Tubes.Unused6 );
        addRectangle( Tubes.Byproduct );

    }

    private void addRectangle( final Tubes tube ) {
        addRectangle( tube.tank, tube.location.x, tube.location.y );
    }

    private void addRectangle( int tank, float x, float z ) {

        final Vector3d width_x = new Vector3d( 4, 0, 0 );
        final Vector3d width_z = new Vector3d( 0, 0, 4 );
        final Vector3d height = new Vector3d( 0, 37, 0 );

        final int y = -13;

        rectangles.add( tank, new Vector3d( x, y, z ), width_x, height );
        rectangles.add( tank, new Vector3d( x + 4f, y, z + 0.5 ), width_z, height );
        rectangles.add( tank, new Vector3d( x, y, z + 4.5 ), width_x, height );
        rectangles.add( tank, new Vector3d( x - 0.5, y, z + 0.5 ), width_z, height );

    }

    public int getTankActivated( PlayerEntity player, Vector3d hitVec, Vector3d collector ) {
        return rectangles.getTank( player, hitVec, collector );
    }

}
