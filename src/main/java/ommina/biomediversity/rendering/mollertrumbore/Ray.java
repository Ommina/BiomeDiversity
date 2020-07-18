package ommina.biomediversity.rendering.mollertrumbore;

public class Ray {

    public Vec3f orig;
    public Vec3f dir;

    public Ray( Vec3f origin, Vec3f direction ) {

        this.orig = origin;
        this.dir = direction;

    }

}
