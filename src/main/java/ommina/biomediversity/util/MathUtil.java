package ommina.biomediversity.util;

import net.minecraft.util.math.BlockPos;

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

}
