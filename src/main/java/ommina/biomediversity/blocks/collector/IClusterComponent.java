package ommina.biomediversity.blocks.collector;

import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public interface IClusterComponent {

    @Nullable
    BlockPos getCollectorPos();

    boolean isClusterComponentConnected();

}
