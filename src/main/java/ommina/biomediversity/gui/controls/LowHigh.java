package ommina.biomediversity.gui.controls;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import ommina.biomediversity.gui.Control;

import java.util.Collections;
import java.util.List;

public class LowHigh extends AbstractLowHigh {

    public LowHigh( TileEntity te, String methodName, ScaleMode scaleMode, float min, float max ) {
        super( te, methodName, scaleMode, min, max );
    }

    //region Overrides
    @Override
    public void drawBackgroundLayer( MatrixStack matrixStack, int x, int y ) {

        Minecraft.getInstance().getTextureManager().bindTexture( OVERLAY_RESOURCE );

        Control.drawSprite( TEXTURE_RESOLUTION, position.x + x, position.y + y, BG.minU, BG.minV, BG.maxU, BG.maxV );

    }

    @Override
    public void drawForegroundLayer( MatrixStack matrixStack ) {

        setValue();

        Minecraft.getInstance().getTextureManager().bindTexture( OVERLAY_RESOURCE );

        Control.drawSprite( TEXTURE_RESOLUTION, position.x, position.y + getY() - GAUGE_BAR_VERTICAL_LENGTH, FG.minU, FG.minV, FG.maxU, FG.maxV );

    }

    @Override
    public List<String> getTooltip( boolean isShiftKeyDown ) {

        return (isShiftKeyDown ? Collections.singletonList( format.format( value ) ) : null);
    }
//endregion Overrides

}
