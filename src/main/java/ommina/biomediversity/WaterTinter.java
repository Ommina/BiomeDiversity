package ommina.biomediversity;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;

import javax.annotation.Nullable;
import java.awt.*;

public class WaterTinter implements IBlockColor {

    @Override
    public int getColor( BlockState blockState, @Nullable IEnviromentBlockReader iEnviromentBlockReader, @Nullable BlockPos blockPos, int i ) {
        return (new Color( 200, 0, 0 ).getRGB());
    }

}
