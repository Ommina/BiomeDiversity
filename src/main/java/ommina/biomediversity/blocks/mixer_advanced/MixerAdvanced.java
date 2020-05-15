package ommina.biomediversity.blocks.mixer_advanced;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import ommina.biomediversity.blocks.BlockTileEntity;
import ommina.biomediversity.config.Constants;

public class MixerAdvanced extends BlockTileEntity<TileEntityMixerAdvanced> {

    private static final float INSET = 4f;//1f / 16f * 4f;

    public static final VoxelShape SHAPE = Block.makeCuboidShape( INSET, INSET, INSET, 16f - INSET, 16f - INSET, 16f - INSET );

    public MixerAdvanced() {
        super( Block.Properties.create( Material.ROCK ).harvestLevel( 2 ).harvestTool( ToolType.PICKAXE ).hardnessAndResistance( Constants.DEFAULT_TILE_ENTITY_HARDNESS ).notSolid() );
    }

    //region Overrides
    @Override
    public TileEntity createTileEntity( BlockState state, IBlockReader world ) {
        return new TileEntityMixerAdvanced();
    }

    @Override
    public BlockRenderType getRenderType( BlockState state ) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context ) {
        return SHAPE;
    }

    @Override
    public VoxelShape getRaytraceShape( BlockState state, IBlockReader worldIn, BlockPos pos ) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return this.blocksMovement ? state.getShape(worldIn, pos) : VoxelShapes.empty();
    }

    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return state.getShape(worldIn, pos);
    }



}
