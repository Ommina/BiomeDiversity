package ommina.biomediversity.rendering;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import javafx.geometry.Point3D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import ommina.biomediversity.BiomeDiversity;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.EnumSet;
import java.util.stream.DoubleStream;

public class RenderHelper {

    public static final EnumSet<RenderHelper.Faces> FACES_FLUID = EnumSet.of( RenderHelper.Faces.TOP, RenderHelper.Faces.NORTH, RenderHelper.Faces.SOUTH, RenderHelper.Faces.WEST, RenderHelper.Faces.EAST );
    public static final EnumSet<RenderHelper.Faces> FACES_NORTHWEST = EnumSet.of( RenderHelper.Faces.NORTH, RenderHelper.Faces.WEST );
    public static final EnumSet<RenderHelper.Faces> FACES_NORTHEAST = EnumSet.of( RenderHelper.Faces.NORTH, RenderHelper.Faces.EAST );
    public static final EnumSet<RenderHelper.Faces> FACES_SOUTHWEST = EnumSet.of( RenderHelper.Faces.SOUTH, RenderHelper.Faces.WEST );
    public static final EnumSet<RenderHelper.Faces> FACES_SOUTHEAST = EnumSet.of( RenderHelper.Faces.SOUTH, RenderHelper.Faces.EAST );

    public static final RenderType TRIANGLE;

    static {

        RenderState.TransparencyState translucentTransparency = ObfuscationReflectionHelper.getPrivateValue( RenderState.class, null, "field_228515_g_" );
        RenderState.TextureState mipmapBlockAtlasTexture = new RenderState.TextureState( PlayerContainer.LOCATION_BLOCKS_TEXTURE, false, true );
        RenderState.LightmapState enableLightmap = new RenderState.LightmapState( true );

        RenderType.State glState = RenderType.State.getBuilder()
             .texture( mipmapBlockAtlasTexture )
             .transparency( translucentTransparency )
             .lightmap( enableLightmap )
             .build( true );

        TRIANGLE = RenderType.makeType( BiomeDiversity.getId( "triangle" ).toString(), DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, GL11.GL_TRIANGLES, 3, false, false, glState );

    }

    public enum Faces {
        TOP,
        BOTTOM,
        NORTH,
        SOUTH,
        WEST,
        EAST
    }

    public enum Justification {
        LEFT, CENTRE, RIGHT
    }

    public static float[] getRGBA( int color ) {
        return new float[]{ ((color >> 16) & 0xFF) / 255f, ((color >> 8) & 0xFF) / 255f, ((color) & 0xFF) / 255f, ((color >> 24) & 0xFF) / 255f };
    }

    public static TextureAtlasSprite getSprite( ResourceLocation resourceLocation ) {
        return Minecraft.getInstance().getAtlasSpriteGetter( PlayerContainer.LOCATION_BLOCKS_TEXTURE ).apply( resourceLocation );
    }

    /**
     * Draws a triangle to the provided buffer.
     * Points must be sent in a _counter-clockwise_ order
     */
    public static void drawTriangle( IRenderTypeBuffer buffer, MatrixStack matrix, int rotation, Triangle triangle, TextureAtlasSprite sprite, float[] rgba ) {

        IVertexBuilder builder = buffer.getBuffer( RenderHelper.TRIANGLE );

        Point3D p1 = triangle.getP1();
        Point3D p2 = triangle.getP2();
        Point3D p3 = triangle.getP3();

        Quaternion quaternion = new Quaternion( (float) rotation, (float) rotation, (float) rotation, true );

        matrix.push();
        matrix.translate( 0.5, 0.5, 0.5 );
        //matrix.rotate( quaternion );

        double texX = Math.min( 16.0, 16d * DoubleStream.of( Math.abs( p1.getX() - p2.getX() ), Math.abs( p1.getX() - p3.getX() ), Math.abs( p2.getX() - p3.getX() ) ).max().orElse( 1 ) );
        double texY = Math.min( 16.0, 16d * DoubleStream.of( Math.abs( p1.getY() - p2.getY() ), Math.abs( p1.getY() - p3.getY() ), Math.abs( p2.getY() - p3.getY() ) ).max().orElse( 1 ) );
        double texZ = Math.min( 16.0, 16d * DoubleStream.of( Math.abs( p1.getZ() - p2.getZ() ), Math.abs( p1.getZ() - p3.getZ() ), Math.abs( p2.getZ() - p3.getZ() ) ).max().orElse( 1 ) );

        builder.pos( matrix.getLast().getMatrix(), (float) p1.getX(), (float) p1.getY(), (float) p1.getZ() ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getInterpolatedU( texZ ), sprite.getMinV() ).lightmap( 0, 176 ).endVertex();
        builder.pos( matrix.getLast().getMatrix(), (float) p2.getX(), (float) p2.getY(), (float) p2.getZ() ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getInterpolatedU( texZ ), sprite.getInterpolatedV( texY ) ).lightmap( 0, 176 ).endVertex();
        builder.pos( matrix.getLast().getMatrix(), (float) p3.getX(), (float) p3.getY(), (float) p3.getZ() ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getMinV() ).lightmap( 0, 176 ).endVertex();

        matrix.pop();

    }

