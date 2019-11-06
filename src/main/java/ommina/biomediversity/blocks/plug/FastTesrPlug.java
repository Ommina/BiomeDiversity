package ommina.biomediversity.blocks.plug;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.animation.TileEntityRendererFast;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.tile.RenderHelper;

import java.awt.*;

import static ommina.biomediversity.blocks.tile.RenderHelper.*;

public class FastTesrPlug<T extends TileEntityPlug> extends TileEntityRendererFast<T> {

    private static final ResourceLocation INTERNAL_SPRITE = BiomeDiversity.getId( "block/cluster/cluster_glow_internal" );
    private static final ResourceLocation EXTERNAL_SPRITE = BiomeDiversity.getId( "block/cluster/cluster_glow_external" );

    private static final float WIDTH_FLUID = 14f / 16f;
    private static final float LENGTH_FLUID = 14f / 16f;
    private static final float HEIGHT_FLUID = 12f / 16f;

    private static final float HEIGHT_CONNECTION = 9f / 16f;

    private static final float[] COLOUR_DISCONNECTED = RenderHelper.getRGBA( new Color( 254, 0, 0, 255 ).getRGB() );
    private static final float[] COLOUR_CONNECTED = RenderHelper.getRGBA( new Color( 0, 200, 0, 255 ).getRGB() );

    //region Overrides
    @Override
    public void renderTileEntityFast( TileEntityPlug te, double x, double y, double z, float partialTicks, int destroyStage, BufferBuilder buffer ) {

        final double offset = 1f / 16f;

        PlugRenderData renderData = te.getPlugRenderData();

        float height = (HEIGHT_FLUID * ((float) renderData.value / (float) renderData.maximum));

        RenderHelper.renderCube( buffer, x + offset, y + offset, z + offset, WIDTH_FLUID, height, LENGTH_FLUID, renderData.sprite, COLOUR_CONNECTED, FACES_FLUID );

        y += 2f / 16f;

        final float side = 15f / 16f;

        float[] color = te.isClusterComponentConnected() ? COLOUR_CONNECTED : COLOUR_DISCONNECTED;

        renderLight( buffer, x, y, z, 1f / 16f / 2f, 1f / 16f / 2f, side, INTERNAL_SPRITE, color );
        renderLight( buffer, x, y, z, 0, 1f / 16f, side, EXTERNAL_SPRITE, color );

    }
//endregion Overrides

    private static void renderLight( BufferBuilder buffer, double x, double y, double z, float offset, float size, float side, ResourceLocation spriteResource, float[] color ) {

        RenderHelper.renderCube( buffer, x + offset, y, z + offset, size, HEIGHT_CONNECTION, size, spriteResource, color, FACES_NORTHWEST );
        RenderHelper.renderCube( buffer, x + side, y, z + offset, size, HEIGHT_CONNECTION, size, spriteResource, color, FACES_NORTHEAST );
        RenderHelper.renderCube( buffer, x + offset, y, z + side, size, HEIGHT_CONNECTION, size, spriteResource, color, FACES_SOUTHWEST );
        RenderHelper.renderCube( buffer, x + side, y, z + side, size, HEIGHT_CONNECTION, size, spriteResource, color, FACES_SOUTHEAST );

    }

}


