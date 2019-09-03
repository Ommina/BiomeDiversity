package ommina.biomediversity.fluids;


import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraftforge.fluids.FluidAttributes;
import ommina.biomediversity.BiomeDiversity;

import java.awt.*;
import java.util.List;
import java.util.*;

public class FluidFactory {

    private static Map<String, Fluid> fluids = new HashMap<>();
    private static Map<String, Item> items = new HashMap<>();
    private static Map<String, Block> blocks = new HashMap<>();


    private static List<FluidItem> fluidItems = new ArrayList<>();

    private static void init() {

        fluidItems.add( new FluidItem( "coolbiometic", getBuilder( "coolbiometic", "fluid_blank" ).viscosity( 2000 ).color( new Color( 43, 168, 225, 255 ).getRGB() ).rarity( Rarity.UNCOMMON ).build() ) );


        fluidItems.add( new FluidItem( "rainwater", getBuilder( "rainwater", "fluid_blank" ).color( new Color( 53, 83, 153, 192 ).getRGB() ).rarity( Rarity.COMMON ).build() ) );


    }

/*

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

 */

    public static void create() {

        if ( fluidItems.isEmpty() )
            init();

        for ( FluidItem fluiditem : fluidItems ) {

            String name = fluiditem.name;
            FluidAttributes attributes = fluiditem.getAttributes();

            BdFluid still = new BdFluid.Source( attributes );
            BdFluid flowing = new BdFluid.Flowing( attributes );

            still.setRegistryName( BiomeDiversity.getId( name + "_still" ) );
            flowing.setRegistryName( BiomeDiversity.getId( name + "_flowing" ) );

            fluids.put( still.getRegistryName().toString(), still );
            fluids.put( flowing.getRegistryName().toString(), flowing );

            still.setFluids( still, flowing );
            flowing.setFluids( still, flowing );

            Item bucket = new BucketItem( still, new Item.Properties().containerItem( Items.BUCKET ).maxStackSize( 1 ).group( BiomeDiversity.TAB ) ).setRegistryName( BiomeDiversity.getId( name + "_bucket" ) );

            items.put( bucket.getRegistryName().toString(), bucket );

            still.setBucketItem( bucket );
            flowing.setBucketItem( bucket );

            Block block = new FlowingFluidBlock( still, Block.Properties.create( Material.WATER ).doesNotBlockMovement().tickRandomly().hardnessAndResistance( 100f ).lightValue( 0 ).noDrops() ) {
            }.setRegistryName( BiomeDiversity.getId( name ) );

            blocks.put( block.getRegistryName().toString(), block );

            still.setBlock( block );
            flowing.setBlock( block );

        }

    }

    public static Collection<Fluid> getFluids() {
        return fluids.values();
    }

    public static Collection<Item> getItems() {
        return items.values();
    }

    public static Collection<Block> getBlocks() {
        return blocks.values();
    }

    private static FluidAttributes.Builder getBuilder( String name, String fluid ) {

        return getBuilder( name, fluid + "_still", fluid + "_flow" );

    }

    private static FluidAttributes.Builder getBuilder( String name, String still, String flowing ) {

        return FluidAttributes.builder( name, BiomeDiversity.getId( "block/fluid/" + still ), BiomeDiversity.getId( "block/fluid/" + flowing ) );

    }


/*

    FluidAttributes.builder(name,BiomeDiversity.getId("block/fluid/"+still ),BiomeDiversity.getId("block/fluid/"+flowing ))
    overlay( BiomeDiversity.getId("block/fluid/"+overlay ) )
    color( colour.getRGB( ) )
    density( density )
    temperature( temperature )
    luminosity( luminosity )
    viscosity( viscosity )
    rarity( rarity )
    build();

*/

    public static final class FluidItem {

        private String name;
        private FluidAttributes attributes;

        public FluidItem( String name, FluidAttributes attributes ) {

            this.name = name;
            this.attributes = attributes;

        }

        public FluidAttributes getAttributes() {
            return attributes;
        }

    }

}
