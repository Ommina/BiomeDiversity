package ommina.biomediversity.gui.controls;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ommina.biomediversity.gui.Control;

import java.util.List;

@OnlyIn( Dist.CLIENT )
public class Text extends Control {

    private final String unformattedText;
    private final Justification justification;
    private final int containerWidth;

    public enum Justification {
        LEFT, CENTRE, RIGHT
    }

    public Text( String unformattedText, Justification justification, int containerWidth ) {

        this.unformattedText = unformattedText;
        this.justification = justification;
        this.containerWidth = containerWidth;
        this.hasBorder = false;

    }

    public Text( int value, int containerWidth ) {

        this( format.format( value ), Justification.RIGHT, containerWidth );

    }

    //region Overrides
    @Override
    public void drawBackgroundLayer( int x, int y ) {

        //None
    }

    @Override
    public void drawForegroundLayer() {

        String text = I18n.format( unformattedText );

        int left = position.x;

        if ( justification == Justification.CENTRE )
            left = containerWidth / 2 - fontRenderer.getStringWidth( text ) / 2;

        fontRenderer.drawString( text, left, position.y, 0x404040 );

    }

    @Override
    public List<String> getTooltip( boolean isShiftKeyDown ) {

        // Lines of text don't get tooltips.
        return null;

    }
//endregion Overrides

}
