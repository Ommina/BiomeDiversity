package ommina.biomediversity.blocks.collector;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import ommina.biomediversity.util.MathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Rectangles {

    private static final Vec3d MUL = new Vec3d( 0.0625d, 0.0625d, 0.0625d );

    private final List<Rectangle> recs = new ArrayList<>( 32 );

    public class Rectangle {

        private final int tank;
        private final Vec3d S1;
        private final Vec3d S2;
        private final Vec3d S3;

        public Rectangle( final int tank, final Vec3d S1, final Vec3d S2, final Vec3d S3 ) {

            this.tank = tank;
            this.S1 = S1.mul( MUL );
            this.S2 = this.S1.add( S2.mul( MUL ) );
            this.S3 = this.S1.add( S3.mul( MUL ) );

        }

        /**
         * Euclidean distance between this and the specified vector, returned as double.
         */
        public double distanceTo( Vec3d vec, Vec3d collector ) {

            Vec3d S4 = S1.add( collector );

            double d0 = vec.x - S4.x;
            double d1 = vec.y - S4.y;
            double d2 = vec.z - S4.z;

            return (double) MathHelper.sqrt( d0 * d0 + d1 * d1 + d2 * d2 );

        }

    }

    public boolean add( Rectangle rectangle ) {
        return recs.add( rectangle );
    }

    public boolean add( int tank, Vec3d S1, Vec3d S2, Vec3d S3 ) {
        return recs.add( new Rectangle( tank, S1, S2, S3 ) );
    }

    public int getTank( PlayerEntity player, Vec3d hitVector, Vec3d collector ) {

        Vec3d playerVec = player.getEyePosition( 1.0f ).add( player.getLookVec() );

        List<Rectangle> list = recs.stream().filter( r -> MathUtil.intersectsRayWithSquare( playerVec, hitVector, r.S1.add( collector ), r.S2.add( collector ), r.S3.add( collector ) ) ).collect( Collectors.toList() );

        int tank = -1;
        double distance = Double.MAX_VALUE;

        for ( Rectangle rectangle : list ) {

            double distanceTo = rectangle.distanceTo( playerVec, collector );

            if ( distanceTo < distance ) {
                tank = rectangle.tank;
                distance = distanceTo;
            }

        }

        if ( distance <= 2.18f )
            return tank;

        //BiomeDiversity.LOGGER.info( " distance too far " + distance );

        return -1;

    }

}
