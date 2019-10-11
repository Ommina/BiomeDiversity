package ommina.biomediversity.gui.controls;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
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

        System.out.println( "moo" );

        if ( tank == null || tank.getCapacity() <= 0 )
            return;

        FluidStack fluidStack = tank.getFluid();

        if ( fluidStack.isEmpty() || fluidStack.getFluid() == null || fluidStack.getAmount() <= 0 )
            return;

        GlStateManager.disableBlend();

        TextureManager textureManager = Minecraft.getInstance().getTextureManager();

        Fluid fluid = fluidStack.getFluid();

        ResourceLocation fluidStill = fluid.getAttributes().getStillTexture();

        TextureAtlasSprite fluidStillSprite = null;

        if ( fluidStill != null )
            fluidStillSprite = RenderHelper.getSprite( fluidStill );


        int fluidColor = fluid.getAttributes().getColor();

        int heightTexture = MathUtil.clamp( (int) ((float) fluidStack.getAmount() / (float) tank.getCapacity() * height), 1, height );

        float[] rgba = new float[]{ ((fluidColor >> 16) & 0xFF) / 255f, ((fluidColor >> 8) & 0xFF) / 255f, ((fluidColor >> 0) & 0xFF) / 255f, ((fluidColor >> 24) & 0xFF) / 255f };

        GlStateManager.color4f( rgba[0], rgba[1], rgba[2], rgba[3] );

        //textureManager.bindTexture( TextureMap.LOCATION_BLOCKS_TEXTURE );

        this.blit( x + position.x, y + position.y + height - heightTexture, 1, width, heightTexture - 2, fluidStillSprite );

        //drawTexturedModalRect( x + position.x, y + position.y + height - heightTexture, fluidStillSprite, width, heightTexture - 2 );

        GlStateManager.color4f( 1f, 1f, 1f, 1f );

    }

    @Override
    public void drawForegroundLayer() {

        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        textureManager.bindTexture( OVERLAY_TEXTURE );


        //drawTexturedModalRect( (float) position.x, (float) position.y, FG.minU, FG.minV, FG.sizeU, FG.sizeV );

    }

    @Override
    public List<String> getTooltip( boolean isShiftKeyDown ) {

        if ( tank == null || tank.getCapacity() <= 0 )
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
