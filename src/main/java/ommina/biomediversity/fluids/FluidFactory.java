package ommina.biomediversity.fluids;


import net.minecraft.item.Rarity;
import net.minecraftforge.fluids.FluidAttributes;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FluidFactory {

    private static List<FluidWrapper> wrapper = new ArrayList<>();

    public static void init() {

        wrapper.add( new FluidWrapper( "rainwater", "fluid_blank" ).setViscosity( 500 ).setColour( new Color( 53, 83, 153, 192 ) ).setRarity( Rarity.COMMON ).build() );

        wrapper.add( new FluidWrapper( "coolbiometic", "molten_metal" ).setViscosity( 2000 ).setLuminosity( 5 ).setColour( new Color( 43, 168, 226, 255 ) ).setRarity( Rarity.UNCOMMON ).build() );
        wrapper.add( new FluidWrapper( "warmbiometic", "fluid_blank" ).setViscosity( 500 ).setLuminosity( 15 ).setColour( new Color( 249, 79, 105, 255 ) ).setRarity( Rarity.UNCOMMON ).build() );
        wrapper.add( new FluidWrapper( "neutralbiometic", "fluid_blank" ).setViscosity( 1000 ).setLuminosity( 7 ).setColour( new Color( 255, 244, 182, 255 ) ).setRarity( Rarity.EPIC ).build() );

        wrapper.add( new FluidWrapper( "mineralwater", "fluid_blank" ).setViscosity( 500 ).setColour( new Color( 68, 126, 129, 224 ) ).setRarity( Rarity.COMMON ).build() );
        wrapper.add( new FluidWrapper( "junglewater", "fluid_blank" ).setViscosity( 500 ).setColour( new Color( 59, 74, 15, 250 ) ).setRarity( Rarity.UNCOMMON ).build() );
        wrapper.add( new FluidWrapper( "montenorinocite", "molten_metal" ).setViscosity( 15000 ).setLuminosity( 10 ).setColour( new Color( 70, 166, 41, 255 ) ).setRarity( Rarity.COMMON ).build() );

    }


    //mineralWater = (BdFluid) register( "mineralwater", "fluid_blank" ).setViscosity( 500 ).setLuminosity( 0 ).setColor( new Color( 68, 126, 196, 224 ) ).setRarity( EnumRarity.COMMON );
    //jungleWater = (BdFluid) register( "junglewater", "fluid_blank" ).setViscosity( 500 ).setLuminosity( 0 ).setColor( new Color( 59, 74, 15, 250 ) ).setRarity( EnumRarity.UNCOMMON );
    //moltenOrinocite = (BdFluid) register( "moltenorinocite", "molten_metal" ).setViscosity( 15000 ).setLuminosity( 10 ).setColor( new Color( 70, 166, 41, 255 ) ).setRarity( EnumRarity.COMMON );

/*


        natural = (BdFluid) register( "natural", "viscous_blank" ).setViscosity( 30000 ).setLuminosity( 2 ).setColor( new Color( 142, 110, 41, 249 ) ).setRarity( EnumRarity.COMMON );
        diluteNatural = (BdFluid) register( "dilutenatural", "fluid_blank" ).setViscosity( 10000 ).setLuminosity( 2 ).setColor( new Color( 180, 142, 25, 128 ) ).setRarity( EnumRarity.UNCOMMON );




        swampWater = (BdFluid) register( "biomepumpeda", "fluid_blank" ).setViscosity( 500 ).setLuminosity( 0 ).setColor( new Color( 122, 153, 86, 244 ) ).setRarity( EnumRarity.COMMON ); // Pumped from swamp
        murkyWater = (BdFluid) register( "murkywater", "fluid_blank" ).setViscosity( 500 ).setLuminosity( 0 ).setColor( new Color( 95, 139, 141, 244 ) ).setRarity( EnumRarity.UNCOMMON );
        sillWater = (BdFluid) register( "biomepumpedb", "fluid_blank" ).setViscosity( 500 ).setLuminosity( 0 ).setColor( new Color( 209, 177, 161, 250 ) ).setRarity( EnumRarity.UNCOMMON ); // Pumped from mesa
        enrichedWater = (BdFluid) register( "enrichedwater", "fluid_blank" ).setViscosity( 500 ).setLuminosity( 0 ).setColor( new Color( 77, 107, 78, 250 ) ).setRarity( EnumRarity.RARE );


        freshWater = (BdFluid) register( "freshwater", "fluid_blank" ).setViscosity( 500 ).setLuminosity( 0 ).setColor( new Color( 61, 105, 175, 192 ) ).setRarity( EnumRarity.COMMON );
        purifiedWater = (BdFluid) register( "purifiedwater", "fluid_blank" ).setViscosity( 500 ).setLuminosity( 0 ).setColor( new Color( 89, 163, 255, 128 ) ).setRarity( EnumRarity.COMMON );

        paleDeliquescent = (BdFluid) register( "paledeliquescent", "fluid_blank" ).setViscosity( 500 ).setLuminosity( 0 ).setColor( new Color( 38, 127, 87, 128 ) ).setRarity( EnumRarity.UNCOMMON );
        brightDeliquescent = (BdFluid) register( "brightdeliquescent", "fluid_blank" ).setViscosity( 10000 ).setLuminosity( 0 ).setColor( new Color( 0, 255, 173, 160 ) ).setRarity( EnumRarity.RARE );
        sparklingDeliquescent = (BdFluid) register( "sparklingdeliquescent", "fluid_blank" ).setViscosity( 10000 ).setLuminosity( 0 ).setColor( new Color( 55, 249, 29, 192 ) ).setRarity( EnumRarity.RARE );
        scintillatingDeliquescent = (BdFluid) register( "scintillatingdeliquescent", "fluid_blank" ).setViscosity( 500 ).setLuminosity( 0 ).setColor( new Color( 179, 249, 164, 224 ) ).setRarity( EnumRarity.EPIC );

        sparklingWater = (BdFluid) register( "sparklingwater", "fluid_blank" ).setViscosity( 500 ).setLuminosity( 1 ).setColor( new Color( 133, 247, 190, 204 ) ).setRarity( EnumRarity.RARE );
        sparklingMineralWater = (BdFluid) register( "sparklingmineralwater", "fluid_blank" ).setViscosity( 500 ).setLuminosity( 1 ).setColor( new Color( 200, 225, 247, 174 ) ).setRarity( EnumRarity.EPIC );

        dilutePaleMix = mix( paleDeliquescent, diluteNatural, "fluid_blank" );
        diluteScintillatingMix = mix( scintillatingDeliquescent, diluteNatural, "fluid_blank" );

 */

/*

    }

public static Collection<Fluid> getFluids(){
     return fluids.values();
     }

public static Collection<Item> getItems(){
     return items.values();
     }

public static Collection<Block> getBlocks(){
     return blocks.values();
     }

*/

    public static final class FluidItem {

        private String name;
        private FluidAttributes attributes;

        public FluidItem( String name ) {

            this.name = name;

        }

        public FluidAttributes getAttributes() {
            return attributes;
        }

    }

}
