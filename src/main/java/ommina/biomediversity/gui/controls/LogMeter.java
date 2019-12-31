package ommina.biomediversity.gui.controls;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ommina.biomediversity.fluids.ModFluids;
import ommina.biomediversity.gui.Control;
import ommina.biomediversity.gui.UV;

import java.util.List;

@OnlyIn( Dist.CLIENT )
public class LogMeter extends Control {

    private static final int startColour;
    private static final int endColour;

    private static final UV BG = new UV( 16, 8, 16 + Sizes.LOG_GAUGE.width, 8 + Sizes.LOG_GAUGE.height );

    static {

        startColour = ModFluids.WARMBIOMETIC.getAttributes().getColor();
        endColour = ModFluids.COOLBIOMETIC.getAttributes().getColor();

    }

    private final int value;

    public LogMeter( final int value ) {
        super( Sizes.LOG_GAUGE );

        this.value = value;

    }

    //region Overrides
    @Override
    public void drawBackgroundLayer( int x, int y ) {

        this.fillGradientHorizontal( position.x + x, position.y + y, position.x + x + this.width, position.y + y + this.height, startColour, endColour );

        final float f = 1f / 256f;

        Minecraft.getInstance().getTextureManager().bindTexture( OVERLAY_RESOURCE );

        drawSprite( f, (float) position.x + x, (float) position.y + y, BG.minU, BG.minV, BG.sizeU, BG.sizeV );


        /*

        final float f = 1f / 256f;

        Minecraft.getInstance().getTextureManager().bindTexture( OVERLAY_RESOURCE );

        if ( BATTERY == null || BATTERY.getMaxEnergyStored() == 0 ) {
            drawSprite( f, (float) position.x + x, (float) position.y + y, BG_UNCHARGEABLE.minU, BG_UNCHARGEABLE.minV, BG_UNCHARGEABLE.sizeU, BG_UNCHARGEABLE.sizeV );
            return;
        }

        drawSprite( f, (float) position.x + x, (float) position.y + y, BG.minU, BG.minV, BG.sizeU, BG.sizeV );

        int h = MathUtil.clamp( height - (int) ((float) BATTERY.getEnergyStored() / (float) BATTERY.getMaxEnergyStored() * height), 0, height );
        drawSprite( f, position.x + x, (float) position.y + y + h, FG.minU, FG.minV + h, FG.sizeU, FG.sizeV - h );

        */

    }

    @Override
    public void drawForegroundLayer() {

        // While the foreground ('filled') portion should probably be in here, it has been left in drawBackground, just to avoid the extra texture binding
    }

    @Override
    public List<String> getTooltip( boolean isShiftKeyDown ) {

        /*

        if ( BATTERY == null || BATTERY.getEnergyStored() == 0 )
            if ( isShiftKeyDown )
                return Collections.singletonList( Translator.translateToLocal( "text.biomediversity.gui.powerdisabled" ) );
            else
                return null;
        else
            return Collections.singletonList( format.format( this.BATTERY.getEnergyStored() ) );

        */

        return null;

    }
//endregion Overrides

}
