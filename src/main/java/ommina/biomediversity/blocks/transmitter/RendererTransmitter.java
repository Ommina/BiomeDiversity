package ommina.biomediversity.blocks.transmitter;


import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fluids.FluidStack;
import ommina.biomediversity.blocks.tile.RenderHelper;
import ommina.biomediversity.config.Config;

import java.util.EnumSet;

public class RendererTransmitter extends TileEntityRenderer<TileEntityTransmitter> {

    private static final EnumSet<RenderHelper.Faces> faces = EnumSet.of( RenderHelper.Faces.TOP, RenderHelper.Faces.NORTH, RenderHelper.Faces.SOUTH, RenderHelper.Faces.WEST, RenderHelper.Faces.EAST );

    private static final float WIDTH = 14f / 16f;
    private static final float LENGTH = 14f / 16f;
    private static final float HEIGHT = 10f / 16f;

    public RendererTransmitter( final TileEntityRendererDispatcher tileEntityRendererDispatcher ) {
        super( tileEntityRendererDispatcher );
    }

    //region Overrides
    @Override
    public void render( TileEntityTransmitter tile, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay ) {

        FluidStack fluid = tile.getTank( 0 ).getFluid();

        if ( !fluid.isEmpty() ) {

            final float xTranslate = 1f / 16f;
            final float yTranslate = 1f / 16f;
            final float zTranslate = 1f / 16f;

            float height = (HEIGHT * ((float) fluid.getAmount() / (float) Config.transmitterCapacity.get()));

            RenderHelper.renderCube( buffer, matrix, xTranslate, yTranslate, zTranslate, WIDTH, height, LENGTH, fluid, faces );

        }

    }
//endregion Overrides

}
