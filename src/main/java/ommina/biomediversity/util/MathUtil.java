package ommina.biomediversity.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class MathUtil {

    public static int getDistanceBetween( BlockPos pos1, BlockPos pos2 ) {

        return (int) Math.sqrt( Math.pow( pos1.getX() - pos2.getX(), 2 ) + Math.pow( pos1.getY() - pos2.getY(), 2 ) + Math.pow( pos1.getZ() - pos2.getZ(), 2 ) );

    }

    public static int clamp( float value, int min, int max ) {

        return value < min ? min : value > max ? max : (int) value;

    }

    public static float clamp( float value, float min, float max ) {

        return value < min ? min : value > max ? max : value;

    }

    public static float round( float value, int precision ) {

        int scale = (int) Math.pow( 10, precision );

        return (float) Math.round( value * scale ) / scale;

    }

    public static int mid( int val1, int val2 ) {

        return (val1 + val2) >>> 1;

    }

    public static boolean intersectsRayWithSquare( PlayerEntity player, Vec3d hitVector, Vec3d S1, Vec3d S2, Vec3d S3 ) {

        return intersectsRayWithSquare( player.getEyePosition( 1.0f ).add( player.getLookVec() ), hitVector, S1, S2, S3 );

    }

    public static boolean intersectsRayWithSquare( Vec3d playerVector, Vec3d hitVector, Vec3d S1, Vec3d S2, Vec3d S3 ) {

        // https://stackoverflow.com/questions/21114796/3d-ray-quad-intersection-test-in-java  (Thanks, user3146587!)

        // 1.
        Vec3d dS21 = S2.subtract( S1 );
        Vec3d dS31 = S3.subtract( S1 );
        Vec3d n = dS21.crossProduct( dS31 );

        // 2.
        Vec3d dR = playerVector.subtract( hitVector );

        double ndotdR = n.dotProduct( dR );

        if ( Math.abs( ndotdR ) < 1e-6f ) { // Choose your tolerance
            return false;
        }

        double t = -n.dotProduct( playerVector.subtract( S1 ) ) / ndotdR;
        Vec3d M = playerVector.add( dR.scale( t ) );

        // 3.
        Vec3d dMS1 = M.subtract( S1 );
        double u = dMS1.dotProduct( dS21 );
        double v = dMS1.dotProduct( dS31 );

        // 4.
        return (u >= 0.0f && u <= dS21.dotProduct( dS21 )
             && v >= 0.0f && v <= dS31.dotProduct( dS31 ));

    }

}
