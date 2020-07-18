package ommina.biomediversity.rendering.mollertrumbore;

import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;

public class MollerTrumbore {

    private static final double EPSILON = 0.0000001;

    public static boolean rayTriangleIntersect( Vec3f origin, BlockRayTraceResult hit, Vec3f v0, Vec3f v1, Vec3f v2 ) {

        Vec3f v0v1 = v1.sub( v0 );
        Vec3f v0v2 = v2.sub( v0 );
        Ray r = new Ray( origin, new Vec3f( hit.getHitVec() ) );
        Vec3f pvec = r.dir.cross( v0v2 );

        double det = v0v1.dot( pvec );

        if ( det < 0.000001 )
            return false;

        Vec3f tvec = r.orig.sub( v0 );
        float invDet = (float) (1.0 / det);
        double u = tvec.dot( pvec ) * invDet;

        if ( u < 0 || u > 1 )
            return false;

        Vec3f qvec = tvec.cross( v0v1 );
        double v = r.dir.dot( qvec ) * invDet;

        if ( v < 0 || u + v > 1 )
            return false;

        return ((v0v2.dot( qvec ) * invDet) >= 0);

    }


    public static boolean attemptTwo( Vec3f origin, BlockRayTraceResult hit, Vec3f p1, Vec3f p2, Vec3f p3 ) {

        Vec3d q1 = hit.getHitVec().mul( 3, 3, 3 );


        //Let p1,p2,p3 denote your triangle

        //Pick two points q1,q2 on the line very far away in both directions.

        //Let SignedVolume(a,b,c,d) denote the signed volume of the tetrahedron a,b,c,d.

        //If SignedVolume(q1,p1,p2,p3) and SignedVolume(q2,p1,p2,p3) have different signs AND SignedVolume(q1,q2,p1,p2), SignedVolume(q1,q2,p2,p3) and SignedVolume(q1,q2,p3,p1) have the same sign, then there is an intersection.


        return false;

    }

    private static int SignedVolume( Vec3f a, Vec3f b, Vec3f c, Vec3f d ) {

        Vec3f ba = b.sub( a );
        Vec3f ca = c.sub( a );
        Vec3f da = d.sub( a );
        Vec3f baca = ba.cross( ca );

        double baca_da = baca.dot( da );
        double sv = (1.0f / 6.0f) * baca_da;

        //SignedVolume( a, b, c, d ) = (1.0 / 6.0) * dot( cross( b - a, c - a ), d - a )

        return 0;

    }

}