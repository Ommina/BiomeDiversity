
package ommina.biomediversity.fluids;

import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
@ObjectHolder( "biomediversity" )
public class ModFluids {

    //@ObjectHolder( "coolbiometic_flowing" ) public static Fluid COOLBIOMETIC_FLOWING;
    //@ObjectHolder( "coolbiometic_bucket" ) public static Item COOLBIOMETIC_BUCKET;
    //@ObjectHolder( "coolbiometic" ) public static Block COOLBIOMETIC;

    //public static BdFluid coolBiometic;
    //public static BdFluid warmBiometic;
    //public static BdFluid neutralBiometic;

    //public static BdFluid natural;
    //public static BdFluid diluteNatural;

    //public static BdFluid moltenOrinocite;

    @ObjectHolder( "rainwater_still" ) public static FlowingFluid RAINWATER_STILL;
    @ObjectHolder( "rainwater_flowing" ) public static Fluid RAINWATER_FLOWING;
    @ObjectHolder( "rainwater_bucket" ) public static Item RAINWATER_BUCKET;
    //@ObjectHolder( "rainwater" ) public static Block RAINWATER_BLOCK;

    //public static BdFluid mineralWater;

    //public static BdFluid swampWater;
    //public static BdFluid murkyWater;
    //public static BdFluid sillWater;
    //public static BdFluid enrichedWater;

    //public static BdFluid jungleWater;

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


    //public static final List<BdFluid> fluids = new ArrayList<BdFluid>();

/*

    @SubscribeEvent
    public static void registerBlock( final RegistryEvent.Register<Block> event ) {

        FluidFactory.getBlocks().forEach( s -> event.getRegistry().register( s.getBlock() ) );

    }

    @SubscribeEvent
    public static void registerItem( final RegistryEvent.Register<Item> event ) {

        FluidFactory.getItems().forEach( s -> event.getRegistry().register( s.getItem() ) );

    }

    @SubscribeEvent
    public static void registerFluid( final RegistryEvent.Register<Fluid> event ) {

        FluidFactory.getFluids().forEach( s -> event.getRegistry().register( s.getFluid() ) );

    }

*/

/*


    public static void register() {


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
