package ommina.biomediversity.blocks.cluster;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IClusterController {

    boolean checkMultiblock( World world, BlockPos pos );

    void defineMultiblock();

}
