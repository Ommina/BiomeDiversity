package ommina.biomediversity.blocks.mixer_advanced;

import com.mojang.blaze3d.matrix.MatrixStack;
import javafx.geometry.Point3D;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluid;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.fluids.ModFluids;
import ommina.biomediversity.rendering.Icosphere;
import ommina.biomediversity.rendering.RenderHelper;
import ommina.biomediversity.rendering.Triangle;

import java.util.List;

import static ommina.biomediversity.rendering.RenderHelper.getSprite;

public class RendererMixerAdvanced extends TileEntityRenderer<TileEntityMixerAdvanced> {

    private static TextureAtlasSprite spriteFaceBlank;
    private static TextureAtlasSprite spriteFaceFluid;

    private int rotate = 0;
    private int currentFace = 0;
    private int count = 0;
    private int currentSphere = 0;

    public RendererMixerAdvanced( final TileEntityRendererDispatcher tileEntityRendererDispatcher ) {
        super( tileEntityRendererDispatcher );
    }

    //region Overrides
    @Override
    public void render( TileEntityMixerAdvanced tile, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay ) {

        Fluid fluid = ModFluids.JUNGLEWATER;

        if ( spriteFaceBlank == null || spriteFaceFluid == null ) {
            spriteFaceFluid = getSprite( fluid.getAttributes().getStillTexture() );
            spriteFaceBlank = getSprite( BiomeDiversity.getId( "block/cluster/cluster_glow_external" ) );
        }

        Point3D p1;// = new Point3D( 0.5d, 0d, 0d );
        Point3D p2;// = new Point3D( 0.6d, 0.8d, 0.3d );
        Point3D p3;// = new Point3D( 0.7d, 0d, 0d );

        List<Triangle> triangles = Icosphere.getSphere( 2 );

        float c = 255.0f / triangles.size();

        for ( int n = 0; n < triangles.size(); n++ ) {

            //float[] r = new float[]{ (c * (float) n) / 255f, (c * (float) n) / 255f, (c * (float) n) / 255, 250f / 255f };
            float a = 200f;

            float[] r = new float[]{ a / 255f, a / 255f, a / 255f, 252f / 255f };

            if ( currentFace == n )
                r = new float[]{ 250f / 255f, 5f / 255f, 5f / 255, 250f / 255f };

            Triangle t = triangles.get( n );

            p1 = t.getP1();
            p2 = t.getP2();
            p3 = t.getP3();

            RenderHelper.drawTriangle( buffer, matrix, rotate, t, spriteFaceBlank, r );

        }

        count++;
        rotate++;

        if ( rotate >= 360 )
            rotate = 0;

        if ( count >= 50 ) {
            count = 0;
            //currentFace++;
            if ( currentFace >= triangles.size() ) {
                currentFace = 0;
                currentSphere++;
                if ( currentSphere > Icosphere.MAX_RECURSION )
                    currentSphere = 0;
            }

        }


    }

//endregion Overrides

}
