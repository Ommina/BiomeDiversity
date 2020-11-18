package ommina.biomediversity.blocks.cluster;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public interface IClusterController {

    boolean checkMultiblock( IWorld world, BlockPos pos );

    void defineMultiblock();

}
