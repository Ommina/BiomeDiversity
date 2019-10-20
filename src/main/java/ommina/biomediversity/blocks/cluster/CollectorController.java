package ommina.biomediversity.blocks.cluster;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class CollectorController extends ClusterBlockTransparent implements IClusterController {

    public CollectorController() {

        super( Block.Properties.create( Material.ROCK ).harvestLevel( 2 ).harvestTool( ToolType.PICKAXE ).hardnessAndResistance( 10f ) );

    }

}
