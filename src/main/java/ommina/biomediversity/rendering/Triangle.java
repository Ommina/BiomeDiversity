package ommina.biomediversity.rendering;

import javafx.geometry.Point3D;

public class Triangle implements Comparable<Triangle> {

    final private Point3D p1;
    final private Point3D p2;
    final private Point3D p3;

    public Triangle( final Point3D p1, final Point3D p2, final Point3D p3 ) {

        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;

    }

    //region Overrides
    @Override
    public int compareTo( Triangle o ) {

        final float MIN_DIFF = 0.02f; // Just give some wiggle room

        // We want the top of the sphere to be sorted first, so our first stop is the y-vertex with the largest value
        double yo = Math.max( o.p1.getY(), Math.max( o.p2.getY(), o.p3.getY() ) );
        double ythis = Math.max( p1.getY(), Math.max( p2.getY(), p3.getY() ) );

        if ( Math.abs( yo - ythis ) >= MIN_DIFF ) {
            if ( yo > ythis )
                return 1;
            else
                return -1;
        }

        // So both vertex are pretty much at the same y.  From here we're going to compare the angle created between the
        // X-axes (North-South) and (x,z) vertex.  Sort smallest values first.

        float a = 1.0f;

        float b1 = (float) Math.sqrt( Math.pow( p2.getX(), 2 ) + Math.pow( p2.getZ(), 2 ) );
        float c1 = (float) Math.sqrt( Math.pow( p2.getX(), 2 ) + Math.pow( 1d - p2.getZ(), 2 ) );

        float t1 = (float) (Math.acos( (Math.pow( a, 2 ) + Math.pow( b1, 2 ) - Math.pow( c1, 2 )) / 2.0 * a * b1 ) * (180 / Math.PI));

        float b2 = (float) Math.sqrt( Math.pow( o.p2.getX(), 2 ) + Math.pow( o.p2.getZ(), 2 ) );
        float c2 = (float) Math.sqrt( Math.pow( o.p2.getX(), 2 ) + Math.pow( 1d - o.p2.getZ(), 2 ) );

        float t2 = (float) (Math.acos( (Math.pow( a, 2 ) + Math.pow( b2, 2 ) - Math.pow( c2, 2 )) / 2.0 * a * b2 ) * (180 / Math.PI));

        if ( Math.abs( t1 - t2 ) > MIN_DIFF ) {
            if ( t1 > t2 ) {
                return 1;
            } else if ( t1 < t2 ) {
                return -1;
            }
        }

        return 0;

    }

//endregion Overrides

    public Point3D getP1() {
        return p1;
    }

    public Point3D getP2() {
        return p2;
    }

    public Point3D getP3() {
        return p3;
    }

}
