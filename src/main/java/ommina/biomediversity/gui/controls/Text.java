package ommina.biomediversity.gui.controls;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ommina.biomediversity.gui.Control;
import ommina.biomediversity.rendering.RenderHelper;

import java.util.List;

import static ommina.biomediversity.config.Constants.DEFAULT_TEXT_COLOUR;

@OnlyIn( Dist.CLIENT )
public class Text extends Control {

    private final String unformattedText;
    private final RenderHelper.Justification justification;
    private final int containerWidth;

    public Text( String unformattedText, RenderHelper.Justification justification, int containerWidth ) {
        super( Sizes.EMPTY );

        this.unformattedText = unformattedText;
        this.justification = justification;
        this.containerWidth = containerWidth;
        this.hasBorder = false;

    }

    public Text( int value, int containerWidth ) {
        this( format.format( value ), RenderHelper.Justification.RIGHT, containerWidth );
    }

    //region Overrides
    @Override
    public void drawBackgroundLayer( MatrixStack matrixStack, int x, int y ) {
        //None
    }

    @Override
    public void drawForegroundLayer( MatrixStack matrixStack ) {
        RenderHelper.drawText( I18n.format( unformattedText ), matrixStack, position.x, position.y, containerWidth, justification, DEFAULT_TEXT_COLOUR );
    }

    @Override
    public List<String> getTooltip( boolean isShiftKeyDown ) {
        return null; // Lines of text don't get tooltips.  Not yet, anyway
    }

//endregion Overrides

}
