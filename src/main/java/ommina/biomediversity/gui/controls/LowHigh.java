package ommina.biomediversity.gui.controls;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import ommina.biomediversity.gui.Control;
import ommina.biomediversity.gui.UV;
import ommina.biomediversity.util.MathUtil;

import java.util.Collections;
import java.util.List;

public class LowHigh extends DynamicRange {

    private static final int GAUGE_BAR_VERTICAL_LENGTH = 1;

    private static final UV BG = new UV( 57, 0, Sizes.LOW_HIGH.width, Sizes.LOW_HIGH.height );
    private static final UV FG = new UV( 16, 5, Sizes.LOW_HIGH.width, 3 );

    private static final double BASE = 1.19d;

    private ScaleMode scaleMode;

    public enum ScaleMode {
        LINEAR,
        LOGARITHMIC,
        MIXED
    }

    public LowHigh( TileEntity te, String methodName, ScaleMode scaleMode, float min, float max ) {
        super( Sizes.LOW_HIGH, te, methodName, min, max );

        this.scaleMode = scaleMode;

    }

    //region Overrides
    @Override
    public void drawBackgroundLayer( int x, int y ) {

        Minecraft.getInstance().getTextureManager().bindTexture( OVERLAY_RESOURCE );

        float f = 1f / 256f;

        Control.drawSprite( f, position.x + x, position.y + y, BG.minU, BG.minV, BG.maxU, BG.maxV );

    }

    @Override
    public void drawForegroundLayer() {

        setValue();

        Minecraft.getInstance().getTextureManager().bindTexture( OVERLAY_RESOURCE );

        float f = 1f / 256f;

        Control.drawSprite( f, position.x, position.y + getY() - GAUGE_BAR_VERTICAL_LENGTH, FG.minU, FG.minV, FG.maxU, FG.maxV );

    }

    @Override
    public List<String> getTooltip( boolean isShiftKeyDown ) {

        return (isShiftKeyDown ? Collections.singletonList( format.format( value ) ) : null);
    }
//endregion Overrides

    private int getY() {

        if ( scaleMode == ScaleMode.LINEAR )
            return MathUtil.clamp( (height * (1f - (value - min) / range)), GAUGE_BAR_VERTICAL_LENGTH, height - GAUGE_BAR_VERTICAL_LENGTH );
        else if ( scaleMode == ScaleMode.LOGARITHMIC )
            return MathUtil.clamp( (int) (Math.log( (double) value ) / Math.log( BASE )), GAUGE_BAR_VERTICAL_LENGTH, height - GAUGE_BAR_VERTICAL_LENGTH );
        else
            return MathUtil.clamp( (height * (1f - (((int) (Math.log( (double) value ) / Math.log( BASE )) - min) / range))), GAUGE_BAR_VERTICAL_LENGTH, height - GAUGE_BAR_VERTICAL_LENGTH );

    }

}
