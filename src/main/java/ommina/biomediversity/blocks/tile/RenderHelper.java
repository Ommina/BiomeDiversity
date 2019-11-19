package ommina.biomediversity.blocks.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.awt.*;
import java.util.EnumSet;

public class RenderHelper {

    public static final EnumSet<RenderHelper.Faces> FACES_FLUID = EnumSet.of( RenderHelper.Faces.TOP, RenderHelper.Faces.NORTH, RenderHelper.Faces.SOUTH, RenderHelper.Faces.WEST, RenderHelper.Faces.EAST );
    public static final EnumSet<RenderHelper.Faces> FACES_NORTHWEST = EnumSet.of( RenderHelper.Faces.NORTH, RenderHelper.Faces.WEST );
    public static final EnumSet<RenderHelper.Faces> FACES_NORTHEAST = EnumSet.of( RenderHelper.Faces.NORTH, RenderHelper.Faces.EAST );
    public static final EnumSet<RenderHelper.Faces> FACES_SOUTHWEST = EnumSet.of( RenderHelper.Faces.SOUTH, RenderHelper.Faces.WEST );
    public static final EnumSet<RenderHelper.Faces> FACES_SOUTHEAST = EnumSet.of( RenderHelper.Faces.SOUTH, RenderHelper.Faces.EAST );

    public enum Faces {
        TOP,
        BOTTOM,
        NORTH,
        SOUTH,
        WEST,
        EAST
    }

    public static float[] getRGBA( int color ) {

        return new float[]{ ((color >> 16) & 0xFF) / 255f, ((color >> 8) & 0xFF) / 255f, ((color) & 0xFF) / 255f, ((color >> 24) & 0xFF) / 255f };

    }

    public static TextureAtlasSprite getSprite( ResourceLocation resourceLocation ) {

        return Minecraft.getInstance().getTextureMap().getSprite( resourceLocation );

    }

    public static void renderCube( BufferBuilder buffer, double x, double y, double z, float w, float h, float l, ResourceLocation resourceLocation, int color, EnumSet<Faces> faces ) {

        final TextureAtlasSprite sprite = getSprite( resourceLocation );
        final float[] rgba = getRGBA( color );

        renderCube( buffer, x, y, z, w, h, l, sprite, rgba, faces );

    }

    public static void renderCube( BufferBuilder buffer, double x, double y, double z, float w, float h, float l, ResourceLocation resourceLocation, float[] rgba, EnumSet<Faces> faces ) {

        final TextureAtlasSprite sprite = getSprite( resourceLocation );

        renderCube( buffer, x, y, z, w, h, l, sprite, rgba, faces );

    }

    public static void renderCube( BufferBuilder buffer, double x, double y, double z, float w, float h, float l, TextureAtlasSprite sprite, float[] rgba, EnumSet<Faces> faces ) {

        render( buffer, x, y, z, w, h, l, sprite, rgba, faces );

    }

    public static void renderCube( BufferBuilder buffer, double x, double y, double z, float w, float h, float l, FluidStack fluid, EnumSet<Faces> faces ) {

        renderCube( buffer, x, y, z, w, h, l, fluid.getFluid().getAttributes().getStillTexture(), fluid.getFluid().getAttributes().getColor(), faces );

    }

    public static void renderCube( BufferBuilder buffer, double x, double y, double z, float w, float h, float l, ResourceLocation resourceLocation, Color color, EnumSet<Faces> faces ) {

        final TextureAtlasSprite sprite = getSprite( resourceLocation );
        final float[] rgba = getRGBA( color.getRGB() );

        renderCube( buffer, x, y, z, w, h, l, sprite, rgba, faces );

    }

    private static void render( BufferBuilder buffer, double x, double y, double z, float w, float h, float l, TextureAtlasSprite sprite, float[] rgba, EnumSet<Faces> faces ) {

        double texY = Math.min( 16, h * 16f );

        buffer.setTranslation( x, y, z );

        if ( faces.contains( Faces.NORTH ) ) {
            buffer.pos( 0, h, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getInterpolatedV( texY ) ).lightmap( 0, 176 ).endVertex();
            buffer.pos( 0, 0, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getMinV() ).lightmap( 0, 176 ).endVertex();
            buffer.pos( l, 0, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMaxU(), sprite.getMinV() ).lightmap( 0, 176 ).endVertex();
            buffer.pos( l, h, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMaxU(), sprite.getInterpolatedV( texY ) ).lightmap( 0, 176 ).endVertex();
        }

        if ( faces.contains( Faces.SOUTH ) ) {
            buffer.pos( 0, h, w ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getInterpolatedV( texY ) ).lightmap( 0, 176 ).endVertex();
            buffer.pos( 0, 0, w ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getMinV() ).lightmap( 0, 176 ).endVertex();
            buffer.pos( l, 0, w ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMaxU(), sprite.getMinV() ).lightmap( 0, 176 ).endVertex();
            buffer.pos( l, h, w ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMaxU(), sprite.getInterpolatedV( texY ) ).lightmap( 0, 176 ).endVertex();
        }

        if ( faces.contains( Faces.WEST ) ) {
            buffer.pos( 0, 0, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getMinV() ).lightmap( 0, 176 ).endVertex();
            buffer.pos( 0, h, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getInterpolatedV( texY ) ).lightmap( 0, 176 ).endVertex();
            buffer.pos( 0, h, w ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMaxU(), sprite.getInterpolatedV( texY ) ).lightmap( 0, 176 ).endVertex();
            buffer.pos( 0, 0, w ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMaxU(), sprite.getMinV() ).lightmap( 0, 176 ).endVertex();
        }

        if ( faces.contains( Faces.EAST ) ) {
            buffer.pos( l, 0, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getMinV() ).lightmap( 0, 176 ).endVertex();
            buffer.pos( l, h, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getInterpolatedV( texY ) ).lightmap( 0, 176 ).endVertex();
            buffer.pos( l, h, w ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMaxU(), sprite.getInterpolatedV( texY ) ).lightmap( 0, 176 ).endVertex();
            buffer.pos( l, 0, w ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMaxU(), sprite.getMinV() ).lightmap( 0, 176 ).endVertex();
        }

        if ( faces.contains( Faces.TOP ) ) {
            buffer.pos( 0, h, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getMinV() ).lightmap( 0, 176 ).endVertex();
            buffer.pos( 0, h, w ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMaxU(), sprite.getMinV() ).lightmap( 0, 176 ).endVertex();
            buffer.pos( l, h, w ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMaxU(), sprite.getMaxV() ).lightmap( 0, 176 ).endVertex();
            buffer.pos( l, h, 0 ).color( rgba[0], rgba[1], rgba[2], rgba[3] ).tex( sprite.getMinU(), sprite.getMaxV() ).lightmap( 0, 176 ).endVertex();
        }

        //buffer.sortVertexData( Minecraft.getInstance().player.posX, Minecraft.getInstance().player.posY, Minecraft.getInstance().player.posZ);

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
