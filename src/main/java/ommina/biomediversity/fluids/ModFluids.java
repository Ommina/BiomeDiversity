package ommina.biomediversity.fluids;

import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.BiomeDiversity;

@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
@ObjectHolder( BiomeDiversity.MODID )
public class ModFluids {

    @ObjectHolder( "rainwater" ) public static FlowingFluid RAINWATER;                   // Produced by Rain Barrel TE

    @ObjectHolder( "coolbiometic" ) public static FlowingFluid COOLBIOMETIC;             // Produced by Collector (directly)
    @ObjectHolder( "warmbiometic" ) public static FlowingFluid WARMBIOMETIC;
    @ObjectHolder( "neutralbiometic" ) public static FlowingFluid NEUTRALBIOMETIC;       //                       (indirectly by Mixer)

    @ObjectHolder( "mineralwater" ) public static FlowingFluid MINERALWATER;             // WorldGen Spheres
    @ObjectHolder( "junglewater" ) public static FlowingFluid JUNGLEWATER;               //          Jungle Biome
    @ObjectHolder( "moltenorinocite" ) public static FlowingFluid MOLTENORINOCITE;       //          Melted Ore


    //public static BdFluid natural;
    //public static BdFluid diluteNatural;


    //public static BdFluid swampWater;
    //public static BdFluid murkyWater;
    //public static BdFluid sillWater;
    //public static BdFluid enrichedWater;


    //public static BdFluid freshWater;
    //public static BdFluid purifiedWater;

    //public static BdFluid paleDeliquescent;
    //public static BdFluid scintillatingDeliquescent;
    //public static BdFluid brightDeliquescent;
    //public static BdFluid sparklingDeliquescent;

    //public static BdFluid sparklingWater;
    //public static BdFluid sparklingMineralWater;

    //public static BdFluid dilutePaleMix;
    //public static BdFluid diluteScintillatingMix;

/*



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
