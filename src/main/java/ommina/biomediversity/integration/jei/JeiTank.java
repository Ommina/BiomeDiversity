package ommina.biomediversity.integration.jei;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ommina.biomediversity.gui.Control;

import javax.annotation.Nullable;
import java.util.List;

@OnlyIn( Dist.CLIENT )
public class JeiTank extends Control {

    public JeiTank() {
        super( Sizes.TANK );

    }

    //region Overrides
    @Override
    public void drawBackgroundLayer( int x, int y ) {

        drawBorder( x, y );

    }

    @Override
    public void drawForegroundLayer() {

    }

    @Nullable
    @Override
    public List<String> getTooltip( boolean isShiftKeyDown ) {
        return null;
    }
//endregion Overrides

}
