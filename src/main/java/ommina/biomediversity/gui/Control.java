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

@OnlyIn( Dist.CLIENT )
public abstract class Control extends AbstractGui {

    public static final TextureAtlasSprite OVERLAY_SPRITE = RenderHelper.getSprite( BiomeDiversity.getId( "gui/overlay" ) );
    public static final ResourceLocation OVERLAY_RESOURCE = BiomeDiversity.getId( "textures/gui/overlay.png" );

    public static final float[] BACKGROUND_COLOUR = RenderHelper.getRGBA( new Color( 139, 139, 139, 255 ).getRGB() );
    public static final float[] HIGHLIGHT_COLOUR = RenderHelper.getRGBA( Color.white.getRGB() );
    public static final float[] LOWLIGHT_COLOUR = RenderHelper.getRGBA( new Color( 55, 55, 55, 255 ).getRGB() );

    protected static final NumberFormat format = NumberFormat.getInstance( Locale.getDefault() );
    protected static FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;

    protected Point position = new Point( 42, 42 );

    protected boolean hasBorder = true;
    protected int height = 0;
    protected int width = 0;

    public static void drawSprite( int x, int y, int offset, int width, int height, TextureAtlasSprite sprite ) {
        innerBlit( x, x + width, y, y + height, offset, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV() );
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


    public abstract void drawBackgroundLayer( int x, int y );

    public abstract void drawForegroundLayer();

/*

    public void prepareBackground( int x, int y, int width, int height ) {

        int c = Color.GREEN.getRGB();

        horizontalLine( 10, 10, 20, c );
        verticalLine( 10, 10, 20, c );

        //drawBorder( 30, 30, 20, 20, RenderHelper.getRGBA( c ) );

        hLine( 50, 100, 50, c );

    }

*/

    public static void horizontalLine( int x, int y, int length, int colour ) {

        fill( x, y, length + x, y + 1, colour );

    }

    public static void verticalLine( int x, int y, int length, int colour ) {

        fill( x, y, x + 1, length + y, colour );

    }

    public void drawBorder( int x, int y ) {

        if ( !hasBorder )
            return;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture();
        GlStateManager.blendFuncSeparate( GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO );

        bufferbuilder.begin( 7, DefaultVertexFormats.POSITION_COLOR );
        fill( bufferbuilder, position.x + x, position.y + y, position.x + x + width, position.y + y + height, BACKGROUND_COLOUR );
        fill( bufferbuilder, position.x + x + width, position.y + y, position.x + x + width + 1, position.y + y + height, HIGHLIGHT_COLOUR );
        fill( bufferbuilder, position.x + x, position.y + y + height, position.x + x + width + 1, position.y + y + height + 1, HIGHLIGHT_COLOUR );
        fill( bufferbuilder, position.x + x - 1, position.y + y - 1, position.x + x, position.y + y + height, LOWLIGHT_COLOUR );
        fill( bufferbuilder, position.x + x, position.y + y - 1, position.x + x + width, position.y + y, LOWLIGHT_COLOUR );
        fill( bufferbuilder, position.x + x + width, position.y + y - 1, position.x + x + width + 1, position.y + y, BACKGROUND_COLOUR );
        fill( bufferbuilder, position.x + x - 1, position.y + y + height, position.x + x, position.y + y + height + 1, BACKGROUND_COLOUR );
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

        //System.out.println( "mouse: " + mouseX + ", " + mouseY );

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

    public Control setSize( Dimension size ) {
        return setWidth( size.width ).setHeight( size.height );
    }

}
