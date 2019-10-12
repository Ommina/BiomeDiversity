package ommina.biomediversity.gui.controls;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.tile.RenderHelper;
import ommina.biomediversity.fluids.BdFluidTank;
import ommina.biomediversity.gui.UV;
import ommina.biomediversity.util.MathUtil;

import java.util.Arrays;
import java.util.List;

@OnlyIn( Dist.CLIENT )
public class Tank extends Control {

    public static final int WIDTH = 16;
    public static final int HEIGHT = 55;

    public static UV FG = new UV( 0, 0, WIDTH, HEIGHT );

    private final BdFluidTank tank;

    public Tank( BdFluidTank tank ) {

        this.tank = tank;

        this.width = WIDTH;
        this.height = HEIGHT;

    }

    //region Overrides
    @Override
    public void drawBackgroundLayer( int x, int y ) {

        if ( tank.getCapacity() <= 0 )
            return;

        FluidStack fluidStack = tank.getFluid();

        if ( fluidStack.isEmpty() || fluidStack.getFluid() == null || fluidStack.getAmount() <= 0 )
            return;


        GlStateManager.disableBlend();
        Minecraft.getInstance().getTextureManager().bindTexture( AtlasTexture.LOCATION_BLOCKS_TEXTURE );

        Fluid fluid = fluidStack.getFluid();

        TextureAtlasSprite fluidStillSprite = RenderHelper.getSprite( fluid.getAttributes().getStillTexture() );

        final int heightTexture = MathUtil.clamp( (int) ((float) fluidStack.getAmount() / (float) tank.getCapacity() * height), 1, height );
        final float[] rgba = RenderHelper.getRGBA( fluid.getAttributes().getColor() );

        GlStateManager.color4f( rgba[0], rgba[1], rgba[2], rgba[3] );

        Control.drawSprite( x + position.x, y + position.y + height - heightTexture, 0, width, heightTexture - 2, fluidStillSprite ); //TODO: Needs to be tiled; it's all squishy in the GUI

        GlStateManager.color4f( 1f, 1f, 1f, 1f );

    }

    @Override
    public void drawForegroundLayer() {

        Minecraft.getInstance().getTextureManager().bindTexture( BiomeDiversity.getId( "textures/gui/overlay.png" ) );

        Control.drawSprite( (float) position.x, (float) position.y, FG.minU, FG.minV, FG.maxU, FG.maxV );

    }

    @Override
    public List<String> getTooltip( boolean isShiftKeyDown ) {

        if ( tank.getCapacity() <= 0 )
            return null;

        FluidStack fluidStack = tank.getFluid();

        if ( fluidStack.isEmpty() || fluidStack.getFluid() == null || fluidStack.getAmount() <= 0 )
            return null;

        if ( !isShiftKeyDown )
            return Arrays.asList( fluidStack.getTranslationKey() );

        return Arrays.asList( fluidStack.getTranslationKey(), format.format( fluidStack.getAmount() ) );

    }
//endregion Overrides

}
