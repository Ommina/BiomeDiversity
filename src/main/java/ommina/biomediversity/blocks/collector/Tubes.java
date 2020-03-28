package ommina.biomediversity.blocks.collector;

import net.minecraft.fluid.EmptyFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.fluids.SingleFluidRecipe;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public enum Tubes {

    Unused0( 0, new Point2D.Float( -4.5f, -11.5f ), null ),
    Unused1( 1, new Point2D.Float( -11.5f, -4.5f ), null ),
    Unused2( 2, new Point2D.Float( -11.5f, 16.5f ), null ),
    Cool( 3, new Point2D.Float( -4.5f, 23.5f ), "coolbiometic" ),
    Warm( 4, new Point2D.Float( 16.5f, 23.5f ), "warmbiometic" ),
    Unused5( 5, new Point2D.Float( 23.5f, 16.5f ), null ),
    Unused6( 6, new Point2D.Float( 23.5f, -4.5f ), null ),
    Byproduct( 7, new Point2D.Float( 16.5f, -11.5f ), "byproduct" ),
    Unused8( 8, new Point2D.Float( 6.5f, -7.5f ), null ),
    Unused9( 9, new Point2D.Float( -7.5f, 6.5f ), null ),
    Unused10( 10, new Point2D.Float( 6.5f, 20.5f ), null ),
    Unused11( 11, new Point2D.Float( 20.5f, 6.5f ), null );

    final int tank;
    final Point2D.Float location;
    final ResourceLocation fluid;

    Tubes( int tank, Point2D.Float location, @Nullable String fluidRegistryName ) {

        this.tank = tank;
        this.location = location;
        this.fluid = fluidRegistryName == null ? null : BiomeDiversity.getId( fluidRegistryName );

    }

    Point2D.Float getRendererLocation() {

        final float xoffset = 0;
        final float yoffset = 0;

        return new Point2D.Float( location.x / 16f + xoffset, location.y / 16f + yoffset );

    }

    public int tank() {
        return tank;
    }

    @Nullable
    public Fluid fluid() {

        Fluid f = ForgeRegistries.FLUIDS.getValue( fluid );

        if ( f instanceof EmptyFluid )
            return null;

        return f;

    }

    @Nullable
    public static Fluid fluid( int tank ) {
        return Arrays.stream( Tubes.values() ).filter( tube -> tube.tank == tank ).collect( Collectors.toList() ).get( 0 ).fluid();
    }

    public static Collection<SingleFluidRecipe> getRecipes() {
        return getAllFluids().stream().map( f -> new SingleFluidRecipe( f.getFluid() ) ).collect( Collectors.toList() );
    }

    public static List<FluidStack> getAllFluids() {

        List<FluidStack> stacks = new ArrayList<FluidStack>();

        for ( int n = 0; n <= 7; n++ )
            if ( Tubes.fluid( n ) != null )
                stacks.add( new FluidStack( Tubes.fluid( n ), 1 ) );

        return stacks;

    }

}
