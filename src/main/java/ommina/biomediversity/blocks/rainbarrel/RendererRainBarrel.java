package ommina.biomediversity.blocks.rainbarrel;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fluids.FluidStack;
import ommina.biomediversity.rendering.RenderHelper;
import ommina.biomediversity.config.Config;

import java.util.EnumSet;

public class RendererRainBarrel extends TileEntityRenderer<TileEntityRainBarrel> {

    private static final float WIDTH = 14f / 16f;
    private static final float LENGTH = 14f / 16f;
    private static final float HEIGHT = 13.75f / 16f;

    public RendererRainBarrel( TileEntityRendererDispatcher tileEntityRendererDispatcher ) {
        super( tileEntityRendererDispatcher );
    }

    //region Overrides
    @Override
    public void render( TileEntityRainBarrel tile, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay ) {

        FluidStack fluid = tile.getFluid();

        if ( !fluid.isEmpty() ) {

            final float xTranslate = 1f / 16f;
            final float yTranslate = 1f / 16f;
            final float zTranslate = 1f / 16f;

            EnumSet<RenderHelper.Faces> faces = EnumSet.of( RenderHelper.Faces.TOP );

            float height = (HEIGHT * ((float) fluid.getAmount() / (float) Config.rainbarrelCapacity.get()));

            RenderHelper.renderCube( buffer, matrix, xTranslate, yTranslate, zTranslate, WIDTH, height, LENGTH, fluid, faces );

        }

    }
//endregion Overrides

}