    public static void renderCube( IRenderTypeBuffer buffer, MatrixStack matrix, double x, double y, double z, float w, float h, float l, ResourceLocation resourceLocation, int color, EnumSet<Faces> faces ) {

        final TextureAtlasSprite sprite = getSprite( resourceLocation );
        final float[] rgba = getRGBA( color );

        renderCube( buffer, matrix, x, y, z, w, h, l, sprite, rgba, faces );

    }

    public static void renderCube( IRenderTypeBuffer buffer, MatrixStack matrix, double x, double y, double z, float w, float h, float l, ResourceLocation resourceLocation, float[] rgba, EnumSet<Faces> faces ) {
        renderCube( buffer, matrix, x, y, z, w, h, l, getSprite( resourceLocation ), rgba, faces );
    }

    public static void renderCube( IRenderTypeBuffer buffer, MatrixStack matrix, double x, double y, double z, float w, float h, float l, TextureAtlasSprite sprite, float[] rgba, EnumSet<Faces> faces ) {
        render( buffer, matrix, x, y, z, w, h, l, sprite, rgba, faces );
    }

    public static void renderCube( IRenderTypeBuffer buffer, MatrixStack matrix, double x, double y, double z, float w, float h, float l, FluidStack fluid, EnumSet<Faces> faces ) {
        renderCube( buffer, matrix, x, y, z, w, h, l, fluid.getFluid().getAttributes().getStillTexture(), fluid.getFluid().getAttributes().getColor(), faces );
    }

    public static void renderCube( IRenderTypeBuffer buffer, MatrixStack matrix, double x, double y, double z, float w, float h, float l, ResourceLocation resourceLocation, Color color, EnumSet<Faces> faces ) {

        final TextureAtlasSprite sprite = getSprite( resourceLocation );
        final float[] rgba = getRGBA( color.getRGB() );

        renderCube( buffer, matrix, x, y, z, w, h, l, sprite, rgba, faces );

    }

