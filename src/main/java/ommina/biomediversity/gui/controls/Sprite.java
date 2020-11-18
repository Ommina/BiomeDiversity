package ommina.biomediversity.gui.controls;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ommina.biomediversity.gui.Control;
import ommina.biomediversity.gui.UV;

import java.util.List;

@OnlyIn( Dist.CLIENT )
public class Sprite extends Control {

    private final UV FG = new UV( 0, 0, Sizes.SPRITE.width, Sizes.SPRITE.height );
    private final ResourceLocation sprite;

    public Sprite( final ResourceLocation sprite ) {
        super( Sizes.SPRITE );

        this.sprite = sprite;

    }

    //region Overrides
    @Override
    public void drawBackgroundLayer( MatrixStack matrixStack, int x, int y ) {
        //None
    }

    @Override
    public void drawForegroundLayer(MatrixStack matrixStack) {

        Minecraft.getInstance().getTextureManager().bindTexture( sprite );
        Control.drawSprite( 16f / 256f, (float) position.x, (float) position.y, FG.minU, FG.minV, FG.maxU, FG.maxV );

    }

    @Override
    public List<String> getTooltip( boolean isShiftKeyDown ) {
        return null; // Lines of text don't get tooltips.
    }
//endregion Overrides

}
