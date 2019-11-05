package ommina.biomediversity.blocks.cluster;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.GlassBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ClusterBlock extends GlassBlock {

    public static final BooleanProperty FORMED = BooleanProperty.create( "formed" );

    public ClusterBlock( Properties properties ) {
        super( properties );
    }

    //region Overrides
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public void onPlayerDestroy( IWorld world, BlockPos blockPos, BlockState blockState ) {
        super.onPlayerDestroy( world, blockPos, blockState );

        if ( !world.isRemote() )
            checkForController( world.getWorld(), blockPos );

    }

/*

    @Override
    public void harvestBlock( World world, PlayerEntity playerEntity, BlockPos blockPos, BlockState blockState, @Nullable TileEntity tileEntity, ItemStack itemStack ) {

        if ( !world.isRemote ) {

            checkForController( world, blockPos );

            BiomeDiversity.LOGGER.warn( "HB" );

        }

        super.harvestBlock( world, playerEntity, blockPos, blockState, tileEntity, itemStack );

    }

*/

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

    private void checkForController( World world, BlockPos blockPos ) {

        if ( !world.isRemote )
            for ( int y = -1; y <= 1; y++ )
                for ( int x = -1; x <= 1; x++ )
                    for ( int z = -1; z <= 1; z++ )
                        if ( world.getBlockState( blockPos.add( x, y, z ) ).getBlock() instanceof IClusterController )
                            ((IClusterController) world.getBlockState( blockPos.add( x, y, z ) ).getBlock()).checkMultiblock( world, blockPos.add( x, y, z ) );

    }


}
