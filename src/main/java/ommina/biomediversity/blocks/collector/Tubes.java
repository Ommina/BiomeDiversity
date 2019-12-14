package ommina.biomediversity.blocks.collector;

import javax.vecmath.Point2f;

enum Tubes {

    Unused0( 0, new Point2f( -4.5f, -12f ) ),
    Unused1( 1, new Point2f( -11.5f, -5 ) ),
    Unused2( 2, new Point2f( -11.5f, 16f ) ),
    Cool( 3, new Point2f( -4.5f, 23f ) ),
    Warm( 4, new Point2f( 16.5f, 23f ) ),
    Unused5( 5, new Point2f( 23.5f, 16f ) ),
    Unused6( 6, new Point2f( 23.5f, -5f ) ),
    Byproduct( 7, new Point2f( 16.5f, -12f ) ),
    Unused8( 8, new Point2f( 6.5f, -8f ) ),
    Unused9( 9, new Point2f( -7.5f, 6f ) ),
    Unused10( 10, new Point2f( 6.5f, 20f ) ),
    Unused11( 11, new Point2f( 20.5f, 6f ) );

    final int tank;
    final Point2f location;

    Tubes( int tank, Point2f location ) {
        this.tank = tank;
        this.location = location;
    }

    Point2f getTesrLocation() {

        final float xoffset = 0;
        final float yoffset = 0;

        return new Point2f( location.x / 16f + xoffset, location.y / 16f + yoffset );

    }

}
