package ommina.biomediversity.blocks.receiver;


import net.minecraft.client.renderer.BufferBuilder;
import net.minecraftforge.client.model.animation.TileEntityRendererFast;
import net.minecraftforge.fluids.FluidStack;
import ommina.biomediversity.blocks.tile.RenderHelper;
import ommina.biomediversity.config.Config;

import java.util.EnumSet;

public class FastTesrReceiver<T extends TileEntityReceiver> extends TileEntityRendererFast<T> {

    private static final EnumSet<RenderHelper.Faces> faces = EnumSet.of( RenderHelper.Faces.TOP, RenderHelper.Faces.NORTH, RenderHelper.Faces.SOUTH, RenderHelper.Faces.WEST, RenderHelper.Faces.EAST );
    private static final float WIDTH = 14f / 16f;
    private static final float LENGTH = 14f / 16f;
    private static final float HEIGHT = 26f / 16f;

    @Override
    public void renderTileEntityFast( TileEntityReceiver te, double x, double y, double z, float partialTicks, int destroyStage, BufferBuilder buffer ) {

        final double offset = 1f / 16f;

        x += offset;
        y += offset;
        z += offset;

        FluidStack fluid = te.getTank( 0 ).getFluid();

        if ( !fluid.isEmpty() ) {

            float height = (HEIGHT * ((float) fluid.getAmount() / (float) Config.transmitterCapacity.get())); // Yes, this IS supposed to be TransmitterCapacity (Receiver and Transmitter must share capacities)

            RenderHelper.renderFluidCube( buffer, x, y, z, WIDTH, height, LENGTH, fluid, faces );

        }

    }

}
