package ommina.biomediversity.gui.controls;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ommina.biomediversity.gui.Control;
import ommina.biomediversity.gui.UV;

import java.util.List;

@OnlyIn( Dist.CLIENT )
public class Sprite extends Control {

    private final UV FG;
    private final ResourceLocation sprite;

    public Sprite( final ResourceLocation sprite ) {

        super( Sizes.SPRITE );

        this.sprite = sprite;

        FG = new UV( 0, 0, width, height );

    }

    //region Overrides
    @Override
    public void drawBackgroundLayer( int x, int y ) {

        //None
    }

    @Override
    public void drawForegroundLayer() {


        Minecraft.getInstance().getTextureManager().bindTexture( sprite );

        //Control.drawSprite( position.x,  position.y , 0, 16, 16, sprite ); //TODO: Needs to be tiled; it's all squishy in the GUI

        Control.drawSprite( 16f / 256f, (float) position.x, (float) position.y, FG.minU, FG.minV, FG.maxU, FG.maxV );


        // this.minecraft.getTextureManager().bindTexture( GUI );


        //Minecraft.getInstance().getTextureManager().bindTexture( sprite );

        //Minecraft.getInstance().getTextureMap().getSprite( sprite );

        //final int y = MathUtil.clamp( (height * (1f - (value - min) / range)), GAUGE_BAR_VERTICAL_LENGTH, HEIGHT - GAUGE_BAR_VERTICAL_LENGTH );

        //Control.drawSprite( position.x, position.y, FG.minU, FG.minV, FG.maxU, FG.maxV );


    /*

        String text = I18n.format( unformattedText );

        int left = position.x;

        if ( justification == Justification.CENTRE )
            left = containerWidth / 2 - fontRenderer.getStringWidth( text ) / 2;

        fontRenderer.drawString( text, left, position.y, 0x404040 );

*/

    }

    @Override
    public List<String> getTooltip( boolean isShiftKeyDown ) {

        // Lines of text don't get tooltips.
        return null;

    }
//endregion Overrides

}