    public static void render( IRenderTypeBuffer buffer, MatrixStack matrix, double x, double y, double z, float w, float h, float l, TextureAtlasSprite sprite, float[] rgba ) {

        double texY = Math.min( 16, h * 16f );
        double texX = Math.min( 16, w * 16f );
        double texZ = Math.min( 16, w * 16f );

        IVertexBuilder builder = buffer.getBuffer( RenderType.getTranslucent() );

        matrix.push();
        matrix.translate( x, y, z );

        builder.pos( matrix.getLast().getMatrix(), l, h, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getInterpolatedU( texZ ), sprite.getInterpolatedV( texY ) ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
        builder.pos( matrix.getLast().getMatrix(), l, 0, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getInterpolatedU( texZ ), sprite.getMinV() ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
        builder.pos( matrix.getLast().getMatrix(), 0, 0, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getMinV() ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
        builder.pos( matrix.getLast().getMatrix(), 0, h, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getInterpolatedV( texY ) ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();

        matrix.pop();

    }

    private static void render( IRenderTypeBuffer buffer, MatrixStack matrix, double x, double y, double z, float w, float h, float l, TextureAtlasSprite sprite, float[] rgba, EnumSet<Faces> faces ) {

        double texY = Math.min( 16, h * 16f );
        double texX = Math.min( 16, w * 16f );
        double texZ = Math.min( 16, w * 16f );

        IVertexBuilder builder = buffer.getBuffer( RenderType.getTranslucent() );

        matrix.push();
        matrix.translate( x, y, z );

        if ( faces.contains( Faces.NORTH ) ) {
            builder.pos( matrix.getLast().getMatrix(), l, h, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getInterpolatedU( texZ ), sprite.getInterpolatedV( texY ) ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
            builder.pos( matrix.getLast().getMatrix(), l, 0, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getInterpolatedU( texZ ), sprite.getMinV() ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
            builder.pos( matrix.getLast().getMatrix(), 0, 0, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getMinV() ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
            builder.pos( matrix.getLast().getMatrix(), 0, h, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getInterpolatedV( texY ) ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
        }

        if ( faces.contains( Faces.SOUTH ) ) {
            builder.pos( matrix.getLast().getMatrix(), 0, h, w ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getInterpolatedV( texY ) ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
            builder.pos( matrix.getLast().getMatrix(), 0, 0, w ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getMinV() ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
            builder.pos( matrix.getLast().getMatrix(), l, 0, w ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getInterpolatedU( texX ), sprite.getMinV() ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
            builder.pos( matrix.getLast().getMatrix(), l, h, w ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getInterpolatedU( texX ), sprite.getInterpolatedV( texY ) ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
        }

        if ( faces.contains( Faces.WEST ) ) {
            builder.pos( matrix.getLast().getMatrix(), 0, 0, w ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getInterpolatedU( texX ), sprite.getMinV() ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
            builder.pos( matrix.getLast().getMatrix(), 0, h, w ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getInterpolatedU( texX ), sprite.getInterpolatedV( texY ) ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
            builder.pos( matrix.getLast().getMatrix(), 0, h, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getInterpolatedV( texY ) ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
            builder.pos( matrix.getLast().getMatrix(), 0, 0, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getMinV() ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
        }

        if ( faces.contains( Faces.EAST ) ) {
            builder.pos( matrix.getLast().getMatrix(), l, 0, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getMinV() ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
            builder.pos( matrix.getLast().getMatrix(), l, h, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getInterpolatedV( texY ) ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
            builder.pos( matrix.getLast().getMatrix(), l, h, w ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getInterpolatedU( texX ), sprite.getInterpolatedV( texY ) ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
            builder.pos( matrix.getLast().getMatrix(), l, 0, w ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getInterpolatedU( texX ), sprite.getMinV() ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
        }

        if ( faces.contains( Faces.TOP ) ) {
            builder.pos( matrix.getLast().getMatrix(), 0, h, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getMinV() ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
            builder.pos( matrix.getLast().getMatrix(), 0, h, w ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMaxU(), sprite.getMinV() ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
            builder.pos( matrix.getLast().getMatrix(), l, h, w ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMaxU(), sprite.getMaxV() ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
            builder.pos( matrix.getLast().getMatrix(), l, h, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getMaxV() ).lightmap( 0, 176 ).normal( 0, 1, 0 ).endVertex();
        }

        matrix.pop();

    }

    public static void drawText( String text, int left, int top, int width, Justification justification, int colour ) {

        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;

        if ( justification == Justification.CENTRE ) {
            left = width / 2 - fontRenderer.getStringWidth( text ) / 2;
        } else if ( justification == Justification.RIGHT ) {
            left = left + width - fontRenderer.getStringWidth( text );
        }

        fontRenderer.drawString( text, left, top, colour );

    }

/*

    public static void renderPowerBar( BufferBuilder buffer, TextureAtlasSprite texturePower, float height, float barCount, float originX, float originZ, float lengthX, float lengthZ ) {

        float w = texturePower.getIconWidth() / (texturePower.getMaxU() - texturePower.getMinU());

        final float BAR_WIDTH = 1.0f / w;

        float pbMinU = texturePower.getMinU() + barCount * BAR_WIDTH;
        float pbMaxU = texturePower.getMinU() + barCount * BAR_WIDTH + BAR_WIDTH;

        buffer.pos( originX, 0, originZ ).color( 1f, 1f, 1f, 1f ).tex( pbMinU, texturePower.getMaxV() ).lightmap( 0, 176 ).endVertex();
        buffer.pos( originX, height, originZ ).color( 1f, 1f, 1f, 1f ).tex( pbMinU, texturePower.getMinV() ).lightmap( 0, 176 ).endVertex();
        buffer.pos( originX + lengthX, height, originZ + lengthZ ).color( 1f, 1f, 1f, 1f ).tex( pbMaxU, texturePower.getMinV() ).lightmap( 0, 176 ).endVertex();
        buffer.pos( originX + lengthX, 0, originZ + lengthZ ).color( 1f, 1f, 1f, 1f ).tex( pbMaxU, texturePower.getMaxV() ).lightmap( 0, 176 ).endVertex();

    }


    public static void renderCube( BufferBuilder buffer, double x, double y, double z, float w, float h, float l, String texture, EnumSet<Faces> faces ) {

        final float low = w;
        final float high = w + l;

        buffer.setTranslation( x, y, z );

        TextureAtlasSprite text = ((AtlasTexture) Minecraft.getInstance().getTextureManager().getTexture( new ResourceLocation( "minecraft:water_still" ) )).getAtlasSprite( "minecraft:water_still" );


        //TextureAtlasSprite text = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite( texture );

        double posY = h;

        if ( faces.contains( Faces.TOP ) ) {
            buffer.pos( low, posY, low ).color( 1f, 1f, 1f, 1f ).tex( text.getMinU(), text.getMinV() ).lightmap( 0, 176 ).endVertex();
            buffer.pos( low, posY, high ).color( 1f, 1f, 1f, 1f ).tex( text.getMaxU(), text.getMinV() ).lightmap( 0, 176 ).endVertex();
            buffer.pos( high, posY, high ).color( 1f, 1f, 1f, 1f ).tex( text.getMaxU(), text.getMaxV() ).lightmap( 0, 176 ).endVertex();
            buffer.pos( high, posY, low ).color( 1f, 1f, 1f, 1f ).tex( text.getMinU(), text.getMaxV() ).lightmap( 0, 176 ).endVertex();
        }

    }

*/


/*


        if ( faces.contains( Faces.NORTH ) ) {
            buffer.pos( low, 0, low ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( still.getMinU(), still.getInterpolatedV( texY ) ).lightmap( 0, 176 ).endVertex();
            buffer.pos( low, posY, low ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( still.getMinU(), still.getMinV() ).lightmap( 0, 176 ).endVertex();
            buffer.pos( high, posY, low ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( still.getMaxU(), still.getMinV() ).lightmap( 0, 176 ).endVertex();
            buffer.pos( high, 0, low ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( still.getMaxU(), still.getInterpolatedV( texY ) ).lightmap( 0, 176 ).endVertex();
        }


        // South
        buffer.pos( low, 0, high ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( still.getMinU(), still.getMinV() ).lightmap( 0, 176 ).endVertex();
        buffer.pos( high, 0, high ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( still.getMaxU(), still.getMinV() ).lightmap( 0, 176 ).endVertex();
        buffer.pos( high, posY, high ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( still.getMaxU(), still.getInterpolatedV( texY ) ).lightmap( 0, 176 ).endVertex();
        buffer.pos( low, posY, high ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( still.getMinU(), still.getInterpolatedV( texY ) ).lightmap( 0, 176 ).endVertex();

        // East
        buffer.pos( high, 0, low ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( still.getMinU(), still.getInterpolatedV( texY ) ).lightmap( 0, 176 ).endVertex();
        buffer.pos( high, posY, low ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( still.getMinU(), still.getMinV() ).lightmap( 0, 176 ).endVertex();
        buffer.pos( high, posY, high ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( still.getMaxU(), still.getMinV() ).lightmap( 0, 176 ).endVertex();
        buffer.pos( high, 0, high ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( still.getMaxU(), still.getInterpolatedV( texY ) ).lightmap( 0, 176 ).endVertex();

        // West
        buffer.pos( low, 0, low ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( still.getMinU(), still.getMinV() ).lightmap( 0, 176 ).endVertex();
        buffer.pos( low, 0, high ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( still.getMaxU(), still.getMinV() ).lightmap( 0, 176 ).endVertex();
        buffer.pos( low, posY, high ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( still.getMaxU(), still.getInterpolatedV( texY ) ).lightmap( 0, 176 ).endVertex();
        buffer.pos( low, posY, low ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( still.getMinU(), still.getInterpolatedV( texY ) ).lightmap( 0, 176 ).endVertex();

 */


}
