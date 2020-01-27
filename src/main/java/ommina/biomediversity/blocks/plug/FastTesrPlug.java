package ommina.biomediversity.blocks.plug;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.tile.RenderHelper;

import javax.annotation.Nonnull;
import java.awt.*;

import static ommina.biomediversity.blocks.tile.RenderHelper.*;

public class FastTesrPlug<T extends TileEntityPlugBase> extends TileEntityRenderer<T> {

    private static final ResourceLocation INTERNAL_SPRITE = BiomeDiversity.getId( "block/cluster/cluster_glow_internal" );
    private static final ResourceLocation EXTERNAL_SPRITE = BiomeDiversity.getId( "block/cluster/cluster_glow_external" );

    private static final float WIDTH_FLUID = (16f - (1.001f * 2)) / 16f;
    private static final float LENGTH_FLUID = (16f - (1.001f * 2)) / 16f;
    private static final float HEIGHT_FLUID = 12f / 16f;

    private static final float HEIGHT_CONNECTION = 9f / 16f;
    private static final float[] COLOUR_DISCONNECTED = RenderHelper.getRGBA( new Color( 254, 0, 0, 255 ).getRGB() );
    private static final float[] COLOUR_CONNECTED = RenderHelper.getRGBA( new Color( 0, 200, 0, 255 ).getRGB() );

    public FastTesrPlug( final TileEntityRendererDispatcher tileEntityRendererDispatcher ) {
        super( tileEntityRendererDispatcher );
    }

    //region Overrides
    @Override
    public void render( @Nonnull T tile, float partialTick, @Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer renderer, int light, int overlayLight ) {

        final double offset = 1.001f / 16f;

        PlugRenderData renderData = tile.getPlugRenderData();

        if ( renderData.value > 0 ) {

            float height = (HEIGHT_FLUID * ((float) renderData.value / (float) renderData.maximum));

            //RenderHelper.renderCube( buffer, x + offset, y + offset, z + offset, WIDTH_FLUID, height, LENGTH_FLUID, renderData.sprite, renderData.colour, FACES_FLUID );

        }

        //y += 2f / 16f;

        final float side = 15f / 16f;

        float[] color = tile.isClusterComponentConnected() ? COLOUR_CONNECTED : COLOUR_DISCONNECTED;

        //renderLight( buffer, x, y, z, 1f / 16f / 2f, 1f / 16f / 2f, side, INTERNAL_SPRITE, color );
        //renderLight( buffer, x, y, z, 0, 1f / 16f, side, EXTERNAL_SPRITE, color );

    }

    //@Override
    //public void setRendererDispatcher( TileEntityRendererDispatcher p_147497_1_ ) {
    //    super.setRendererDispatcher( p_147497_1_ );
    //}
//endregion Overrides

    private static void renderLight( BufferBuilder buffer, double x, double y, double z, float offset, float size, float side, ResourceLocation spriteResource, float[] color ) {

        RenderHelper.renderCube( buffer, x + offset, y, z + offset, size, HEIGHT_CONNECTION, size, spriteResource, color, FACES_NORTHWEST );
        RenderHelper.renderCube( buffer, x + side, y, z + offset, size, HEIGHT_CONNECTION, size, spriteResource, color, FACES_NORTHEAST );
        RenderHelper.renderCube( buffer, x + offset, y, z + side, size, HEIGHT_CONNECTION, size, spriteResource, color, FACES_SOUTHWEST );
        RenderHelper.renderCube( buffer, x + side, y, z + side, size, HEIGHT_CONNECTION, size, spriteResource, color, FACES_SOUTHEAST );

    }

}


