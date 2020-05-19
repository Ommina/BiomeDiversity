package ommina.biomediversity.blocks.receiver;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fluids.FluidStack;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.rendering.RenderHelper;
import ommina.biomediversity.config.Config;

import static ommina.biomediversity.rendering.RenderHelper.*;

public class RendererReceiver extends TileEntityRenderer<TileEntityReceiver> {

    private static final float WIDTH_FLUID = 14f / 16f;
    private static final float LENGTH_FLUID = 14f / 16f;
    private static final float HEIGHT_FLUID = 24f / 16f;

    private static final float HEIGHT_GLOWBAR = 21f / 16f;

    private static TextureAtlasSprite spriteInternal;
    private static TextureAtlasSprite spriteExternal;

    public RendererReceiver( final TileEntityRendererDispatcher tileEntityRendererDispatcher ) {
        super( tileEntityRendererDispatcher );
    }

    //region Overrides
    @Override
    public void render( TileEntityReceiver tile, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay ) {

        if ( spriteInternal == null || spriteExternal == null ) {
            spriteInternal = getSprite( BiomeDiversity.getId( "block/cluster/cluster_glow_internal" ) );
            spriteExternal = getSprite( BiomeDiversity.getId( "block/cluster/cluster_glow_external" ) );
        } // Attempting to use getAtlasSprite much earlier throws exceptions.  So while this null check every frame isn't ideal, it is hoped it is better for performance than refetching the sprite each frame instead

        FluidStack fluid = tile.getTank( 0 ).getFluid();

        if ( !fluid.isEmpty() ) {

            final float xTranslate = 1f / 16f;
            final float yTranslate = 1f / 16f;
            final float zTranslate = 1f / 16f;

            float height = (HEIGHT_FLUID * ((float) fluid.getAmount() / (float) Config.transmitterCapacity.get())); // Yes, this IS supposed to be TransmitterCapacity (Receiver and Transmitter must share capacities)

            RenderHelper.renderCube( buffer, matrix, xTranslate, yTranslate, zTranslate, WIDTH_FLUID, height, LENGTH_FLUID, fluid, FACES_FLUID );

        }

        float[] color = tile.getGlowbarColour();

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
        final float yTranslate = 3f / 16f;

        RenderHelper.renderCube( buffer, matrix, offset, yTranslate, offset, size, HEIGHT_GLOWBAR, size, sprite, color, FACES_NORTHWEST );
        RenderHelper.renderCube( buffer, matrix, sideTranslate, yTranslate, offset, size, HEIGHT_GLOWBAR, size, sprite, color, FACES_NORTHEAST );
        RenderHelper.renderCube( buffer, matrix, offset, yTranslate, sideTranslate, size, HEIGHT_GLOWBAR, size, sprite, color, FACES_SOUTHWEST );
        RenderHelper.renderCube( buffer, matrix, sideTranslate, yTranslate, sideTranslate, size, HEIGHT_GLOWBAR, size, sprite, color, FACES_SOUTHEAST );

    }

}
