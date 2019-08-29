
package ommina.biomediversity.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.Rarity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fluids.FluidAttributes;
import ommina.biomediversity.BiomeDiversity;

import java.awt.*;


public class BdFluid extends WaterFluid.Source {

    protected static SoundEvent emptySound = SoundEvents.ITEM_BUCKET_EMPTY;
    protected static SoundEvent fillSound = SoundEvents.ITEM_BUCKET_FILL;
    protected static Material material = Material.WATER;

    private FluidAttributes attributes;

    public BdFluid( FluidAttributes attributes ) {

        this.attributes = attributes;

    }

    public BdFluid( String name, String still, String flowing, String overlay, Color colour, int density, int temperature, int luminosity, int viscosity, Block block, Rarity rarity ) {

        this.attributes = FluidAttributes.builder( name, BiomeDiversity.getId( "block/fluid/" + still ), BiomeDiversity.getId( "block/fluid/" + flowing ) )
             .overlay( BiomeDiversity.getId( "block/fluid/" + overlay ) )
             .color( colour.getRGB() )
             .density( density )
             .temperature( temperature )
             .luminosity( luminosity )
             .viscosity( viscosity )
             .rarity( rarity )
             .block( () -> block )
             .build();

    }

    @Override
    protected FluidAttributes createAttributes( Fluid fluid ) {

        return this.attributes;

    }


    /*

        @Override
        protected FluidAttributes createAttributes( Fluid fluid ) {

            return attributes;

            if ( fluid instanceof EmptyFluid )
                return net.minecraftforge.fluids.FluidAttributes.builder( "empty", null, null )
                     .vanillaColor().density( 0 ).temperature( 0 ).luminosity( 0 ).viscosity( 0 ).density( 0 ).build();
            if ( fluid instanceof WaterFluid )
                return net.minecraftforge.fluids.FluidAttributes.builder( "water",
                     new net.minecraft.util.ResourceLocation( "block/water_still" ),
                     new net.minecraft.util.ResourceLocation( "block/water_flow" ) )
                     .overlay( new net.minecraft.util.ResourceLocation( "block/water_overlay" ) )
                     .vanillaColor().block( () -> net.minecraft.block.Blocks.WATER ).build();
            if ( fluid instanceof LavaFluid )
                return net.minecraftforge.fluids.FluidAttributes.builder( "lava",
                     new net.minecraft.util.ResourceLocation( "block/lava_still" ),
                     new net.minecraft.util.ResourceLocation( "block/lava_flow" ) )
                     .block( () -> net.minecraft.block.Blocks.LAVA )
                     .vanillaColor().luminosity( 15 ).density( 3000 ).viscosity( 6000 ).temperature( 1300 ).build();
            throw new RuntimeException( "Mod fluids must override createAttributes." );
        }
    */

}
