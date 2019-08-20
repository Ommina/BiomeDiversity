package ommina.biomediversity.blocks;


import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import ommina.biomediversity.BiomeDiversity;

public abstract class BlockTileEntity<TE extends TileEntity> extends Block {

    public BlockTileEntity( String name, Block.Properties properties ) {
        super( properties );

        setRegistryName( BiomeDiversity.getId( name ) );

    }

      /*


    public abstract Class<TE> getTileEntityClass();

    @SuppressWarnings( "unchecked" )
    public TE getTileEntity( IBlockAccess world, BlockPos pos ) {

        return (TE) world.getTileEntity( pos );
    }


    //@Override
    public boolean hasTileEntity( IBlockState state ) {

        return true;
    }

    @Nullable
    //@Override
    public abstract TE createTileEntity( World world, IBlockState state );

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
