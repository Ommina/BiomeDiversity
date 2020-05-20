package ommina.biomediversity.blocks.mixer_advanced;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.blocks.cluster.ClusterBlock;
import ommina.biomediversity.blocks.cluster.IClusterController;
import ommina.biomediversity.config.Constants;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class MixerAdvanced extends ClusterBlock implements IClusterController {

    private static final float INSET = 4f;//1f / 16f * 4f;
    public static final VoxelShape SHAPE = Block.makeCuboidShape( INSET, INSET, INSET, 16f - INSET, 16f - INSET, 16f - INSET );

    private Map<String, ClusterBlock> multiblock;

    public MixerAdvanced() {
        super( Block.Properties.create( Material.ROCK ).harvestLevel( 2 ).harvestTool( ToolType.PICKAXE ).hardnessAndResistance( Constants.DEFAULT_TILE_ENTITY_HARDNESS ).notSolid() );
    }

    //region Overrides
    @Override
    public boolean checkMultiblock( World world, BlockPos pos ) {

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
        multiblock.put( "-1-1-1", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "-1-10", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "-1-11", ModBlocks.CLUSTER_BLOCK_TANK );

        multiblock.put( "0-1-1", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "0-10", ModBlocks.CLUSTER_BLOCK_STURDY );
        multiblock.put( "0-11", ModBlocks.CLUSTER_BLOCK_TANK );

        multiblock.put( "1-1-1", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "1-10", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "1-11", ModBlocks.CLUSTER_BLOCK_TANK );

        //Middle Row
        multiblock.put( "-10-1", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "-100", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "-101", ModBlocks.CLUSTER_BLOCK_TANK );

        multiblock.put( "00-1", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "000", ModBlocks.MIXER_ADVANCED );
        multiblock.put( "001", ModBlocks.CLUSTER_BLOCK_TANK );

        multiblock.put( "10-1", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "100", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "101", ModBlocks.CLUSTER_BLOCK_TANK );

        //Top Row
        multiblock.put( "-11-1", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "-110", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "-111", ModBlocks.CLUSTER_BLOCK_TANK );

        multiblock.put( "01-1", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "010", ModBlocks.CLUSTER_BLOCK_STURDY );
        multiblock.put( "011", ModBlocks.CLUSTER_BLOCK_TANK );

        multiblock.put( "11-1", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "110", ModBlocks.CLUSTER_BLOCK_TANK );
        multiblock.put( "111", ModBlocks.CLUSTER_BLOCK_TANK );

    }

    @Override
    public BlockRenderType getRenderType( BlockState state ) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getCollisionShape( BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context ) {
        return this.blocksMovement ? state.getShape( worldIn, pos ) : VoxelShapes.empty();
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
    public ActionResultType onBlockActivated( BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit ) { // onBlockActivated

        if ( world.isRemote )
            return ActionResultType.CONSUME;

        return ActionResultType.PASS;

    }

    @Override
    public boolean hasTileEntity( BlockState state ) {
        return state.get( FORMED );
    }

    @Nullable
    @Override
    public TileEntity createTileEntity( BlockState state, IBlockReader world ) {

        if ( state.get( FORMED ) )
            return new TileEntityMixerAdvanced();

        return null;

    }

}
