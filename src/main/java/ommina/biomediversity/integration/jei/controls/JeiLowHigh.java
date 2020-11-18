package ommina.biomediversity.integration.jei.controls;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import ommina.biomediversity.config.Constants;
import ommina.biomediversity.gui.Control;
import ommina.biomediversity.gui.controls.AbstractLowHigh;

import javax.annotation.Nullable;
import java.util.List;

public class JeiLowHigh extends AbstractLowHigh {

    public JeiLowHigh() {
        super( ScaleMode.LINEAR, 0f, Constants.MIN_FLUID_STRENGTH, Constants.MAX_FLUID_STRENGTH );
    }

    //region Overrides
    @Override
    public void drawBackgroundLayer( MatrixStack matrixStack, int x, int y ) {

        drawBorder( matrixStack, x, y );

        Minecraft.getInstance().getTextureManager().bindTexture( OVERLAY_RESOURCE );

        float f = 1f / 256f;

        Control.drawSprite( f, position.x, position.y, BG.minU, BG.minV, BG.maxU, BG.maxV );

    }

    @Override
    public void drawForegroundLayer(MatrixStack matrixStack) {

        Minecraft.getInstance().getTextureManager().bindTexture( OVERLAY_RESOURCE );

        float f = 1f / 256f;

        Control.drawSprite( f, position.x, position.y + getY() - GAUGE_BAR_VERTICAL_LENGTH, FG.minU, FG.minV, FG.maxU, FG.maxV );

    }

    @Nullable
    @Override
    public List<String> getTooltip( boolean isShiftKeyDown ) {
        return null;
    }
//endregion Overrides


}
