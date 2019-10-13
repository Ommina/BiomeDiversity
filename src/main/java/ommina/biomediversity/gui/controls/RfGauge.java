package ommina.biomediversity.gui.controls;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.energy.EnergyStorage;
import ommina.biomediversity.gui.Control;
import ommina.biomediversity.gui.UV;
import ommina.biomediversity.util.MathUtil;
import ommina.biomediversity.util.Translator;

import java.util.Collections;
import java.util.List;

@OnlyIn( Dist.CLIENT )
public class RfGauge extends Control {

    private static final int WIDTH = 8;
    private static final int HEIGHT = 53;

    private static final UV BG = new UV( 41, 0, 41 + WIDTH, HEIGHT );
    private static final UV BG_UNCHARGEABLE = new UV( 49, 0, 49 + WIDTH, HEIGHT );
    private static final UV FG = new UV( 33, 0, 33 + WIDTH, HEIGHT );

    private final EnergyStorage BATTERY;

    public RfGauge( EnergyStorage battery ) {

        assignwh();

        this.BATTERY = battery;

    }

    public RfGauge( int energy, int maxEnergy ) {

        assignwh();

        this.BATTERY = new EnergyStorage( maxEnergy, 0, 0 );
        this.BATTERY.receiveEnergy( energy, false );

    }

    private void assignwh() {

        width = WIDTH;
        height = HEIGHT;

    }

    //region Overrides
    @Override
    public void drawBackgroundLayer( int x, int y ) {

        Minecraft.getInstance().getTextureManager().bindTexture( OVERLAY_RESOURCE );

        if ( BATTERY.getMaxEnergyStored() == 0 ) {
            drawSprite( (float) position.x + x, (float) position.y + y, BG_UNCHARGEABLE.minU, BG_UNCHARGEABLE.minV, BG_UNCHARGEABLE.sizeU, BG_UNCHARGEABLE.sizeV );
            return;
        }

        drawSprite( (float) position.x + x, (float) position.y + y, BG.minU, BG.minV, BG.sizeU, BG.sizeV );

        int h = MathUtil.clamp( HEIGHT - (int) ((float) BATTERY.getEnergyStored() / (float) BATTERY.getMaxEnergyStored() * HEIGHT), 0, HEIGHT );


        drawSprite( position.x + x, (float) position.y + y + h, FG.minU, FG.minV + h, FG.sizeU, FG.sizeV - h );

    }

    @Override
    public void drawForegroundLayer() {

        // While the foreground ('filled') portion should probably be in here, it has been left in drawBackground, just to avoid the extra texture binding
    }

    @Override
    public List<String> getTooltip( boolean isShiftKeyDown ) {

        if ( this.BATTERY.getEnergyStored() == 0 )
            if ( isShiftKeyDown )
                return Collections.singletonList( Translator.translateToLocal( "text.biomediversity.gui.powerdisabled" ) );
            else
                return null;
        else
            return Collections.singletonList( format.format( this.BATTERY.getEnergyStored() ) );

    }

//endregion Overrides

}
