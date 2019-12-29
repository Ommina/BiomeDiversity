package ommina.biomediversity.blocks.collector;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import ommina.biomediversity.BiomeDiversity;

import javax.annotation.Nullable;
import javax.vecmath.Point2f;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum Tubes {

    Unused0( 0, new Point2f( -4.5f, -11.5f ), null ),
    Unused1( 1, new Point2f( -11.5f, -4.5f ), null ),
    Unused2( 2, new Point2f( -11.5f, 16.5f ), null ),
    Cool( 3, new Point2f( -4.5f, 23.5f ), "coolbiometic" ),
    Warm( 4, new Point2f( 16.5f, 23.5f ), "warmbiometic" ),
    Unused5( 5, new Point2f( 23.5f, 16.5f ), null ),
    Unused6( 6, new Point2f( 23.5f, -4.5f ), null ),
    Byproduct( 7, new Point2f( 16.5f, -11.5f ), "byproduct" ),
    Unused8( 8, new Point2f( 6.5f, -7.5f ), null ),
    Unused9( 9, new Point2f( -7.5f, 6.5f ), null ),
    Unused10( 10, new Point2f( 6.5f, 20.5f ), null ),
    Unused11( 11, new Point2f( 20.5f, 6.5f ), null );

    final int tank;
    final Point2f location;
    final ResourceLocation fluid;

    Tubes( int tank, Point2f location, @Nullable String fluidRegistryName ) {

        this.tank = tank;
        this.location = location;
        this.fluid = fluidRegistryName == null ? null : BiomeDiversity.getId( fluidRegistryName );

    }

    Point2f getTesrLocation() {

        final float xoffset = 0;
        final float yoffset = 0;

        return new Point2f( location.x / 16f + xoffset, location.y / 16f + yoffset );

    }

    public int tank() {
        return tank;
    }

    @Nullable
    public Fluid fluid() {
        return ForgeRegistries.FLUIDS.getValue( fluid );
    }

    @Nullable
    public static Fluid fluid( int tank ) {
        return Arrays.stream( Tubes.values() ).filter( tube -> tube.tank == tank ).collect( Collectors.toList() ).get( 0 ).fluid();
    }

}
