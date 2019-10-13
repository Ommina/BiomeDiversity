package ommina.biomediversity.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseContainerScreen<T extends Container> extends ContainerScreen<T> {

    protected Set<Control> controls = new HashSet<Control>();
    protected ResourceLocation GUI;

    public BaseContainerScreen( T screenContainer, PlayerInventory inv, ITextComponent title ) {
        super( screenContainer, inv, title );

    }

//region Overrides

    @Override
    public void render( int mouseX, int mouseY, float partialTicks ) {

        this.renderBackground();
        super.render( mouseX, mouseY, partialTicks );
        this.renderHoveredToolTip( mouseX, mouseY );
    }

    @Override
    protected void renderHoveredToolTip( int mouseX, int mouseY ) {

        for ( Control ctl : controls )
            if ( ctl.ownsMousePoint( mouseX - this.guiLeft, mouseY - guiTop ) && ctl.getTooltip( hasShiftDown() ) != null )
                this.renderTooltip( ctl.getTooltip( hasShiftDown() ), mouseX, mouseY );

    }

    @Override
    protected void drawGuiContainerForegroundLayer( int mouseX, int mouseY ) {

        final int x = (this.width - this.xSize) / 2;
        final int y = (this.height - this.ySize) / 2;

        for ( Control ctl : controls )
            ctl.drawForegroundLayer();//( x, y );

    }

    @Override
    public void drawGuiContainerBackgroundLayer( float partialTicks, int mouseX, int mouseY ) {

        GlStateManager.color4f( 1.0F, 1.0F, 1.0F, 1.0F );

        this.minecraft.getTextureManager().bindTexture( GUI );
        final int x = (this.width - this.xSize) / 2;
        final int y = (this.height - this.ySize) / 2;
        this.blit( x, y, 0, 0, this.xSize, this.ySize );

        for ( Control ctl : controls )
            ctl.drawBackgroundLayer( x, y );

    }
//endregion Overrides

}
