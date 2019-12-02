package ommina.biomediversity.blocks.transmitter;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.gui.BaseContainerScreen;
import ommina.biomediversity.gui.controls.RfGauge;
import ommina.biomediversity.gui.controls.Tank;
import ommina.biomediversity.gui.controls.Temperature;
import ommina.biomediversity.gui.controls.Text;
import ommina.biomediversity.util.Translator;

import java.awt.*;

public class TransmitterScreen extends BaseContainerScreen<TransmitterContainer> {

    private static final Point POWER_GAUGE = new Point( 160, 15 );
    private static final Point TANK_INPUT = new Point( 8, 15 );
    private static final Point BIOMENAME_TEXT = new Point( 27, 18 );
    private static final Point TEMPERATURE_GAUGE = new Point( 133, 15 );

    public TransmitterScreen( TransmitterContainer container, PlayerInventory inv, ITextComponent name ) {
        super( container, inv, name );

        TileEntityTransmitter transmitter = (TileEntityTransmitter) container.getTileEntity();
        GUI = BiomeDiversity.getId( "textures/gui/gui_blank.png" );

        Text guiName = new Text( Translator.translateToLocal( ModBlocks.TRANSMITTER.getTranslationKey() ), Text.Justification.CENTRE, xSize );
        guiName.setPostion( TITLE_TEXT );
        controls.add( guiName );

        Text guiInventory = new Text( inv.getName().getString(), Text.Justification.LEFT, xSize );
        guiInventory.setPostion( new Point( 8, ySize - 94 ) );
        controls.add( guiInventory );

        Temperature temp = new Temperature( transmitter, "getTemperature", -2f, 2f );
        temp.setPostion( TEMPERATURE_GAUGE );
        controls.add( temp );

        Biome biome = ForgeRegistries.BIOMES.getValue( ResourceLocation.tryCreate( transmitter.getBiomeRegistryName() ) );
        if ( biome != null ) {
            Text biomeName = new Text( biome.getDisplayName().getString(), Text.Justification.LEFT, xSize );
            biomeName.setPostion( BIOMENAME_TEXT );
            controls.add( biomeName );
        }

        Tank t = new Tank( transmitter.getTank( 0 ) );
        t.setPostion( TANK_INPUT );
        controls.add( t );

        //RfGauge rf = new RfGauge( transmitter.clientGetBattery() );
        //rf.setPostion( POWER_GAUGE );
        //controls.add( rf );

    }

}
