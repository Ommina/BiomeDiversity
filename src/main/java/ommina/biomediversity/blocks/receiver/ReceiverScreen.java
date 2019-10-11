package ommina.biomediversity.blocks.receiver;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.gui.controls.Control;
import ommina.biomediversity.gui.controls.Tank;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

@OnlyIn( Dist.CLIENT )
public class ReceiverScreen extends ContainerScreen<ReceiverContainer> {

    private static final Point TITLE_TEXT = new Point( 0, 5 );
    private static final Point TANK_INPUT = new Point( 8, 15 );
    private static final Point BIOMENAME_TEXT = new Point( 27, 18 );
    private static final Point TEMPERATURE_GAUGE = new Point( 133, 15 );
    private static final ResourceLocation GUI = BiomeDiversity.getId( "textures/gui/receiver.png" );

    protected Set<Control> controls = new HashSet<Control>();

    public ReceiverScreen( ReceiverContainer container, PlayerInventory inv, ITextComponent name ) {
        super( container, inv, name );

        TileEntityReceiver receiver = (TileEntityReceiver) container.getTileEntity();

        Tank t = new Tank( receiver.getTank( 0 ) );
        t.setPostion( TANK_INPUT );
        controls.add( t );

    }

    //region Overrides
    @Override
    public void render( int mouseX, int mouseY, float partialTicks ) {
        this.renderBackground();
        super.render( mouseX, mouseY, partialTicks );
        this.renderHoveredToolTip( mouseX, mouseY );
    }

    @Override
    protected void drawGuiContainerForegroundLayer( int mouseX, int mouseY ) {
    }

    @Override
    protected void drawGuiContainerBackgroundLayer( float partialTicks, int mouseX, int mouseY ) {

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
