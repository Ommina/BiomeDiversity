package ommina.biomediversity.fluids;

import net.minecraft.block.BlockState;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModBlocks;

public class BdSourceFluid extends ForgeFlowingFluid.Source {

    protected BdSourceFluid( Properties properties ) {
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
    protected void flowInto( IWorld worldIn, BlockPos pos, BlockState blockStateIn, Direction direction, IFluidState fluidStateIn ) {

        if ( blockStateIn.getBlock() instanceof ILiquidContainer ) {

            ((ILiquidContainer) blockStateIn.getBlock()).receiveFluid( worldIn, pos, blockStateIn, fluidStateIn );

        } else {

            BiomeDiversity.LOGGER.info( "SPosition: " + pos.toString() );
            BiomeDiversity.LOGGER.info( "SDirection: " + direction.toString() );
            BiomeDiversity.LOGGER.info( "SOffset: " + worldIn.getBlockState( pos.offset( direction ) ).getBlock().toString() );
            BiomeDiversity.LOGGER.info( "SBlockStateIn: " + blockStateIn.toString() );
            //BiomeDiversity.LOGGER.info( "FluidStateIn: " + fluidStateIn.getFluid().toString() );

            if ( worldIn.getBlockState( pos.offset( direction ) ).getBlock() == ModBlocks.BYPRODUCT ) {
                worldIn.setBlockState( pos, ModBlocks.ORINOCITE_ORE.getDefaultState(), 3 );
                return;
            }

            //if ( //!blockStateIn.isAir() &&
            //     ((blockStateIn.getBlock() == ModBlocks.BYPRODUCT && worldIn.getBlockState( pos.offset( direction ) ).getBlock() == ModBlocks.MOLTEN_ORINOCITE) ||
            //          (blockStateIn.getBlock() == ModBlocks.MOLTEN_ORINOCITE && worldIn.getBlockState( pos.offset( direction ) ).getBlock() == ModBlocks.BYPRODUCT)) ) {
            //    worldIn.setBlockState( pos, ModBlocks.ORINOCITE_ORE.getDefaultState(), 3 );
            //    return;
            // }

            worldIn.setBlockState( pos, fluidStateIn.getBlockState(), 3 );

        }

    }

    @Override
    protected boolean canFlow( IBlockReader worldIn, BlockPos fromPos, BlockState fromBlockState, Direction direction, BlockPos toPos, BlockState toBlockState, IFluidState toFluidState, Fluid fluidIn ) {

        BiomeDiversity.LOGGER.info( "SfromPos: " + fromPos.toString() + ", toPos: " + toPos.toString() + ", fromBlockState: " + fromBlockState.toString() + ", toBlockState: " + toBlockState.toString() + " toFluidState: " + toFluidState.toString() + " fluidIn: " + fluidIn.toString() );

        if ( fromBlockState.getBlock() == ModBlocks.MOLTEN_ORINOCITE && toBlockState.getBlock() == ModBlocks.BYPRODUCT )
            return true;

        return super.canFlow( worldIn, fromPos, fromBlockState, direction, toPos, toBlockState, toFluidState, fluidIn );

    }


}
