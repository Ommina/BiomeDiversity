package ommina.biomediversity.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.tile.RenderHelper;

import javax.annotation.Nullable;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static org.lwjgl.opengl.GL11.GL_LINES;

@OnlyIn( Dist.CLIENT )
public abstract class Control extends AbstractGui {

    public static final TextureAtlasSprite OVERLAY_SPRITE = RenderHelper.getSprite( BiomeDiversity.getId( "gui/overlay" ) );
    public static final ResourceLocation OVERLAY_RESOURCE = BiomeDiversity.getId( "textures/gui/overlay.png" );

    public static final float[] BACKGROUND_COLOUR = RenderHelper.getRGBA( new Color( 139, 139, 139, 255 ).getRGB() );
    public static final float[] HIGHLIGHT_COLOUR = RenderHelper.getRGBA( Color.white.getRGB() );
    public static final float[] LOWLIGHT_COLOUR = RenderHelper.getRGBA( new Color( 55, 55, 55, 255 ).getRGB() );

    protected static final NumberFormat format = NumberFormat.getInstance( Locale.getDefault() );
    protected static FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;

    protected Point position = new Point( 0, 0 );

    protected boolean hasBorder = true;
    protected int height;
    protected int width;

    public Control( Size size ) {

        this.height = size.height;
        this.width = size.width;

    }

    public static class Sizes {

        public static final Size EMPTY = new Size( 0, 0 );
        public static final Size LOW_HIGH = new Size( 8, 53 );
        public static final Size RF_GAUGE = new Size( 8, 52 );
        public static final Size SPRITE = new Size( 16, 16 );
        public static final Size LOG_GAUGE = new Size( 16, 16 );
        public static final Size TANK = new Size( 16, 52 );
        public static final Size TEMPERATURE = new Size( 17, 53 );

    }

/*

    public static void drawSprite( float x, float y, int minU, int minV, int maxU, int maxV ) {

        final float f = 1f / 256f;

        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();

        bufferbuilder.begin( 7, DefaultVertexFormats.POSITION_TEX );
        bufferbuilder.pos( x, y + maxV, 1 ).tex( minU * f, (minV + maxV) * f ).endVertex();
        bufferbuilder.pos( x + maxU, y + maxV, 1 ).tex( (minU + maxU) * f, (minV + maxV) * f ).endVertex();
        bufferbuilder.pos( x + maxU, y, 1 ).tex( (minU + maxU) * f, minV * f ).endVertex();
        bufferbuilder.pos( x, y, 1 ).tex( minU * f, minV * f ).endVertex();
        tessellator.draw();

    }

*/

    public static void fill( double x1, double y1, double x2, double y2, int colour ) {

        if ( x1 < x2 ) {
            double i = x1;
            x1 = x2;
            x2 = i;
        }

        if ( y1 < y2 ) {
            double j = y1;
            y1 = y2;
            y2 = j;
        }

        float[] f = RenderHelper.getRGBA( colour );

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture();
        GlStateManager.blendFuncSeparate( GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO );

        bufferbuilder.begin( 7, DefaultVertexFormats.POSITION_COLOR );
        bufferbuilder.pos( x1, y2, 0.0D ).color( f[0], f[1], f[2], f[3] ).endVertex();
        bufferbuilder.pos( x2, y2, 0.0D ).color( f[0], f[1], f[2], f[3] ).endVertex();
        bufferbuilder.pos( x2, y1, 0.0D ).color( f[0], f[1], f[2], f[3] ).endVertex();
        bufferbuilder.pos( x1, y1, 0.0D ).color( f[0], f[1], f[2], f[3] ).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture();
        GlStateManager.disableBlend();
    }


    public static void drawLine( double x1, double y1, double x2, double y2, int colour ) {

        float[] f = RenderHelper.getRGBA( colour );

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture();
        GlStateManager.blendFuncSeparate( GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO );

        bufferbuilder.begin( GL_LINES, DefaultVertexFormats.POSITION_COLOR );
        bufferbuilder.pos( x1, y1, 0.0D ).color( f[0], f[1], f[2], f[3] ).endVertex();
        bufferbuilder.pos( x2, y2, 0.0D ).color( f[0], f[1], f[2], f[3] ).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture();
        GlStateManager.disableBlend();

    }

    public static void drawSprite( int x, int y, int offset, int width, int height, TextureAtlasSprite sprite ) {
        innerBlit( x, x + width, y, y + height, offset, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV() );
    }

    public static void drawSprite( float f, float x, float y, int minU, int minV, int maxU, int maxV ) {

        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();

        bufferbuilder.begin( 7, DefaultVertexFormats.POSITION_TEX );
        bufferbuilder.pos( x, y + maxV, 1 ).tex( minU * f, (minV + maxV) * f ).endVertex();
        bufferbuilder.pos( x + maxU, y + maxV, 1 ).tex( (minU + maxU) * f, (minV + maxV) * f ).endVertex();
        bufferbuilder.pos( x + maxU, y, 1 ).tex( (minU + maxU) * f, minV * f ).endVertex();
        bufferbuilder.pos( x, y, 1 ).tex( minU * f, minV * f ).endVertex();
        tessellator.draw();

    }

