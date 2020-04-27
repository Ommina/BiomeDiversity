package ommina.biomediversity.gui.controls;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ommina.biomediversity.fluids.ModFluids;
import ommina.biomediversity.gui.Control;
import ommina.biomediversity.gui.UV;
import ommina.biomediversity.util.MathUtil;

import java.util.Arrays;
import java.util.List;

@OnlyIn( Dist.CLIENT )
public class Temperature extends DynamicRange {

    private static final int GAUGE_BAR_VERTICAL_LENGTH = 2;

    private static final int startColour;
    private static final int endColour;

    private static final UV FG = new UV( 16, 0, 16 + Sizes.TEMPERATURE.width, 5 );

    static {

        startColour = ModFluids.WARMBIOMETIC.getAttributes().getColor();
        endColour = ModFluids.COOLBIOMETIC.getAttributes().getColor();

    }

    public Temperature( TileEntity te, String methodName, float min, float max ) {
        super( Sizes.TEMPERATURE, te, methodName, min, max );
    }

    public Temperature( float value, float min, float max ) {
        super( Sizes.TEMPERATURE, value, min, max );
    }

    //region Overrides
    @Override
    public void drawBackgroundLayer( int x, int y ) {
        this.fillGradient( position.x + x, position.y + y, position.x + x + this.width, position.y + y + this.height, startColour, endColour );
    }

    @Override
    public void drawForegroundLayer() {

        setValue();

        Minecraft.getInstance().getTextureManager().bindTexture( OVERLAY_RESOURCE );

        final int y = MathUtil.clamp( (height * (1f - (value - min) / range)), GAUGE_BAR_VERTICAL_LENGTH, height - GAUGE_BAR_VERTICAL_LENGTH );

        drawSprite( TEXTURE_RESOLUTION, (float) position.x, (float) (position.y + y - GAUGE_BAR_VERTICAL_LENGTH), FG.minU, FG.minV, FG.sizeU, FG.sizeV );

    }

    @Override
    public List<String> getTooltip( boolean isShiftKeyDown ) {

        return (isShiftKeyDown ? Arrays.asList( format.format( value ) ) : null);
    }
//endregion Overrides

}
