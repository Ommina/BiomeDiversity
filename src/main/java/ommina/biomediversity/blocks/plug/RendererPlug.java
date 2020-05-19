package ommina.biomediversity.blocks.plug;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.rendering.RenderHelper;

import java.awt.*;

import static ommina.biomediversity.rendering.RenderHelper.*;

public class RendererPlug extends TileEntityRenderer<TileEntityPlugBase> {

    private static final float WIDTH_FLUID = (16f - (1.001f * 2)) / 16f;
    private static final float LENGTH_FLUID = (16f - (1.001f * 2)) / 16f;
    private static final float HEIGHT_FLUID = 12f / 16f;

    private static final float HEIGHT_GLOWBAR = 9f / 16f;

    private static final float[] COLOUR_DISCONNECTED = RenderHelper.getRGBA( new Color( 254, 0, 0, 255 ).getRGB() );
    private static final float[] COLOUR_CONNECTED = RenderHelper.getRGBA( new Color( 0, 200, 0, 255 ).getRGB() );

    private static TextureAtlasSprite spriteInternal;
    private static TextureAtlasSprite spriteExternal;

    public RendererPlug( final TileEntityRendererDispatcher tileEntityRendererDispatcher ) {
        super( tileEntityRendererDispatcher );
    }

    //region Overrides
    @Override
    public void render( TileEntityPlugBase tile, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay ) {

        if ( spriteInternal == null || spriteExternal == null ) {
            spriteInternal = getSprite( BiomeDiversity.getId( "block/cluster/cluster_glow_internal" ) );
            spriteExternal = getSprite( BiomeDiversity.getId( "block/cluster/cluster_glow_external" ) );
        }

        PlugRenderData renderData = tile.getPlugRenderData();

        if ( renderData.value > 0 ) {

            final float xTranslate = 1f / 16f;
            final float yTranslate = 1f / 16f;
            final float zTranslate = 1f / 16f;

            float height = (HEIGHT_FLUID * ((float) renderData.value / (float) renderData.maximum));

            RenderHelper.renderCube( buffer, matrix, xTranslate, yTranslate, zTranslate, WIDTH_FLUID, height, LENGTH_FLUID, renderData.spriteLocation, renderData.colour, FACES_FLUID );

        }

        float[] color = tile.isClusterComponentConnected() ? COLOUR_CONNECTED : COLOUR_DISCONNECTED;

        final float interalGlowSize = 1f / 16f / 2f;
        final float externalGlowSize = 1f / 16f;

        final float internalTranslateOffset = 1f / 16f / 2f;
        final float externalTranslateOffset = 0;

        renderLight( buffer, matrix, internalTranslateOffset, interalGlowSize, spriteInternal, color );
        renderLight( buffer, matrix, externalTranslateOffset, externalGlowSize, spriteExternal, color );

    }
//endregion Overrides

    private static void renderLight( IRenderTypeBuffer buffer, MatrixStack matrix, float offset, float size, TextureAtlasSprite sprite, float[] color ) {

        final float sideTranslate = 15f / 16f;
        final float yTranslate = 2f / 16f;

        RenderHelper.renderCube( buffer, matrix, offset, yTranslate, offset, size, HEIGHT_GLOWBAR, size, sprite, color, FACES_NORTHWEST );
        RenderHelper.renderCube( buffer, matrix, sideTranslate, yTranslate, offset, size, HEIGHT_GLOWBAR, size, sprite, color, FACES_NORTHEAST );
        RenderHelper.renderCube( buffer, matrix, offset, yTranslate, sideTranslate, size, HEIGHT_GLOWBAR, size, sprite, color, FACES_SOUTHWEST );
        RenderHelper.renderCube( buffer, matrix, sideTranslate, yTranslate, sideTranslate, size, HEIGHT_GLOWBAR, size, sprite, color, FACES_SOUTHEAST );

    }

}
