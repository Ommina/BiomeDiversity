package ommina.biomediversity.fluids;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.sounds.ModSounds;

public abstract class BdFlowingFluid extends ForgeFlowingFluid {

    protected BdFlowingFluid( Properties properties ) {
        super( properties );
    }

    //region Overrides

    /**
     * @param worldIn      World
     * @param pos          BlockPos the flowing fluid is entering (the flow INTO portion, pos is the INTO)
     * @param blockStateIn Block at pos -- seems to be called twice, once with AIR (or something replaceable?) and once with the fluid
     * @param direction    Direction the fluid is flowing
     * @param fluidStateIn FluidState of the fluid flowing into the block
     */
    @Override
    protected void flowInto( IWorld worldIn, BlockPos pos, BlockState blockStateIn, Direction direction, FluidState fluidStateIn ) {

        if ( blockStateIn.getBlock() instanceof ILiquidContainer ) {

            ((ILiquidContainer) blockStateIn.getBlock()).receiveFluid( worldIn, pos, blockStateIn, fluidStateIn );

        } else {

            //BiomeDiversity.LOGGER.info( "Position: " + pos.toString() );
            //BiomeDiversity.LOGGER.info( "Direction: " + direction.toString() );
            //BiomeDiversity.LOGGER.info( "Offset: " + worldIn.getBlockState( pos.offset( direction ) ).getBlock().toString() );
            //BiomeDiversity.LOGGER.info( "BlockStateIn: " + blockStateIn.toString() );
            //BiomeDiversity.LOGGER.info( "FluidStateIn: " + fluidStateIn.getFluid().getRegistryName().toString() );

            if ( fluidStateIn.getBlockState().getBlock() == ModBlocks.NEUTRAL_BIOMETIC && worldIn.getBlockState( pos.offset( direction ) ).getBlock() == ModBlocks.BYPRODUCT ) {
                worldIn.setBlockState( pos, ModBlocks.PROGRESSIVE1.getDefaultState(), 3 );
                worldIn.playSound( null, pos, ModSounds.FLUID_HARDENING, SoundCategory.BLOCKS, 1.0f, 1.0f );
                return;
            } else if ( worldIn.getBlockState( pos ).getBlock() instanceof FlowingFluidBlock ) {
                return;
            }

            worldIn.setBlockState( pos, fluidStateIn.getBlockState(), 3 );

        }

    }


    @Override
    protected boolean canFlow( IBlockReader worldIn, BlockPos fromPos, BlockState fromBlockState, Direction direction, BlockPos toPos, BlockState toBlockState, FluidState toFluidState, Fluid fluidIn ) {

        //BiomeDiversity.LOGGER.info( "fromPos: " + fromPos.toString() + ", toPos: " + toPos.toString() + ", fromBlockState: " + fromBlockState.toString() + ", toBlockState: " + toBlockState.toString() + " toFluidState: " + toFluidState.toString() + " fluidIn: " + fluidIn.toString() );

        if ( fromBlockState.getBlock() == ModBlocks.NEUTRAL_BIOMETIC && toBlockState.getBlock() == ModBlocks.BYPRODUCT )
            return true;

        return super.canFlow( worldIn, fromPos, fromBlockState, direction, toPos, toBlockState, toFluidState, fluidIn );

    }


//endregion Overrides

    public static class Flowing extends BdFlowingFluid {

        public Flowing( Properties properties ) {
            super( properties );
            setDefaultState( getStateContainer().getBaseState().with( LEVEL_1_8, 7 ) );
        }

        //region Overrides
        @Override
        protected void fillStateContainer( StateContainer.Builder<Fluid, FluidState> builder ) {
            super.fillStateContainer( builder );
            builder.add( LEVEL_1_8 );
        }

        @Override
        public boolean isSource( FluidState state ) {
            return false;
        }

        @Override
        public int getLevel( FluidState state ) {
            return state.get( LEVEL_1_8 );
        }
//endregion Overrides

    }

    public static class Source extends BdFlowingFluid {

        public Source( Properties properties ) {
            super( properties );
        }

        //region Overrides
        @Override
        public boolean isSource( FluidState state ) {
            return true;
        }

        @Override
        public int getLevel( FluidState state ) {
            return 8;
        }
//endregion Overrides

    }

}
