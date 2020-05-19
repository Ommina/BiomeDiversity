package ommina.biomediversity.blocks.plug.fluid;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.blocks.collector.Tubes;
import ommina.biomediversity.blocks.plug.PlugCollectorDetails;
import ommina.biomediversity.blocks.plug.TileEntityPlugBase;
import ommina.biomediversity.rendering.RenderHelper;
import ommina.biomediversity.gui.BaseContainerScreen;
import ommina.biomediversity.gui.controls.Tank;
import ommina.biomediversity.gui.controls.Text;
import ommina.biomediversity.util.Translator;

import java.awt.*;

@OnlyIn( Dist.CLIENT )
public class PlugFluidByproductScreen extends BaseContainerScreen<PlugFluidByproductContainer> {

    private static final Point TANK_INPUT = new Point( 8, 15 );

    public PlugFluidByproductScreen( PlugFluidByproductContainer container, PlayerInventory inv, ITextComponent name ) {
        super( container, inv, name );

        TileEntityPlugBase tile = (TileEntityPlugBase) container.getTileEntity();
        GUI = BiomeDiversity.getId( "textures/gui/gui_blank.png" );

        PlugCollectorDetails collectorDetails = tile.getCollectorDetails();

        Text guiName = new Text( Translator.translateToLocal( ModBlocks.PLUG_FLUID_BYPRODUCT.getTranslationKey() ), RenderHelper.Justification.CENTRE, xSize );
        guiName.setPostion( TITLE_TEXT );
        controls.add( guiName );

        Text guiInventory = new Text( inv.getName().getString(), RenderHelper.Justification.LEFT, xSize );
        guiInventory.setPostion( new Point( 8, ySize - 94 ) );
        controls.add( guiInventory );

        Tank t = new Tank( collectorDetails.getTank( Tubes.Byproduct.tank() ) );
        t.setPostion( TANK_INPUT );
        controls.add( t );

    }

}
