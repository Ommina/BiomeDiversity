package ommina.biomediversity.blocks.transmitter;


import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fluids.FluidStack;
import ommina.biomediversity.blocks.tile.RenderHelper;
import ommina.biomediversity.config.Config;

import javax.annotation.Nonnull;
import java.util.EnumSet;

public class FastTesrTransmitter<T extends TileEntityTransmitter> extends TileEntityRenderer<T> {

    private static final EnumSet<RenderHelper.Faces> faces = EnumSet.of( RenderHelper.Faces.TOP, RenderHelper.Faces.NORTH, RenderHelper.Faces.SOUTH, RenderHelper.Faces.WEST, RenderHelper.Faces.EAST );
    private static final float WIDTH = 14f / 16f;
    private static final float LENGTH = 14f / 16f;
    private static final float HEIGHT = 10f / 16f;

    public FastTesrTransmitter( final TileEntityRendererDispatcher tileEntityRendererDispatcher ) {
        super( tileEntityRendererDispatcher );
    }

//region Overrides
    @Override
    public void render( @Nonnull T tile, float partialTick, @Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer renderer, int light, int overlayLight ) {

        final double offset = 1f / 16f;

        //x += offset;
        //y += offset;
        //z += offset;

        FluidStack fluid = tile.getTank( 0 ).getFluid();

        if ( !fluid.isEmpty() ) {

            float height = (HEIGHT * ((float) fluid.getAmount() / (float) Config.transmitterCapacity.get()));

            //RenderHelper.renderCube( buffer, x, y, z, WIDTH, height, LENGTH, fluid, faces );

        }

    }
//endregion Overrides

}
