package Tests.Graph;

import Graph.nodeConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class nodeConverterTest {
    String pos1 = "35.19589389346247,32.10152879327731,0.0";
    String pos2 ="22.1,15.30303,0.0";
    int id1 = 0;
    int id2 = 1;

    nodeConverter n1 = new nodeConverter(pos1,id1);
    nodeConverter n2 = new nodeConverter(pos2,id2);

    @Test
    void getXTest(){
        assertEquals(35.19589389346247,n1.getX());
        assertEquals(22.1,n2.getX());
    }

    @Test
    void getYTest(){
        assertEquals(32.10152879327731,n1.getY());
        assertEquals(15.30303,n2.getY());
    }
    @Test
    void getZ(){
        assertEquals(0.0,n1.getZ());
        assertEquals(0.0,n2.getZ());
    }

}