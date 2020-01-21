package ommina.biomediversity.blocks.collector;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import ommina.biomediversity.blocks.tile.RenderHelper;
import ommina.biomediversity.config.Constants;

import javax.annotation.Nonnull;
import java.awt.geom.Point2D;

public class FastTesrCollector<T extends TileEntityCollector> extends TileEntityRenderer<T> {

    private static final float X_OFFSET = 0;
    private static final float Z_OFFSET = 0;

    private static final Point2D.Float[] tubeLocation =
         {
              Tubes.Unused0.getTesrLocation(),
              Tubes.Unused1.getTesrLocation(),
              Tubes.Unused2.getTesrLocation(),
              Tubes.Cool.getTesrLocation(),
              Tubes.Warm.getTesrLocation(),
              Tubes.Unused5.getTesrLocation(),
              Tubes.Unused6.getTesrLocation(),
              Tubes.Byproduct.getTesrLocation(),
              Tubes.Unused8.getTesrLocation(),
              Tubes.Unused9.getTesrLocation(),
              Tubes.Unused10.getTesrLocation(),
              Tubes.Unused11.getTesrLocation(),
         };

    public FastTesrCollector( final TileEntityRendererDispatcher tileEntityRendererDispatcher ) {
        super( tileEntityRendererDispatcher );
    }

    //region Overrides
    @Override
    public void render( @Nonnull T tile, float partialTick, @Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer renderer, int light, int overlayLight ) {

        final float BASE = 0.1f;
        final float MODEL_ELEMENT_HEIGHT = 37f / 16f;
        final float SIZE_OUTER = 3.95f / 16f;   // Tubes are square, so width and length are equal

        BlockPos pos = tile.getPos();

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        y -= 13f / 16f;

        IVertexBuilder buffer = renderer.getBuffer( RenderType.translucent() );

        for ( int i = 0; i <= 7; i++ ) {

            FluidStack fluid = tile.getTank( i ).getFluid();

            if ( !fluid.isEmpty() ) {

                float height = BASE + (MODEL_ELEMENT_HEIGHT * ((float) fluid.getAmount() / (float) Constants.COLLECTOR_OUTER_TANK_CAPACITY));

                RenderHelper.renderCube( buffer, x + tubeLocation[i].x, y, z + tubeLocation[i].y, SIZE_OUTER, height, SIZE_OUTER, fluid, RenderHelper.FACES_FLUID );

            }

        }

    }

    @Override
    public boolean isGlobalRenderer( final TileEntityCollector te ) {
        return true;
    }

//endregion Overrides

}
