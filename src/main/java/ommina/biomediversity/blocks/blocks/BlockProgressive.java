package ommina.biomediversity.blocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModBlocks;

import java.util.Random;

public class BlockProgressive extends Block {

    public static final String PREFIX = "progressive-";

    public static final IntegerProperty STAGE = ModBlockStateProperties.STAGE_1_12;

    public BlockProgressive( Properties properties, int defaultStage ) {
        super( properties );

        this.setDefaultState( this.stateContainer.getBaseState().with( this.getStageProperty(), defaultStage ) );
    }

    //region Overrides
    @Override
    public void randomTick( BlockState state, World world, BlockPos pos, Random random ) {

        int stage = getStage( state );

        if ( canBePromoted( stage, world, pos ) )
            promote( stage, world, pos );

    }

    @Override
    public int getWeakPower( BlockState state, IBlockReader blockAccess, BlockPos pos, Direction side ) {

        if ( canBePromoted( getStage( state ), (IWorldReader) blockAccess, pos ) )
            return 15;

        return 0;

    }

    @Override
    public boolean canProvidePower( BlockState state ) {
        return true;
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( STAGE );
    }
//endregion Overrides

    protected int getStage( BlockState state ) {
        return state.get( this.getStageProperty() );
    }

    public IntegerProperty getStageProperty() {
        return STAGE;
    }

    public BlockState withStage( int stage ) {
        return this.getDefaultState().with( this.getStageProperty(), stage );
    }

    private boolean canBePromoted( int stage, IWorldReader blockAccess, BlockPos pos ) {
        return hasVerticalMatch( stage, blockAccess, pos ) && hasHorizontalMatch( stage, blockAccess, pos );
    }

    private boolean hasVerticalMatch( int stage, IWorldReader world, BlockPos pos ) {

        for ( Direction direction : Direction.Plane.VERTICAL ) {

            BlockPos p = pos.offset( direction );

            if ( World.isYOutOfBounds( p.getY() ) )
                continue;

            BlockState s = world.getBlockState( p );

            if ( s.getBlock() instanceof BlockProgressive && getStage( s ) == stage )
                return true;

        }

        return false;

    }

    private boolean hasHorizontalMatch( int stage, IWorldReader world, BlockPos pos ) {

        for ( Direction direction : Direction.Plane.HORIZONTAL ) {

            BlockPos p = pos.offset( direction );

            if ( !World.isValid( p ) || !world.isBlockLoaded( p ) )
                continue;

            BlockState s = world.getBlockState( p );

            if ( s.getBlock() instanceof BlockProgressive && getStage( s ) == stage ||
                 (s.getBlock() == ModBlocks.NEUTRAL_BIOMETIC && stage == 1) ||
                 (s.getBlock() == ModBlocks.BYPRODUCT && stage == 1) )
                return true;

        }

        return false;

    }

    private void promote( int stage, World world, BlockPos pos ) {

        stage++;

        if ( stage < 1 || stage > 12 )
            return;

        world.setBlockState( pos, ForgeRegistries.BLOCKS.getValue( BiomeDiversity.getId( BlockProgressive.PREFIX + stage ) ).getDefaultState(), 3 );

    }

}
