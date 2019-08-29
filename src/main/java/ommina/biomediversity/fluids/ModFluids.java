
package ommina.biomediversity.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Rarity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModBlocks;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class ModFluids {

    public static BdFluid coolBiometic;
    public static BdFluid warmBiometic;
    public static BdFluid neutralBiometic;

    public static BdFluid natural;
    public static BdFluid diluteNatural;

    public static BdFluid moltenOrinocite;

    @ObjectHolder( BiomeDiversity.MODID + ":rainwater" )
    public static BdFluid RAINWATER = new BdFluid( "rainwater", "fluid_blank", "fluid_blank", "fluid_blank", new Color( 53, 83, 153, 192 ), 1000, 300, 0, 1000, ModBlocks.RAINWATER, Rarity.COMMON );

    //rainWater = (BdFluid) register( "rainwater", "fluid_blank" ).setViscosity( 500 ).setLuminosity( 0 ).setColor( new Color( 53, 83, 153, 192 ) ).setRarity( EnumRarity.COMMON );

Block b = Blocks.WATER;

    public static BdFluid mineralWater;

    public static BdFluid swampWater;
    public static BdFluid murkyWater;
    public static BdFluid sillWater;
    public static BdFluid enrichedWater;

    public static BdFluid jungleWater;

    public static BdFluid freshWater;
    public static BdFluid purifiedWater;

    public static BdFluid paleDeliquescent;
    public static BdFluid scintillatingDeliquescent;
    public static BdFluid brightDeliquescent;
    public static BdFluid sparklingDeliquescent;

    public static BdFluid sparklingWater;
    public static BdFluid sparklingMineralWater;

    public static BdFluid dilutePaleMix;
    public static BdFluid diluteScintillatingMix;

    public static final List<BdFluid> fluids = new ArrayList<BdFluid>();

    @SubscribeEvent
    public static void register( final RegistryEvent.Register<Fluid> event ) {

        register( event, "rainwater", RAINWATER );

    }

    private static void register( final RegistryEvent.Register<Fluid> event, String name, Fluid fluid ) {

        fluid.setRegistryName( BiomeDiversity.getId( name ) );

        event.getRegistry().register( fluid );

    }



/*


    public static void register() {

        coolBiometic = (BdFluid) register( "coolbiometic", "fluid_blank" ).setViscosity( 2000 ).setLuminosity( 1 ).setColor( new Color( 43, 168, 226, 255 ) ).setRarity( EnumRarity.UNCOMMON );
        warmBiometic = (BdFluid) register( "warmbiometic", "fluid_blank" ).setViscosity( 500 ).setLuminosity( 15 ).setColor( new Color( 249, 79, 105, 255 ) ).setRarity( EnumRarity.UNCOMMON );
        neutralBiometic = (BdFluid) register( "neutralbiometic", "fluid_blank" ).setViscosity( 1000 ).setLuminosity( 7 ).setColor( new Color( 255, 244, 182, 255 ) ).setRarity( EnumRarity.EPIC );

        natural = (BdFluid) register( "natural", "viscous_blank" ).setViscosity( 30000 ).setLuminosity( 2 ).setColor( new Color( 142, 110, 41, 249 ) ).setRarity( EnumRarity.COMMON );
        diluteNatural = (BdFluid) register( "dilutenatural", "fluid_blank" ).setViscosity( 10000 ).setLuminosity( 2 ).setColor( new Color( 180, 142, 25, 128 ) ).setRarity( EnumRarity.UNCOMMON );

        moltenOrinocite = (BdFluid) register( "moltenorinocite", "molten_metal" ).setViscosity( 15000 ).setLuminosity( 10 ).setColor( new Color( 70, 166, 41, 255 ) ).setRarity( EnumRarity.COMMON );

        rainWater = (BdFluid) register( "rainwater", "fluid_blank" ).setViscosity( 500 ).setLuminosity( 0 ).setColor( new Color( 53, 83, 153, 192 ) ).setRarity( EnumRarity.COMMON );
        mineralWater = (BdFluid) register( "mineralwater", "fluid_blank" ).setViscosity( 500 ).setLuminosity( 0 ).setColor( new Color( 68, 126, 196, 224 ) ).setRarity( EnumRarity.COMMON );

        swampWater = (BdFluid) register( "biomepumpeda", "fluid_blank" ).setViscosity( 500 ).setLuminosity( 0 ).setColor( new Color( 122, 153, 86, 244 ) ).setRarity( EnumRarity.COMMON ); // Pumped from swamp
        murkyWater = (BdFluid) register( "murkywater", "fluid_blank" ).setViscosity( 500 ).setLuminosity( 0 ).setColor( new Color( 95, 139, 141, 244 ) ).setRarity( EnumRarity.UNCOMMON );
        sillWater = (BdFluid) register( "biomepumpedb", "fluid_blank" ).setViscosity( 500 ).setLuminosity( 0 ).setColor( new Color( 209, 177, 161, 250 ) ).setRarity( EnumRarity.UNCOMMON ); // Pumped from mesa
        enrichedWater = (BdFluid) register( "enrichedwater", "fluid_blank" ).setViscosity( 500 ).setLuminosity( 0 ).setColor( new Color( 77, 107, 78, 250 ) ).setRarity( EnumRarity.RARE );

        jungleWater = (BdFluid) register( "junglewater", "fluid_blank" ).setViscosity( 500 ).setLuminosity( 0 ).setColor( new Color( 59, 74, 15, 250 ) ).setRarity( EnumRarity.UNCOMMON );

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

    }

    private static BdFluid register( String fluidName, String textureName ) {

        BdFluid fluid = new BdFluid( fluidName, new ResourceLocation( Biomediversity.MODID, "blocks/" + textureName + "_still" ), new ResourceLocation( Biomediversity.MODID, "blocks/" + textureName + "_flow" ) );

        if ( FluidRegistry.registerFluid( fluid ) )
            FluidRegistry.addBucketForFluid( fluid );
        else
            fluid = (BdFluid) FluidRegistry.getFluid( fluidName );

        fluids.add( fluid );

        return fluid;

    }

    private static BdFluid mix( BdFluid fluidA, BdFluid fluidB, String textureName ) {

        String name = fluidA.getName() + fluidB.getName() + "mix";

        return (BdFluid) register( name, textureName )
                .setViscosity( MathUtil.mid( fluidA.getViscosity(), fluidB.getViscosity() ) )
                .setLuminosity( MathUtil.mid( fluidA.getLuminosity(), fluidB.getLuminosity() ) )
                .setColor( Utils.getColorMidpoint( fluidA.getColor(), fluidB.getColor() ) )
                .setRarity( Utils.getHighestRarity( fluidA.getRarity(), fluidB.getRarity() ) );

    }

*/

}
