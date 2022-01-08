package Tests;

import Graph.MyDWG;
import Graph.MyEdge;
import Graph.MyNode;
import Graph.Point3D;
import api.NodeData;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class MyDWGTest {

    @Test
    void getNode() {
        Point3D p1 = new Point3D(1,2,0);
        Point3D p2 = new Point3D(2,1,0);
        MyDWG g= new MyDWG();
        MyNode n1 = new MyNode(p1,0);
        MyNode n2 = new MyNode(p2,1);
        MyEdge e1 = new MyEdge(0,2,1);
        g.addNode(n1);
        g.addNode(n2);
        g.connect(n1.getKey(),n2.getKey(),2);
        assertEquals(g.getNode(0),n1);
    }

    @Test
    void getEdge() {
        Point3D p1 = new Point3D(1,2,0);
        Point3D p2 = new Point3D(2,1,0);
        MyDWG g= new MyDWG();
        MyNode n1 = new MyNode(p1,0);
        MyNode n2 = new MyNode(p2,1);
        MyEdge e1 = new MyEdge(0,2,1);
        g.addNode(n1);
        g.addNode(n2);
        g.connect(n1.getKey(),n2.getKey(),2);
        assertEquals(g.getEdge(n1.getKey(),n2.getKey()).getSrc(), e1.getSrc());
        assertEquals(g.getEdge(n1.getKey(),n2.getKey()).getDest(), e1.getDest());

    }

    @Test
    void addNode() {
        Point3D p1 = new Point3D(1,2,0);
        Point3D p2 = new Point3D(2,1,0);
        MyDWG g= new MyDWG();
        MyNode n1 = new MyNode(p1,0);
        MyNode n2 = new MyNode(p2,1);
        MyEdge e1 = new MyEdge(0,2,1);
        g.addNode(n1);
        g.addNode(n2);
        assertTrue(g.getNode(n1.getKey())!=null);
    }

    @Test
    void connect() {
        Point3D p1 = new Point3D(1,2,0);
        Point3D p2 = new Point3D(2,1,0);
        MyDWG g= new MyDWG();
        MyNode n1 = new MyNode(p1,0);
        MyNode n2 = new MyNode(p2,1);
        MyEdge e1 = new MyEdge(0,2,1);
        g.addNode(n1);
        g.addNode(n2);
        g.connect(n1.getKey(),n2.getKey(),2);
        assertEquals(g.getEdge(e1.getSrc(),e1.getDest()).getSrc(),e1.getSrc());
        assertEquals(g.getEdge(e1.getSrc(),e1.getDest()).getDest(),e1.getDest());
        assertEquals(g.getEdge(e1.getSrc(),e1.getDest()).getWeight(),e1.getWeight());
    }

    @Test
    void nodeIter() {
        Point3D p1 = new Point3D(1,2,0);
        Point3D p2 = new Point3D(2,1,0);
        Point3D p3 = new Point3D(5,6,10);
        MyDWG g= new MyDWG();
        MyNode n1 = new MyNode(p1,0);
        MyNode n2 = new MyNode(p2,1);
        MyNode n3 = new MyNode(p3,2);
        MyEdge e1 = new MyEdge(0,2,1);
        g.addNode(n1);
        g.addNode(n2);
        g.connect(n1.getKey(),n2.getKey(),2);
        Iterator<NodeData> it = g.nodeIter();
        it.next();
        //it.remove();
        it.next();
        g.addNode(n3);
        Iterator<NodeData> it2 = g.nodeIter();
        while (it2.hasNext()) {
            System.out.println(it2.next());
        }

    }

    @Test
    void edgeIter() throws Exception {
        Point3D p1 = new Point3D(1,2,0);
        Point3D p2 = new Point3D(2,1,0);
        MyDWG g= new MyDWG();
        MyNode n1 = new MyNode(p1,0);
        MyNode n2 = new MyNode(p2,1);
        MyEdge e1 = new MyEdge(0,2,1);
        MyEdge e2 = new MyEdge(1,1,0);
        g.addNode(n1);
        g.addNode(n2);
        g.connect(n1.getKey(),n2.getKey(),2);
        g.connect(n2.getKey(),n1.getKey(),1);

        Iterator it = g.edgeIter();
        while(it.hasNext()) {

            System.out.println(it.next());
            it.remove();
        }
        Iterator it2 = g.edgeIter();
        while(it2.hasNext()) {

            System.out.println(it2.next());

        }
//        Iterator it2 = g.edgeIter(n2.getKey());
//        while(it2.hasNext()) {
//            System.out.println(it2.next());
//        }
//        Iterator it3 = g.nodeIter();
//        while(it3.hasNext()) {
//            System.out.println(it3.next());
//        }
    }


    @Test
    void removeNode() {
        Point3D p1 = new Point3D(1,2,0);
        Point3D p2 = new Point3D(2,1,0);
        MyDWG g= new MyDWG();
        MyNode n1 = new MyNode(p1,0);
        MyNode n2 = new MyNode(p2,1);
        MyEdge e1 = new MyEdge(0,2,1);
        MyEdge e2 = new MyEdge(1,1,0);
        g.addNode(n1);
        g.addNode(n2);
        g.connect(n1.getKey(),n2.getKey(),2);
        g.connect(n2.getKey(),n1.getKey(),1);
        g.removeNode(n1.getKey());
        assertTrue(g.getNode(n1.getKey())==null);
        assertTrue(g.getEdge(n1.getKey(),n2.getKey())==null);

    }

    @Test
    void removeEdge() {
    }

    @Test
    void nodeSize() {
    }

    @Test
    void edgeSize() {
        Point3D p1 = new Point3D(1,2,0);
        Point3D p2 = new Point3D(2,1,0);
        MyDWG g= new MyDWG();
        MyNode n1 = new MyNode(p1,0);
        MyNode n2 = new MyNode(p2,1);
        MyEdge e1 = new MyEdge(0,2,1);
        g.addNode(n1);
        g.addNode(n2);
        g.connect(n1.getKey(),n2.getKey(),2);
        assertEquals(g.edgeSize(),1);
    }

    @Test
    void getMC() {
        Point3D p1 = new Point3D(1,2,0);
        Point3D p2 = new Point3D(2,1,0);
        MyDWG g= new MyDWG();
        int start = g.getMC();
        MyNode n1 = new MyNode(p1,0);
        MyNode n2 = new MyNode(p2,1);
        MyEdge e1 = new MyEdge(0,2,1);
        g.addNode(n1);
        g.addNode(n2);
        g.connect(n1.getKey(),n2.getKey(),2);
        int end = g.getMC();
        assertNotEquals(start,end);

    }

    @Test
    void testToString() {
    }
}