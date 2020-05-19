package ommina.biomediversity.rendering;

import javafx.geometry.Point3D;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class Icosphere {

    /*
        Adapted from: http://blog.andreaskahler.com/2009/06/creating-icosphere-mesh-in-code.html
     */

    private static final List<List<Triangle>> spheres = new ArrayList<List<Triangle>>();

    public static final int MAX_RECURSION = 3;

    List<Point3D> geometry = new ArrayList<>();
    private int index;

    private Icosphere() {
    }

    public static List<Triangle> getSphere( int recursion ) {

        if ( recursion < 0 || recursion > MAX_RECURSION )
            recursion = 0;

        if ( spheres.size() == 0 )
            createSpheres();

        return spheres.get( recursion );

    }

    private static void createSpheres() {

        for ( int n = 0; n <= MAX_RECURSION; n++ ) {

            Icosphere instance = new Icosphere();

            spheres.add( instance.createSphere( n ) );

        }

    }

    private List<Triangle> createSphere( int recursionLevel ) {

        List<TriangleIndices> faces = generate( recursionLevel );

        List<Triangle> triangles = new ArrayList<>();

        for ( TriangleIndices triangleIndices : faces )
            triangles.add( new Triangle( geometry.get( triangleIndices.v1 ), geometry.get( triangleIndices.v2 ), geometry.get( triangleIndices.v3 ) ) );

        Collections.sort( triangles );

        return triangles;

    }

    // add vertex to mesh, fix position to be on unit sphere, return index
    private int addVertex( Point3D p ) {

        double length = Math.sqrt( p.getX() * p.getX() + p.getY() * p.getY() + p.getZ() * p.getZ() );

        geometry.add( new Point3D( p.getX() / length, p.getY() / length, p.getZ() / length ) );

        return index++;

    }

    public int getTotalFaces( int sphere ) {
        return spheres.get( sphere ).size();
    }

    public Triangle getTriangle( int sphere, int face ) {
        return spheres.get( sphere ).get( face );
    }

    public List<TriangleIndices> generate( int recursionLevel ) {

        List<TriangleIndices> faces = new Vector<>();

        double t = (float) ((1 + Math.sqrt( 5.0 )) * 0.5);

        addVertex( new Point3D( -1, t, 0 ) );
        addVertex( new Point3D( 1, t, 0 ) );
        addVertex( new Point3D( -1, -t, 0 ) );
        addVertex( new Point3D( 1, -t, 0 ) );

        addVertex( new Point3D( 0, -1, t ) );
        addVertex( new Point3D( 0, 1, t ) );
        addVertex( new Point3D( 0, -1, -t ) );
        addVertex( new Point3D( 0, 1, -t ) );

        addVertex( new Point3D( t, 0, -1 ) );
        addVertex( new Point3D( t, 0, 1 ) );
        addVertex( new Point3D( -t, 0, -1 ) );
        addVertex( new Point3D( -t, 0, 1 ) );

        // create 20 triangles of the icosahedron

        // 5 faces around point 0
        faces.add( new TriangleIndices( 0, 11, 5 ) );
        faces.add( new TriangleIndices( 0, 5, 1 ) );
        faces.add( new TriangleIndices( 0, 1, 7 ) );
        faces.add( new TriangleIndices( 0, 7, 10 ) );
        faces.add( new TriangleIndices( 0, 10, 11 ) );

        // 5 adjacent faces
        faces.add( new TriangleIndices( 1, 5, 9 ) );
        faces.add( new TriangleIndices( 5, 11, 4 ) );
        faces.add( new TriangleIndices( 11, 10, 2 ) );
        faces.add( new TriangleIndices( 10, 7, 6 ) );
        faces.add( new TriangleIndices( 7, 1, 8 ) );

        // 5 faces around point 3
        faces.add( new TriangleIndices( 3, 9, 4 ) );
        faces.add( new TriangleIndices( 3, 4, 2 ) );
        faces.add( new TriangleIndices( 3, 2, 6 ) );
        faces.add( new TriangleIndices( 3, 6, 8 ) );
        faces.add( new TriangleIndices( 3, 8, 9 ) );

        // 5 adjacent faces
        faces.add( new TriangleIndices( 4, 9, 5 ) );
        faces.add( new TriangleIndices( 2, 4, 11 ) );
        faces.add( new TriangleIndices( 6, 2, 10 ) );
        faces.add( new TriangleIndices( 8, 6, 7 ) );
        faces.add( new TriangleIndices( 9, 8, 1 ) );

        for ( int i = 0; i < recursionLevel; i++ ) {

            List<TriangleIndices> faces2 = new ArrayList<>();

            for ( TriangleIndices tri : faces ) {

                // replace triangle by 4 triangles
                int a = getMiddlePoint( tri.v1, tri.v2 );
                int b = getMiddlePoint( tri.v2, tri.v3 );
                int c = getMiddlePoint( tri.v3, tri.v1 );

                faces2.add( new TriangleIndices( tri.v1, a, c ) );
                faces2.add( new TriangleIndices( tri.v2, b, a ) );
                faces2.add( new TriangleIndices( tri.v3, c, b ) );
                faces2.add( new TriangleIndices( a, b, c ) );

            }

            faces = faces2;

        }

        return faces;

    }

    // return index of point in the middle of p1 and p2
    private int getMiddlePoint( int p1, int p2 ) {

        Point3D point1 = geometry.get( p1 );
        Point3D point2 = geometry.get( p2 );
        Point3D middle = new Point3D(
             (point1.getX() + point2.getX()) / 2.0,
             (point1.getY() + point2.getY()) / 2.0,
             (point1.getZ() + point2.getZ()) / 2.0 );

        // add vertex makes sure point is on unit sphere
        return addVertex( middle );

    }

}
