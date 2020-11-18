package ommina.biomediversity.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

@OnlyIn( Dist.CLIENT )
public abstract class BaseContainerScreen<T extends Container> extends ContainerScreen<T> {

    protected static final Point TITLE_TEXT = new Point( 0, 5 );

    protected Set<Control> controls = new HashSet<Control>();
    protected ResourceLocation GUI;

    public BaseContainerScreen( T screenContainer, PlayerInventory inv, ITextComponent title ) {
        super( screenContainer, inv, title );

    }

//region Overrides

    @Override
    public void render( MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks ) {

        //this.renderBackground();
        super.render( matrixStack, mouseX, mouseY, partialTicks );
        // this.func_230459_a_( matrixStack, mouseX, mouseY );
    }

//    @Override
//    protected void func_230459_a_( MatrixStack matrixStack, int mouseX, int mouseY ) {
//
    //for ( Control ctl : controls )
    //if ( ctl.ownsMousePoint( mouseX - this.guiLeft, mouseY - guiTop ) && ctl.getTooltip( hasShiftDown() ) != null )
    //this.renderTooltip( ctl.getTooltip( hasShiftDown() ), mouseX, mouseY );

//    }

    @Override
    protected void drawGuiContainerForegroundLayer( MatrixStack matrixStack, int mouseX, int mouseY ) {

        final int x = (this.width - this.xSize) / 2;
        final int y = (this.height - this.ySize) / 2;

        for ( Control ctl : controls )
            ctl.drawForegroundLayer( matrixStack );//( x, y );

    }

    @Override
    public void drawGuiContainerBackgroundLayer( MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY ) {

        GlStateManager.color4f( 1.0F, 1.0F, 1.0F, 1.0F );

        this.minecraft.getTextureManager().bindTexture( GUI );
        final int x = (this.width - this.xSize) / 2;
        final int y = (this.height - this.ySize) / 2;
        this.blit( matrixStack, x, y, 0, 0, this.xSize, this.ySize );

        controls.forEach( ctl -> {
            ctl.drawBorder( matrixStack, x, y );
            ctl.drawBackgroundLayer( matrixStack, x, y );
        } );

    }
//endregion Overrides

}
