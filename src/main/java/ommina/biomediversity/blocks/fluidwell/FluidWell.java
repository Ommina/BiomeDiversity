package ommina.biomediversity.blocks.fluidwell;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import ommina.biomediversity.blocks.BlockTileEntity;

public class FluidWell extends BlockTileEntity<TileEntityFluidWell> {

    public FluidWell() {
        super( Block.Properties.create( Material.ROCK ).harvestLevel( 0 ).harvestTool( ToolType.PICKAXE ).hardnessAndResistance( 1.0f ) );
    }

    //region Overrides
    @Override
    public TileEntity createTileEntity( BlockState state, IBlockReader world ) {
        return new TileEntityFluidWell();
    }

    @Override
    public BlockRenderType getRenderType( BlockState state ) {
        return BlockRenderType.MODEL;
    }

}