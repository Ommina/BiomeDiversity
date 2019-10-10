package ommina.biomediversity.blocks.receiver;


import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.animation.TileEntityRendererFast;
import net.minecraftforge.fluids.FluidStack;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.tile.RenderHelper;
import ommina.biomediversity.config.Config;

import java.awt.*;
import java.util.EnumSet;

public class FastTesrReceiver<T extends TileEntityReceiver> extends TileEntityRendererFast<T> {

    private static final EnumSet<RenderHelper.Faces> FACES_FLUID = EnumSet.of( RenderHelper.Faces.TOP, RenderHelper.Faces.NORTH, RenderHelper.Faces.SOUTH, RenderHelper.Faces.WEST, RenderHelper.Faces.EAST );
    private static final EnumSet<RenderHelper.Faces> FACES_NORTHWEST = EnumSet.of( RenderHelper.Faces.NORTH, RenderHelper.Faces.WEST );
    private static final EnumSet<RenderHelper.Faces> FACES_NORTHEAST = EnumSet.of( RenderHelper.Faces.NORTH, RenderHelper.Faces.EAST );
    private static final EnumSet<RenderHelper.Faces> FACES_SOUTHWEST = EnumSet.of( RenderHelper.Faces.SOUTH, RenderHelper.Faces.WEST );
    private static final EnumSet<RenderHelper.Faces> FACES_SOUTHEAST = EnumSet.of( RenderHelper.Faces.SOUTH, RenderHelper.Faces.EAST );

    private static final ResourceLocation INTERNAL_SPRITE = BiomeDiversity.getId( "block/cluster/cluster_glow_internal" );
    private static final ResourceLocation EXTERNAL_SPRITE = BiomeDiversity.getId( "block/cluster/cluster_glow_external" );

    private static final float WIDTH_FLUID = 14f / 16f;
    private static final float LENGTH_FLUID = 14f / 16f;
    private static final float HEIGHT_FLUID = 24f / 16f;

    private static final float HEIGHT_CONNECTION = 21f / 16f;

    private static final float[] COLOUR_DISCONNECTED = RenderHelper.getRGBA( new Color( 254, 0, 0, 255 ).getRGB() );
    private static final float[] COLOUR_CONNECTED = RenderHelper.getRGBA( new Color( 0, 200, 0, 255 ).getRGB() );


//region Overrides

    @Override
    public void renderTileEntityFast( TileEntityReceiver te, double x, double y, double z, float partialTicks, int destroyStage, BufferBuilder buffer ) {

        FluidStack fluid = te.getTank( 0 ).getFluid();

        if ( !fluid.isEmpty() ) {

            final double offset = 1f / 16f;

            float height = (HEIGHT_FLUID * ((float) fluid.getAmount() / (float) Config.transmitterCapacity.get())); // Yes, this IS supposed to be TransmitterCapacity (Receiver and Transmitter must share capacities)

            RenderHelper.renderCube( buffer, x + offset, y + offset, z + offset, WIDTH_FLUID, height, LENGTH_FLUID, fluid, FACES_FLUID );

        }

        y += 3f / 16f;

        float offset = 1f / 16f / 2f;
        float size = 1f / 16f / 2f;
        float side = 15f / 16f;

        float[] color = te.isClusterComponentConnected() ? COLOUR_CONNECTED : COLOUR_DISCONNECTED;

        RenderHelper.renderCube( buffer, x + offset, y, z + offset, size, HEIGHT_CONNECTION, size, INTERNAL_SPRITE, color, FACES_NORTHWEST );
        RenderHelper.renderCube( buffer, x + side, y, z + offset, size, HEIGHT_CONNECTION, size, INTERNAL_SPRITE, color, FACES_NORTHEAST );
        RenderHelper.renderCube( buffer, x + offset, y, z + side, size, HEIGHT_CONNECTION, size, INTERNAL_SPRITE, color, FACES_SOUTHWEST );
        RenderHelper.renderCube( buffer, x + side, y, z + side, size, HEIGHT_CONNECTION, size, INTERNAL_SPRITE, color, FACES_SOUTHEAST );

        offset = 0;
        size = 1f / 16f;

        RenderHelper.renderCube( buffer, x + offset, y, z + offset, size, HEIGHT_CONNECTION, size, EXTERNAL_SPRITE, color, FACES_NORTHWEST );
        RenderHelper.renderCube( buffer, x + side, y, z + offset, size, HEIGHT_CONNECTION, size, EXTERNAL_SPRITE, color, FACES_NORTHEAST );
        RenderHelper.renderCube( buffer, x + offset, y, z + side, size, HEIGHT_CONNECTION, size, EXTERNAL_SPRITE, color, FACES_SOUTHWEST );
        RenderHelper.renderCube( buffer, x + side, y, z + side, size, HEIGHT_CONNECTION, size, EXTERNAL_SPRITE, color, FACES_SOUTHEAST );

    }

//endregion Overrides

}
