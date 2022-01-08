package Tests;

import Graph.Point3D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Point3DTest {

    @Test
    void x() {
        Point3D p1 = new Point3D(1,2,3);
        assertEquals(p1.x(),1);
    }

    @Test
    void y() {
        Point3D p1 = new Point3D(1,2,3);
        assertEquals(p1.y(),2);
    }

    @Test
    void z() {
        Point3D p1 = new Point3D(1,2,3);
        assertEquals(p1.z(),3);
    }

    @Test
    void distance() {
    }

    @Test
    void testToString() {
        Point3D p1 = new Point3D(1,2,3);
        String s = "(x: 1.0, y: 2.0, z: 3.0)";
        assertEquals(s,p1.toString());
    }
}