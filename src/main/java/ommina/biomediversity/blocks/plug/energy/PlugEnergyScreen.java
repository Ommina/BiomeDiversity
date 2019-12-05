package ommina.biomediversity.blocks.plug.energy;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.blocks.plug.PlugCollectorDetails;
import ommina.biomediversity.blocks.plug.TileEntityPlug;
import ommina.biomediversity.gui.BaseContainerScreen;
import ommina.biomediversity.gui.controls.RfGauge;
import ommina.biomediversity.gui.controls.Sprite;
import ommina.biomediversity.gui.controls.Temperature;
import ommina.biomediversity.gui.controls.Text;
import ommina.biomediversity.util.Translator;

import java.awt.*;

@OnlyIn( Dist.CLIENT )
public class PlugEnergyScreen extends BaseContainerScreen<PlugEnergyContainer> {

    private static final Point POWER_GAUGE = new Point( 160, 15 );
    private static final Point SPRITE_BIOME = new Point( 8, 15 );
    private static final Point UNIQUE_BIOMECOUNT_TEXT = new Point( 29, 15 );
    private static final Point RF_PER_TICK_TEXT = new Point( 29, 33 );
    private static final Point RF = new Point( 160, 15 );
    private static final Point TEMPERATURE_GAUGE = new Point( 133, 15 );

    public PlugEnergyScreen( PlugEnergyContainer container, PlayerInventory inv, ITextComponent name ) {
        super( container, inv, name );

        TileEntityPlug tile = (TileEntityPlug) container.getTileEntity();
        GUI = BiomeDiversity.getId( "textures/gui/gui_blank.png" );

        PlugCollectorDetails collectorDetails = tile.getCollectorDetails();

        Text guiName = new Text( Translator.translateToLocal( ModBlocks.PLUG_ENERGY.getTranslationKey() ), Text.Justification.CENTRE, xSize );
        guiName.setPostion( TITLE_TEXT );
        controls.add( guiName );

        Text guiInventory = new Text( inv.getName().getString(), Text.Justification.LEFT, xSize );
        guiInventory.setPostion( new Point( 8, ySize - 94 ) );
        controls.add( guiInventory );

        Temperature temperature = new Temperature( tile, "getTemperature", -5f, 5f );
        temperature.setPostion( TEMPERATURE_GAUGE );
        controls.add( temperature );

        RfGauge rf = new RfGauge( collectorDetails.getEnergyStorage() );
        rf.setPostion( RF );
        controls.add( rf );

        Sprite biome = new Sprite( BiomeDiversity.getId( "textures/gui/biome.png" ) );
        biome.setPostion( SPRITE_BIOME ).setWidth( 16 ).setHeight( 16 );
        controls.add( biome );

        int n = collectorDetails.getUniqueBiomeCount();

        if ( n > 0 ) {
            Text uniqueBiomeCount = new Text( Translator.translateToLocalFormatted( "text.biomediversity.gui.uniquebiomecount", n, n > 1 ? "s" : "" ), Text.Justification.LEFT, xSize );
            uniqueBiomeCount.setPostion( UNIQUE_BIOMECOUNT_TEXT );
            controls.add( uniqueBiomeCount );
        }

        Text rfReleasedPerTick = new Text( Translator.translateToLocalFormatted( "text.biomediversity.gui.rfpertick", collectorDetails.getRfReleasedPerTick() ), Text.Justification.LEFT, xSize );
        rfReleasedPerTick.setPostion( RF_PER_TICK_TEXT );
        controls.add( rfReleasedPerTick );

    }

}
