package ommina.biomediversity.gui.controls;

import net.minecraft.tileentity.TileEntity;
import ommina.biomediversity.gui.UV;
import ommina.biomediversity.util.MathUtil;

public abstract class AbstractLowHigh extends DynamicRange {

    protected static final int GAUGE_BAR_VERTICAL_LENGTH = 1;

    protected static final UV BG = new UV( 57, 0, Sizes.LOW_HIGH.width, Sizes.LOW_HIGH.height );
    protected static final UV FG = new UV( 16, 5, Sizes.LOW_HIGH.width, 3 );

    private static final double BASE = 1.19d;

    private final ScaleMode scaleMode;

    public enum ScaleMode {
        LINEAR,
        LOGARITHMIC,
        MIXED
    }

    public AbstractLowHigh( TileEntity te, String methodName, ScaleMode scaleMode, float min, float max ) {
        super( Sizes.LOW_HIGH, te, methodName, min, max );

        this.scaleMode = scaleMode;

    }

    public AbstractLowHigh( ScaleMode scaleMode, float value, float min, float max ) {
        super( Sizes.LOW_HIGH, value, min, max );

        this.scaleMode = scaleMode;

    }

    protected int getY() {

        if ( scaleMode == ScaleMode.LINEAR )
            return MathUtil.clamp( (height * (1f - (value - min) / range)), GAUGE_BAR_VERTICAL_LENGTH, height - GAUGE_BAR_VERTICAL_LENGTH );
        else if ( scaleMode == ScaleMode.LOGARITHMIC )
            return MathUtil.clamp( (int) (Math.log( (double) value ) / Math.log( BASE )), GAUGE_BAR_VERTICAL_LENGTH, height - GAUGE_BAR_VERTICAL_LENGTH );
        else
            return MathUtil.clamp( (height * (1f - (((int) (Math.log( (double) value ) / Math.log( BASE )) - min) / range))), GAUGE_BAR_VERTICAL_LENGTH, height - GAUGE_BAR_VERTICAL_LENGTH );

    }

}
