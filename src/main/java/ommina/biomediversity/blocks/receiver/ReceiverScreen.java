package ommina.biomediversity.blocks.receiver;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.gui.BaseContainerScreen;
import ommina.biomediversity.gui.controls.RfGauge;
import ommina.biomediversity.gui.controls.Tank;
import ommina.biomediversity.gui.controls.Temperature;
import ommina.biomediversity.gui.controls.Text;
import ommina.biomediversity.util.Translator;

import java.awt.*;

@OnlyIn( Dist.CLIENT )
public class ReceiverScreen extends BaseContainerScreen<ReceiverContainer> {

    private static final Point POWER_GAUGE = new Point( 160, 15 );
    private static final Point TITLE_TEXT = new Point( 0, 5 );
    private static final Point TANK_INPUT = new Point( 8, 15 );
    private static final Point BIOMENAME_TEXT = new Point( 27, 18 );
    private static final Point TEMPERATURE_GAUGE = new Point( 133, 15 );

    public ReceiverScreen( ReceiverContainer container, PlayerInventory inv, ITextComponent name ) {
        super( container, inv, name );

        TileEntityReceiver receiver = (TileEntityReceiver) container.getTileEntity();
        GUI = BiomeDiversity.getId( "textures/gui/receiver.png" );

        Text guiName = new Text( Translator.translateToLocal( ModBlocks.RECEIVER.getTranslationKey() ), Text.Justification.CENTRE, xSize );
        guiName.setPostion( TITLE_TEXT );
        controls.add( guiName );

        Text guiInventory = new Text( inv.getName().getString(), Text.Justification.LEFT, xSize );
        guiInventory.setPostion( new Point( 8, ySize - 94 ) );
        controls.add( guiInventory );

        Temperature temp = new Temperature( receiver, "clientTemperature", -2f, 2f );
        temp.setPostion( TEMPERATURE_GAUGE );
        controls.add( temp );

        //Text biomeName = new Text( ForgeRegistries.BIOMES.getValue(  ) Biome.BIOMES.g  Biome.getBiomeForId(  receiver.getBiomeId() ).getBiomeName() + " " + Integer.toString( receiver.getBiomeId() ), Text.Justification.LEFT, xSize );
        //biomeName.setPostion( BIOMENAME_TEXT );
        //controls.add( biomeName );

        Tank t = new Tank( receiver.getTank( 0 ) );
        t.setPostion( TANK_INPUT );
        controls.add( t );

        RfGauge rf = new RfGauge( receiver.clientGetBattery() );
        rf.setPostion( POWER_GAUGE );
        controls.add( rf );


    }

}
