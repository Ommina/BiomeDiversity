package ommina.biomediversity.gui.controls;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.fluids.ModFluids;
import ommina.biomediversity.gui.Control;
import ommina.biomediversity.gui.UV;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@OnlyIn( Dist.CLIENT )
public class RfMeter extends Control {

    private static final int startColour;
    private static final int endColour;
    private static final UV BG = new UV( 16, 8, 16 + Sizes.LOG_GAUGE.width, 8 + Sizes.LOG_GAUGE.height );

    private static final float H = Sizes.LOG_GAUGE.width / 2f;

    static {

        startColour = ModFluids.WARMBIOMETIC.getAttributes().getColor();
        endColour = ModFluids.COOLBIOMETIC.getAttributes().getColor();

    }

    private TileEntity tile;
    private Method method;

    private int value;

    private float startX;
    private float startY;

    private enum Thetas {

        ONE( 0, 10000, 115, 179 ),
        TWO( 10001, 30000, 180, 269 ),
        THREE( 30001, 10000000, 270, 359 ),
        FOUR( 10000001, 25000000, 0, 64 );

        private final int rangeStart;
        private final int rangeEnd;
        private final int thetaStart;
        private final int thetaEnd;

        Thetas( int rangeStart, int rangeEnd, int thetaStart, int thetaEnd ) {

            this.rangeStart = rangeStart;
            this.rangeEnd = rangeEnd;
            this.thetaStart = thetaStart;
            this.thetaEnd = thetaEnd;

        }

        static double get( int rf ) {

            for ( Thetas t : Thetas.values() )
                if ( rf >= t.rangeStart && rf <= t.rangeEnd )
                    return scale( rf, t );

            return scale( rf, FOUR );

        }

        private static double scale( int rf, Thetas thetas ) {

            int range = thetas.rangeEnd - thetas.rangeStart;
            double d = (rf - thetas.rangeStart) / (double) range;

            return thetas.thetaStart + ((thetas.thetaEnd - thetas.thetaStart) * d);

        }

    }

    public RfMeter( final TileEntity tileEntity, final String methodName ) {
        super( Sizes.LOG_GAUGE );

        tile = tileEntity;

        try {
            method = tile.getClass().getMethod( methodName );
        } catch ( NoSuchMethodException e ) {
            BiomeDiversity.LOGGER.error( "Reflection failed.  NoSuchMethod: " + methodName );
        } catch ( SecurityException e ) {
            BiomeDiversity.LOGGER.error( "Reflection failed.  Security Exception: " + methodName );
        }

    }

    //region Overrides
    @Override
    public void drawBackgroundLayer( int x, int y ) {

        this.fillGradientHorizontal( position.x + x, position.y + y, position.x + x + this.width, position.y + y + this.height, startColour, endColour );

        final float f = 1f / 256f;

        Minecraft.getInstance().getTextureManager().bindTexture( OVERLAY_RESOURCE );

        drawSprite( f, (float) position.x + x, (float) position.y + y, BG.minU, BG.minV, BG.sizeU, BG.sizeV );

    }

    @Override
    public void drawForegroundLayer() {

        setValue();

        double theta = Thetas.get( value );
        double y = Math.sin( Math.toRadians( theta ) ) * H;
        double x = Math.cos( Math.toRadians( theta ) ) * H;

        drawLine( startX, startY, startX + x, startY + y, Color.BLACK.getRGB() );
        fill( startX - 0.5d, startY - 0.5d, startX + 0.5d, startY + 0.5d, Color.DARK_GRAY.getRGB() );

    }

    @Override
    public List<String> getTooltip( boolean isShiftKeyDown ) {
        return null;
    }

    @Override
    public Control setPostion( Point p ) {
        super.setPostion( p );

        startX = position.x + Sizes.LOG_GAUGE.width / 2.0f;
        startY = position.y + Sizes.LOG_GAUGE.height / 2.0f;

        return this;

    }
//endregion Overrides

    public void setValue() {

        if ( method == null )
            return;

        try {
            value = ((int) method.invoke( tile ));
        } catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException e ) {
            BiomeDiversity.LOGGER.error( "Reflection failed." );
        }

    }

}
