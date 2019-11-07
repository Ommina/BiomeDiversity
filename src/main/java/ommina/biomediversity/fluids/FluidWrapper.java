package ommina.biomediversity.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import ommina.biomediversity.BiomeDiversity;

import java.awt.*;
import java.util.function.Supplier;

public class FluidWrapper {

    private Supplier<FlowingFluid> fluid_still;
    private Supplier<FlowingFluid> fluid_flowing;
    private Supplier<FlowingFluidBlock> block;
    private Supplier<Item> bucket;

    private ForgeFlowingFluid.Properties fluid_properties;

    private String name;
    private ResourceLocation stillTexture;
    private ResourceLocation flowingTexture;

    private int luminosity = 0;
    private int density = 1000;
    private int temperature = 300;
    private int viscosity = 1000;
    private Rarity rarity = Rarity.COMMON;
    private Color color;

    public FluidWrapper( String name, String textureName ) {

        this.name = name;
        this.stillTexture = BiomeDiversity.getId( "block/fluid/" + textureName + "_still" );
        this.flowingTexture = BiomeDiversity.getId( "block/fluid/" + textureName + "_flow" );

    }

    public FluidWrapper build() {

        fluid_still = DeferredRegistration.FLUIDS.register( name, () -> new ForgeFlowingFluid.Source( fluid_properties ) );
        fluid_flowing = DeferredRegistration.FLUIDS.register( name + "_flowing", () -> new ForgeFlowingFluid.Flowing( fluid_properties ) );
        block = DeferredRegistration.BLOCKS.register( name, () -> new FlowingFluidBlock( fluid_still, Block.Properties.create( Material.WATER ).doesNotBlockMovement().hardnessAndResistance( 100f ).lightValue( luminosity ).noDrops() ) );
        bucket = DeferredRegistration.ITEMS.register( name + "_bucket", () -> new BucketItem( fluid_still, new Item.Properties().containerItem( Items.BUCKET ).maxStackSize( 1 ).group( BiomeDiversity.TAB ) ) );
        fluid_properties = new ForgeFlowingFluid.Properties( fluid_still, fluid_flowing,
             FluidAttributes.builder( stillTexture, flowingTexture ).color( color.getRGB() ).luminosity( luminosity ).density( density ).temperature( temperature ).viscosity( viscosity ).rarity( rarity ) )
             .bucket( bucket ).block( block );

        return this;

    }

    public FluidWrapper setColour( Color color ) {

        this.color = color;
        return this;

    }

    public FluidWrapper setLuminosity( int luminosity ) {

        this.luminosity = luminosity;
        return this;

    }

    public FluidWrapper setDensity( int density ) {

        this.density = density;
        return this;

    }

    public FluidWrapper setTemperature( int temperature ) {

        this.temperature = temperature;
        return this;

    }

    public FluidWrapper setViscosity( int viscosity ) {

        this.viscosity = viscosity;
        return this;

    }

    public FluidWrapper setRarity( Rarity rarity ) {

        this.rarity = rarity;
        return this;

    }

}
