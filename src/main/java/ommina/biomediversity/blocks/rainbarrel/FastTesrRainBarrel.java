
package ommina.biomediversity.blocks.rainbarrel;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraftforge.client.model.animation.TileEntityRendererFast;
import net.minecraftforge.fluids.FluidStack;
import ommina.biomediversity.blocks.tile.RenderHelper;
import ommina.biomediversity.config.Config;

import java.util.EnumSet;


//public class TreeTapTileEntityRenderer<T extends TreeTapTileEntity> extends TileEntityRendererFast<T>

public class FastTesrRainBarrel<T extends TileEntityRainBarrel> extends TileEntityRendererFast<T> {

    @Override
    public void renderTileEntityFast( TileEntityRainBarrel te, double x, double y, double z, float partialTicks, int destroyStage, BufferBuilder buffer ) {

        final float BASE = 0.1f;
        float HEIGHT = 13f / 16f;

        float low = 1f / 16f;
        float high = low * 15f;

        FluidStack fluid = te.getFluid();

        if ( !fluid.isEmpty() ) {

            EnumSet<RenderHelper.Faces> faces = EnumSet.of( RenderHelper.Faces.TOP );

            float posY = BASE + (HEIGHT * ((float) fluid.getAmount() / (float) Config.rainbarrelCapacity.get()));

            RenderHelper.renderFluidCube( buffer, x, y, z, 1f / 16f, posY, high, fluid, faces );

        }

    }

}
