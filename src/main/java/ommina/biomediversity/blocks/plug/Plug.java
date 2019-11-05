package ommina.biomediversity.blocks.plug;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import ommina.biomediversity.blocks.BlockTileEntity;

public class Plug extends BlockTileEntity<TileEntityPlug> {

    public Plug() {

        super( Block.Properties.create( Material.ROCK ).harvestLevel( 2 ).harvestTool( ToolType.PICKAXE ).hardnessAndResistance( 10f ) );

    }

//region Overrides
    @Override
    public TileEntity createTileEntity( BlockState state, IBlockReader world ) {
        return new TileEntityPlug();
    }

    @Override
    public BlockRenderType getRenderType( BlockState state ) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
//endregion Overrides

}
