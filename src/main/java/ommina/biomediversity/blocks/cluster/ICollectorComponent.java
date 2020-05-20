package ommina.biomediversity.blocks.cluster;

import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public interface ICollectorComponent {

    @Nullable
    BlockPos getCollectorPos();

    boolean isClusterComponentConnected();

    void invalidateCollector();

    void registerSelf();

}
