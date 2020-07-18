package ommina.biomediversity.rendering.mollertrumbore;

import net.minecraft.util.math.Vec3d;

public class Vec3f {

    public double x;
    public double y;
    public double z;

    public Vec3f( double x, double y, double z ) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3f( Vec3d vector3d ) {

        this.x = vector3d.x;
        this.y = vector3d.y;
        this.z = vector3d.z;

    }

    //region Overrides
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }
//endregion Overrides

    public Vec3f add( Vec3f v ) {
        return new Vec3f( this.x + v.x, this.y + v.y, this.z + v.z );
    }

    public Vec3f sub( Vec3f v ) {
        return new Vec3f( this.x - v.x, this.y - v.y, this.z - v.z );
    }

    public double dot( Vec3f v ) {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    public Vec3f cross( Vec3f v ) {
        return new Vec3f( this.y * v.z - this.z * v.y, this.z * v.x - this.x * v.z, this.x * v.y - this.y * v.x );
    }

    public float length() {
        return (float) Math.sqrt( x * x + y * y + z * z );
    }

    public Vec3f normalize() {

        float len = length();

        return new Vec3f( x / len, y / len, z / len );

    }

}
