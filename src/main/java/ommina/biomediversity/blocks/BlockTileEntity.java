package ommina.biomediversity.blocks;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public abstract class BlockTileEntity<TE extends TileEntity> extends Block {

    public BlockTileEntity( Block.Properties properties ) {
        super( properties );

    }

    @Override
    public boolean hasTileEntity( BlockState state ) {
        return true;
    }

    @Override
    public abstract TileEntity createTileEntity( BlockState state, IBlockReader world );



      /*

    @SuppressWarnings( "unchecked" )
    public TE getTileEntity( IBlockAccess world, BlockPos pos ) {

        return (TE) world.getTileEntity( pos );
    }


    @Nullable
    //@Override

    // Facing

    //@Override
    public IBlockState getStateForPlacement( World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand ) {

        if ( this instanceof IBlockHasFacing )
            return this.blockState.getBaseState().withProperty( FACING, placer.getHorizontalFacing().getOpposite() );

        return super.getStateForPlacement( world, pos, facing, hitX, hitY, hitZ, meta, placer, hand );
    }

    @Override
    public void onBlockPlacedBy( World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack ) {

        if ( this instanceof IBlockHasFacing )
            setFacing( worldIn, pos, placer.getHorizontalFacing().getOpposite() );

    }

    public void setFacing( IBlockAccess world, BlockPos pos, EnumFacing facing ) {

        final TE tile = getTileEntity( world, pos );

        if ( tile != null && tile instanceof ITileHasFacing )
            ((ITileHasFacing) tile).setFacing( facing );

    }

    public EnumFacing getFacing( IBlockAccess world, BlockPos pos ) {

        final TE tile = getTileEntity( world, pos );

        return (tile != null && tile instanceof ITileHasFacing) ? ((ITileHasFacing) tile).getFacing() : EnumFacing.NORTH;

    }

    // End Facing
*/
}
