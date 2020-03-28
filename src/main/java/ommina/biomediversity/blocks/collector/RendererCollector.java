package ommina.biomediversity.blocks.collector;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fluids.FluidStack;
import ommina.biomediversity.blocks.tile.RenderHelper;
import ommina.biomediversity.config.Constants;

import java.awt.geom.Point2D;

public class RendererCollector extends TileEntityRenderer<TileEntityCollector> {

    private static final float MODEL_ELEMENT_HEIGHT = 37f / 16f;

    private static final float SIZE_OUTER = 3.95f / 16f;   // Tubes are square, so width and length are equal

    private static final Point2D.Float[] tubeLocation =
         {
              Tubes.Unused0.getRendererLocation(),
              Tubes.Unused1.getRendererLocation(),
              Tubes.Unused2.getRendererLocation(),
              Tubes.Cool.getRendererLocation(),
              Tubes.Warm.getRendererLocation(),
              Tubes.Unused5.getRendererLocation(),
              Tubes.Unused6.getRendererLocation(),
              Tubes.Byproduct.getRendererLocation(),
              Tubes.Unused8.getRendererLocation(),
              Tubes.Unused9.getRendererLocation(),
              Tubes.Unused10.getRendererLocation(),
              Tubes.Unused11.getRendererLocation(),
         };

    public RendererCollector( final TileEntityRendererDispatcher tileEntityRendererDispatcher ) {
        super( tileEntityRendererDispatcher );
    }

    //region Overrides
    @Override
    public void render( TileEntityCollector tile, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay ) {

        final float xTranslate = 0;
        final float yTranslate = -(13f / 16f);
        final float zTranslate = 0;

        for ( int i = 0; i <= 7; i++ ) {

            FluidStack fluid = tile.getTank( i ).getFluid();

            if ( !fluid.isEmpty() ) {

                float height = (MODEL_ELEMENT_HEIGHT * ((float) fluid.getAmount() / (float) Constants.COLLECTOR_OUTER_TANK_CAPACITY));

                RenderHelper.renderCube( buffer, matrix, xTranslate + tubeLocation[i].x, yTranslate, zTranslate + tubeLocation[i].y, SIZE_OUTER, height, SIZE_OUTER, fluid, RenderHelper.FACES_FLUID );

            }

        }

    }

    @Override
    public boolean isGlobalRenderer( final TileEntityCollector te ) {
        return true;
    }
//endregion Overrides

}
