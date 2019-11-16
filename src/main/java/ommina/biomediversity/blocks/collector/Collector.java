package ommina.biomediversity.blocks.collector;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.blocks.cluster.ClusterBlock;
import ommina.biomediversity.blocks.cluster.IClusterController;

import java.util.HashMap;
import java.util.Map;

public class Collector extends ClusterBlock implements IClusterController {

    private Map<String, ClusterBlock> multiblock;

    public Collector() {

        super( Block.Properties.create( Material.ROCK ).harvestLevel( 2 ).harvestTool( ToolType.PICKAXE ).hardnessAndResistance( 3.2f ) );

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
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
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

    private void updateClusterBlockStates( World world, BlockPos blockPos, boolean formed ) {

        for ( int y = -1; y <= 1; y++ )
            for ( int x = -1; x <= 1; x++ )
                for ( int z = -1; z <= 1; z++ ) {
                    BlockPos pos = blockPos.add( x, y, z );
                    if ( world.getBlockState( pos ).getBlock() instanceof ClusterBlock )
                        world.setBlockState( pos, world.getBlockState( pos ).getBlock().getDefaultState().with( FORMED, formed ) );

                }
    }

}
