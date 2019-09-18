
package ommina.biomediversity.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;


public abstract class BdFluid extends ForgeFlowingFluid {

    private FluidAttributes attributes;
    private Item bucketItem;
    private FlowingFluid stillFluid;
    private Fluid flowingFluid;
    private Block backingBlock;

    public BdFluid( FluidAttributes attributes ) {
        super( null );
        this.attributes = attributes;
    }

    public void setFluids( FlowingFluid still, Fluid flowing ) {
        this.stillFluid = still;
        this.flowingFluid = flowing;
    }

    public void setBucketItem( Item bucketItem ) {
        this.bucketItem = bucketItem;
    }

    public void setBlock( Block block ) {
        this.backingBlock = block;
    }

    /*

    public BdFluid( String name, String still, String flowing, String overlay, Color colour, int density, int temperature, int luminosity, int viscosity, Block block, Rarity rarity ) {

        this.attributes = FluidAttributes.builder( name, BiomeDiversity.getId( "block/fluid/" + still ), BiomeDiversity.getId( "block/fluid/" + flowing ) )
             .overlay( BiomeDiversity.getId( "block/fluid/" + overlay ) )
             .color( colour.getRGB() )
             .density( density )
             .temperature( temperature )
             .luminosity( luminosity )
             .viscosity( viscosity )
             .rarity( rarity )
             .block( () -> block )
             .build();

    }

    */

    //@Override
    //protected FluidAttributes createAttributes( Fluid fluid ) {
    //    return this.attributes;
    //}

    @Override
    public Fluid getFlowingFluid() {
        return this.flowingFluid;
    }

    @Override
    public Fluid getStillFluid() {
        return this.stillFluid;
    }

    @Override
    protected boolean canSourcesMultiply() {
        return false;
    }

    @Override
    protected void beforeReplacingBlock( IWorld worldIn, BlockPos pos, BlockState state ) {
        // copied from the WaterFluid implementation
        TileEntity tileentity = state.getBlock().hasTileEntity() ? worldIn.getTileEntity( pos ) : null;
        Block.spawnDrops( state, worldIn.getWorld(), pos, tileentity );
    }

    @Override
    protected int getSlopeFindDistance( IWorldReader worldIn ) {
        return 4;
    }

    @Override
    protected int getLevelDecreasePerBlock( IWorldReader worldIn ) {
        return 1;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public Item getFilledBucket() {
        return this.bucketItem;
    }

    //@Override
    //protected boolean func_215665_a( IFluidState state, IBlockReader block, BlockPos pos, Fluid fluid, Direction direction ) {
    //    return direction == Direction.DOWN && !fluid.isIn( FluidTags.WATER );
    //}

    @Override
    public int getTickRate( IWorldReader worldReader ) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 1;
    }

    @Override
    protected BlockState getBlockState( IFluidState state ) {
        return backingBlock.getDefaultState().with( FlowingFluidBlock.LEVEL, getLevelFromState( state ) );
    }

    @Override
    public boolean isEquivalentTo( Fluid fluidIn ) {
        return fluidIn == this.stillFluid || fluidIn == this.flowingFluid;
    }

    public static class Flowing extends BdFluid {

        {
            setDefaultState( getStateContainer().getBaseState().with( LEVEL_1_8, 7 ) );
        }

        public Flowing( FluidAttributes attributes ) {
            super( attributes );
        }

        protected void fillStateContainer( StateContainer.Builder<Fluid, IFluidState> builder ) {
            super.fillStateContainer( builder );
            builder.add( LEVEL_1_8 );
        }

        public int getLevel( IFluidState state ) {
            return state.get( LEVEL_1_8 );
        }

        public boolean isSource( IFluidState state ) {
            return false;
        }

    }

    public static class Source extends BdFluid {

        public Source( FluidAttributes attributes ) {
            super( attributes );
        }

        public int getLevel( IFluidState state ) {
            return 8;
        }

        public boolean isSource( IFluidState state ) {
            return true;
        }

    }

}
    /*

        @Override
        protected FluidAttributes createAttributes( Fluid fluid ) {

            return attributes;

            if ( fluid instanceof EmptyFluid )
                return net.minecraftforge.fluids.FluidAttributes.builder( "empty", null, null )
                     .vanillaColor().density( 0 ).temperature( 0 ).luminosity( 0 ).viscosity( 0 ).density( 0 ).build();
            if ( fluid instanceof WaterFluid )
                return net.minecraftforge.fluids.FluidAttributes.builder( "water",
                     new net.minecraft.util.ResourceLocation( "block/water_still" ),
                     new net.minecraft.util.ResourceLocation( "block/water_flow" ) )
                     .overlay( new net.minecraft.util.ResourceLocation( "block/water_overlay" ) )
                     .vanillaColor().block( () -> net.minecraft.block.Blocks.WATER ).build();
            if ( fluid instanceof LavaFluid )
                return net.minecraftforge.fluids.FluidAttributes.builder( "lava",
                     new net.minecraft.util.ResourceLocation( "block/lava_still" ),
                     new net.minecraft.util.ResourceLocation( "block/lava_flow" ) )
                     .block( () -> net.minecraft.block.Blocks.LAVA )
                     .vanillaColor().luminosity( 15 ).density( 3000 ).viscosity( 6000 ).temperature( 1300 ).build();
            throw new RuntimeException( "Mod fluids must override createAttributes." );
        }
    */
