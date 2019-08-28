package ommina.biomediversity.blocks.crops;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ommina.biomediversity.items.ModItems;

public class PomegranateBlock extends CropsBlock {

    private static final VoxelShape[] SHAPES = new VoxelShape[]{ Block.makeCuboidShape( 0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D ), Block.makeCuboidShape( 0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D ), Block.makeCuboidShape( 0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D ), Block.makeCuboidShape( 0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D ), Block.makeCuboidShape( 0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D ), Block.makeCuboidShape( 0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D ), Block.makeCuboidShape( 0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D ), Block.makeCuboidShape( 0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D ) };

    public PomegranateBlock( Block.Properties builder ) {
        super( builder );
    }

    @OnlyIn( Dist.CLIENT )
    protected IItemProvider getSeedsItem() {
        return ModItems.POMEGRANATE_SEEDS;
    }

    public VoxelShape getShape( BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context ) {
        return SHAPES[state.get( this.getAgeProperty() )];
    }

    protected boolean isValidGround( BlockState state, IBlockReader worldIn, BlockPos pos ) {

        return (state.getBlock() == Blocks.FARMLAND || state.getBlock() == Blocks.GRASS_BLOCK || state.getBlock() == Blocks.DIRT);
    }

    protected int getBonemealAgeIncrease( World worldIn ) {
        return MathHelper.nextInt( worldIn.rand, 1, 2 );
    }

}
