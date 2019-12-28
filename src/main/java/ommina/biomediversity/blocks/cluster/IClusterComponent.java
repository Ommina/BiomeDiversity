package ommina.biomediversity.blocks.cluster;

import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public interface IClusterComponent {

    @Nullable
    BlockPos getCollectorPos();

    boolean isClusterComponentConnected();

    void invalidateCollector();

    void registerSelf();

}
