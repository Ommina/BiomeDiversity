package ommina.biomediversity.blocks.rainbarrel;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fluids.FluidStack;
import ommina.biomediversity.blocks.tile.RenderHelper;
import ommina.biomediversity.config.Config;

import javax.annotation.Nonnull;
import java.util.EnumSet;

public class FastTesrRainBarrel<T extends TileEntityRainBarrel> extends TileEntityRenderer<T> {

    public FastTesrRainBarrel( final TileEntityRendererDispatcher tileEntityRendererDispatcher ) {
        super( tileEntityRendererDispatcher );
    }

    //region Overrides
    @Override
    public void render( @Nonnull T tile, float partialTick, @Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer renderer, int light, int overlayLight ) {

        final float BASE = 0.1f;
        float HEIGHT = 13f / 16f;

        float low = 1f / 16f;
        float high = low * 15f;

        FluidStack fluid = tile.getFluid();

        if ( !fluid.isEmpty() ) {

            EnumSet<RenderHelper.Faces> faces = EnumSet.of( RenderHelper.Faces.TOP );

            float posY = BASE + (HEIGHT * ((float) fluid.getAmount() / (float) Config.rainbarrelCapacity.get()));

            //RenderHelper.renderCube( buffer, x, y, z, 1f / 16f, posY, high, fluid, faces );

        }

    }
//endregion Overrides

}
