package ommina.biomediversity.blocks.collector;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraftforge.client.model.animation.TileEntityRendererFast;
import net.minecraftforge.fluids.FluidStack;
import ommina.biomediversity.blocks.tile.RenderHelper;
import ommina.biomediversity.config.Constants;
import ommina.biomediversity.fluids.ModFluids;

import javax.vecmath.Point2f;

public class FastTesrCollector<T extends TileEntityCollector> extends TileEntityRendererFast<T> {

    private static final Point2f[] tubeLocation =
         {
              new Point2f( 2f, 4f ), //0
              new Point2f( 2f, 4f ),
              new Point2f( -4.5f / 16f, 23f / 16f ),    // 3 -> Cool
              new Point2f( 16.5f / 16f, 23f / 16f ),    // 4 -> Warm
              new Point2f( 2f, 4f ),
              new Point2f( 2f, 4f ),
              new Point2f( 2f, 4f ),
              new Point2f( 16.5f / 16f, -11.5f / 16f ), // 7 -> By-product
              new Point2f( 2f, 4f ),
              new Point2f( 2f, 4f ),
              new Point2f( 2f, 4f ),
              new Point2f( 2f, 4f )
         };


    //region Overrides
    @Override
    public boolean isGlobalRenderer( T te ) {
        return true;
    }

    @Override
    public void renderTileEntityFast( final T te, double x, double y, double z, final float partialTicks, final int destroyStage, final BufferBuilder buffer ) {

        final float BASE = 0.1f;
        final float MODEL_ELEMENT_HEIGHT = 37f / 16f;
        final float SIZE_OUTER = 3.75f / 16f;   // Tubes are square, so width and length are equal

        y -= 13f / 16f;

        //FluidStack fluid = te.getTank( TileEntityCollector.BYPRODUCT ).getFluid();

        for ( int i = 0; i <= 7; i++ ) {

            if ( tubeLocation[i].y != 4f ) {

                FluidStack fluid = new FluidStack( ModFluids.BYPRODUCT, 32000 );

                if ( !fluid.isEmpty() ) {

                    float height = BASE + (MODEL_ELEMENT_HEIGHT * ((float) fluid.getAmount() / (float) Constants.COLLECTOR_OUTER_TANK_CAPACITY));

                    RenderHelper.renderCube( buffer, x + tubeLocation[i].x, y, z + tubeLocation[i].y, SIZE_OUTER, height, SIZE_OUTER, fluid, RenderHelper.FACES_FLUID );

                }

            }
        }

    }
//endregion Overrides

}
