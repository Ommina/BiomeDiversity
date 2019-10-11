package ommina.biomediversity.gui.controls;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ommina.biomediversity.BiomeDiversity;

import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@OnlyIn( Dist.CLIENT)
public abstract class Control extends AbstractGui {

    public static final ResourceLocation OVERLAY_TEXTURE = BiomeDiversity.getId( "textures/gui/overlay.png" );

    protected static final NumberFormat format = NumberFormat.getInstance( Locale.getDefault() );
    protected static FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;

    protected Point position = new Point( 42, 42 );

    protected int height = 0;
    protected int width = 0;

    public abstract void drawBackgroundLayer( int x, int y );

    public abstract void drawForegroundLayer();

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
