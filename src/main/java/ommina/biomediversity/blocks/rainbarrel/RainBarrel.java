package ommina.biomediversity.blocks.rainbarrel;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import ommina.biomediversity.blocks.BlockTileEntity;
import ommina.biomediversity.config.Constants;

public class RainBarrel extends BlockTileEntity<TileEntityRainBarrel> {

    // private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB( 0f, 0f, 0f, 1.0f, 15f / 16f, 1.0f );

    public RainBarrel() {
        super( Block.Properties.create( Material.ROCK ).harvestLevel( 2 ).harvestTool( ToolType.PICKAXE ).hardnessAndResistance( Constants.DEFAULT_TILE_ENTITY_HARDNESS ) );
    }

    //region Overrides
    @Override
    public TileEntity createTileEntity( BlockState state, IBlockReader world ) {
        return new TileEntityRainBarrel();
    }

    @Override
    public BlockRenderType getRenderType( BlockState state ) {
        return BlockRenderType.MODEL;
    }

    //@Override
    //public BlockRenderLayer getRenderLayer() {
    //    return BlockRenderLayer.CUTOUT;
    //}
//endregion Overrides

}