    protected void fillGradientVertical( int p_fillGradient_1_, int p_fillGradient_2_, int p_fillGradient_3_, int p_fillGradient_4_, int startColour, int endColour ) {
        super.fillGradient( p_fillGradient_1_, p_fillGradient_2_, p_fillGradient_3_, p_fillGradient_4_, startColour, endColour );
    }

    protected void fillGradientHorizontal( int p_fillGradient_1_, int p_fillGradient_2_, int p_fillGradient_3_, int p_fillGradient_4_, int startColour, int endColour ) {

        float f = (float) (startColour >> 24 & 255) / 255.0F;
        float f1 = (float) (startColour >> 16 & 255) / 255.0F;
        float f2 = (float) (startColour >> 8 & 255) / 255.0F;
        float f3 = (float) (startColour & 255) / 255.0F;
        float f4 = (float) (endColour >> 24 & 255) / 255.0F;
        float f5 = (float) (endColour >> 16 & 255) / 255.0F;
        float f6 = (float) (endColour >> 8 & 255) / 255.0F;
        float f7 = (float) (endColour & 255) / 255.0F;

        GlStateManager.disableTexture();
        GlStateManager.enableBlend();
        GlStateManager.disableAlphaTest();
        GlStateManager.blendFuncSeparate( GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO );
        GlStateManager.shadeModel( 7425 );

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin( 7, DefaultVertexFormats.POSITION_COLOR );

        bufferbuilder.pos( (double) p_fillGradient_3_, (double) p_fillGradient_2_, (double) this.blitOffset ).color( f1, f2, f3, f ).endVertex();
        bufferbuilder.pos( (double) p_fillGradient_1_, (double) p_fillGradient_2_, (double) this.blitOffset ).color( f5, f6, f7, f4 ).endVertex();
        bufferbuilder.pos( (double) p_fillGradient_1_, (double) p_fillGradient_4_, (double) this.blitOffset ).color( f5, f6, f7, f4 ).endVertex();
        bufferbuilder.pos( (double) p_fillGradient_3_, (double) p_fillGradient_4_, (double) this.blitOffset ).color( f1, f2, f3, f ).endVertex();
        tessellator.draw();

        GlStateManager.shadeModel( 7424 );
        GlStateManager.disableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.enableTexture();

    }

    public abstract void drawBackgroundLayer( int x, int y );

    public abstract void drawForegroundLayer();

    public static void horizontalLine( int x, int y, int length, int colour ) {
        fill( x, y, length + x, y + 1, colour );
    }

    public static void verticalLine( int x, int y, int length, int colour ) {
        fill( x, y, x + 1, length + y, colour );
    }

    public void drawBorder( int x, int y ) {

        if ( !hasBorder )
            return;

        y += position.y;
        x += position.x;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture();
        GlStateManager.blendFuncSeparate( GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO );

        bufferbuilder.begin( 7, DefaultVertexFormats.POSITION_COLOR );
        fill( bufferbuilder, x, y, x + width, y + height, BACKGROUND_COLOUR );
        fill( bufferbuilder, x + width, y, x + width + 1, y + height, HIGHLIGHT_COLOUR );
        fill( bufferbuilder, x, y + height, x + width + 1, y + height + 1, HIGHLIGHT_COLOUR );
        fill( bufferbuilder, x - 1, y - 1, x, y + height, LOWLIGHT_COLOUR );
        fill( bufferbuilder, x, y - 1, x + width, y, LOWLIGHT_COLOUR );
        fill( bufferbuilder, x + width, y - 1, x + width + 1, y, BACKGROUND_COLOUR );
        fill( bufferbuilder, x - 1, y + height, x, y + height + 1, BACKGROUND_COLOUR );
        tessellator.draw();

        GlStateManager.enableTexture();
        GlStateManager.disableBlend();

    }

    private void fill( final BufferBuilder bufferBuilder, final int x, final int y, final int width, final int height, final float[] colour ) {

        bufferBuilder.pos( (double) x, (double) height, 0.0D ).color( colour[0], colour[1], colour[2], colour[3] ).endVertex();
        bufferBuilder.pos( (double) width, (double) height, 0.0D ).color( colour[0], colour[1], colour[2], colour[3] ).endVertex();
        bufferBuilder.pos( (double) width, (double) y, 0.0D ).color( colour[0], colour[1], colour[2], colour[3] ).endVertex();
        bufferBuilder.pos( (double) x, (double) y, 0.0D ).color( colour[0], colour[1], colour[2], colour[3] ).endVertex();

    }

    @Nullable
    public abstract List<String> getTooltip( boolean isShiftKeyDown );

    public boolean ownsMousePoint( int mouseX, int mouseY ) {
        return mouseX >= position.x && mouseX <= position.x + width && mouseY >= position.y && mouseY <= position.y + height;
    }

    public Control setHeight( int h ) {

        height = h;
        return this;

    }

    public Control setPostion( Point p ) {

        position = p;
        return this;

    }

    public Control setWidth( int w ) {

        width = w;
        return this;

    }

    public Control setHasBorder( boolean border ) {

        hasBorder = border;
        return this;

    }

}
