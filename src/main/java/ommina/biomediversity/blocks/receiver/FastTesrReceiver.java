package ommina.biomediversity.blocks.receiver;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.tile.RenderHelper;
import ommina.biomediversity.config.Config;

import javax.annotation.Nonnull;

import static ommina.biomediversity.blocks.tile.RenderHelper.*;

public class FastTesrReceiver<T extends TileEntityReceiver> extends TileEntityRenderer<T> {

    private static final ResourceLocation INTERNAL_SPRITE = BiomeDiversity.getId( "block/cluster/cluster_glow_internal" );
    private static final ResourceLocation EXTERNAL_SPRITE = BiomeDiversity.getId( "block/cluster/cluster_glow_external" );

    private static final float WIDTH_FLUID = 14f / 16f;
    private static final float LENGTH_FLUID = 14f / 16f;
    private static final float HEIGHT_FLUID = 24f / 16f;

    private static final float HEIGHT_CONNECTION = 21f / 16f;

    public FastTesrReceiver( final TileEntityRendererDispatcher tileEntityRendererDispatcher ) {
        super( tileEntityRendererDispatcher );
    }

    //region Overrides
    @Override
    public void render( @Nonnull T tile, float partialTick, @Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer renderer, int light, int overlayLight ) {

        FluidStack fluid = tile.getTank( 0 ).getFluid();

        if ( !fluid.isEmpty() ) {

            final double offset = 1f / 16f;

            float height = (HEIGHT_FLUID * ((float) fluid.getAmount() / (float) Config.transmitterCapacity.get())); // Yes, this IS supposed to be TransmitterCapacity (Receiver and Transmitter must share capacities)

            //RenderHelper.renderCube( buffer, x + offset, y + offset, z + offset, WIDTH_FLUID, height, LENGTH_FLUID, fluid, FACES_FLUID );

        }

        //y += 3f / 16f;

        final float side = 15f / 16f;

        float[] color = tile.getGlowbarColour();

        //renderLight( buffer, x, y, z, 1f / 16f / 2f, 1f / 16f / 2f, side, INTERNAL_SPRITE, color );
        //renderLight( buffer, x, y, z, 0, 1f / 16f, side, EXTERNAL_SPRITE, color );

    }
//endregion Overrides

    private static void renderLight( BufferBuilder buffer, double x, double y, double z, float offset, float size, float side, ResourceLocation spriteResource, float[] color ) {

        //RenderHelper.renderCube( buffer, x + offset, y, z + offset, size, HEIGHT_CONNECTION, size, spriteResource, color, FACES_NORTHWEST );
        //RenderHelper.renderCube( buffer, x + side, y, z + offset, size, HEIGHT_CONNECTION, size, spriteResource, color, FACES_NORTHEAST );
        //RenderHelper.renderCube( buffer, x + offset, y, z + side, size, HEIGHT_CONNECTION, size, spriteResource, color, FACES_SOUTHWEST );
        //RenderHelper.renderCube( buffer, x + side, y, z + side, size, HEIGHT_CONNECTION, size, spriteResource, color, FACES_SOUTHEAST );

    }

}
