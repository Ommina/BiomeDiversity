package ommina.biomediversity.gui;

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

    protected static final NumberFormat format = NumberFormat.getInstance( Locale.getDefault() );
    protected static FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;

    protected Point position = new Point( 42, 42 );

    protected int height = 0;
    protected int width = 0;

    public static void drawSprite( int x, int y, int offset, int width, int height, TextureAtlasSprite sprite ) {
        innerBlit( x, x + width, y, y + height, offset, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV() );
    }

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

    public abstract void drawBackgroundLayer( int x, int y );

    public abstract void drawForegroundLayer();

    @Nullable
    public abstract List<String> getTooltip( boolean isShiftKeyDown );

    public boolean ownsMousePoint( int mouseX, int mouseY ) {

        return mouseX >= position.x && mouseX <= position.x + width && mouseY >= position.y && mouseY <= position.y + height;

    }

    public void setHeight( int h ) {

        height = h;
    }

    public void setPostion( Point p ) {

        position = p;

    }

    public void setWidth( int w ) {

        width = w;

    }

}
