package ommina.biomediversity.blocks.collector;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import ommina.biomediversity.blocks.cluster.ClusterBlockTransparent;
import ommina.biomediversity.blocks.cluster.IClusterController;

public class Collector extends ClusterBlockTransparent implements IClusterController { // BlockTileEntity<TileEntityCollector> {

    public Collector() {

        super( Block.Properties.create( Material.ROCK ).harvestLevel( 2 ).harvestTool( ToolType.PICKAXE ).hardnessAndResistance( 10f ) );

    }

    //region Overrides
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean hasTileEntity( BlockState state ) {
        return true;
    }

    @Override
    public TileEntity createTileEntity( BlockState state, IBlockReader world ) {
        return new TileEntityCollector();
    }

    @Override
    public void onBlockClicked( BlockState state, World world, BlockPos blockPos, PlayerEntity playerEntity ) {

        world.setBlockState( blockPos, state.cycle( FORMED ) );

        System.out.println( "moo" );

        super.onBlockClicked( state, world, blockPos, playerEntity );
    }
//endregion Overrides

}
