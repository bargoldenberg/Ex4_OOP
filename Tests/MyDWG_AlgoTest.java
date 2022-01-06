package Tests;

import static org.junit.jupiter.api.Assertions.*;
import Graph.MyEdge;
import Graph.Point3D;
import Graph.MyNode;
import Graph.MyDWG;
import Graph.MyDWG_Algo;
import Graph.*;
import api.EdgeData;
import api.GeoLocation;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import org.junit.Assert;
import src.ex4_java_client.Pokemon;
import src.ex4_java_client.PokemonClass;
import src.ex4_java_client.Pokemons;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;


class MyDWG_AlgoTest {

    @org.junit.jupiter.api.Test
    void findEdge() throws FileNotFoundException {

        /**
         * Setting up the test graph.
         */
        MyNode n1 = new MyNode(new Point3D(1.0,1.0,0.0),1);
        MyNode n2 = new MyNode(new Point3D(1.0,5.0,0.0),2);
        MyNode n3 = new MyNode(new Point3D(6.0,3.0,0.0),3);
        MyNode n4 = new MyNode(new Point3D(11.0,5.0,0.0),4);
        MyNode n5 = new MyNode(new Point3D(11.0,1.0,0.0),5);
        MyDWG graph = new MyDWG();
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        graph.addNode(n4);
        graph.addNode(n5);
        graph.connect(1,2,4);
        graph.connect(2,1,4);
        graph.connect(4,5,4);
        graph.connect(5,4,4);
        graph.connect(2,4,10);
        graph.connect(4,2,10);
        graph.connect(1,5,10);
        graph.connect(5,1,10);
        graph.connect(2,3,Math.sqrt(116)/2);
        graph.connect(3,5,Math.sqrt(116)/2);
        graph.connect(3,2,Math.sqrt(116)/2);
        graph.connect(5,3,Math.sqrt(116)/2);
        graph.connect(1,3,Math.sqrt(116)/2);
        graph.connect(3,1,Math.sqrt(116)/2);
        graph.connect(3,4,Math.sqrt(116)/2);
        graph.connect(4,3,Math.sqrt(116)/2);
        MyDWG_Algo g = new MyDWG_Algo();
        g.init(graph);
        /**
         * Test for findEdge --> see if it finds the Pokemon position.
         */
        Pokemons pok = new Pokemons();
        String pokemonStr = "{\"Pokemons\":[{\"Pokemon\":{\"value\":5.0,\"type\":-1,\"pos\":\"4.0,1.0,0.0\"}},{\"Pokemon\":{\"value\":8.0,\"type\":1,\"pos\":\"11.0,3,0,0.0\"}}]}";
        pok.loadjsonstring(pokemonStr);
        MyEdge e1 = (MyEdge) g.findEdge(pok.GetPokeList().get(0));
        MyEdge e2 = (MyEdge) g.findEdge(pok.GetPokeList().get(1));
        System.out.println(e1);
        System.out.println(e2);
        assertEquals(e1.getSrc(),1);
        assertEquals(e2.getSrc(),4);
    }

    @org.junit.jupiter.api.Test
    void nextPos() {
    }
}