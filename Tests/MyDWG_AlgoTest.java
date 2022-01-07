package Tests;

import static org.junit.jupiter.api.Assertions.*;
import Graph.MyEdge;
import Graph.Point3D;
import Graph.MyNode;
import Graph.MyDWG;
import Graph.MyDWG_Algo;
import Graph.*;
import GraphGUI.GUI;
import api.EdgeData;
import api.GeoLocation;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import src.ex4_java_client.Agents;
import src.ex4_java_client.Pokemon;
import src.ex4_java_client.PokemonClass;
import src.ex4_java_client.Pokemons;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;


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
    void tagPokemonsOnEdges(){
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
        g.tagPokemonsOnEdges(pok);
        assertEquals(g.findEdge(pok.GetPokeList().get(0)).getTag(),1);
        assertEquals(g.findEdge(pok.GetPokeList().get(1)).getTag(),1);

    }

    @org.junit.jupiter.api.Test
    void nextPos() {
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
        Agents ag = new Agents();
        String pokemonStr = "{\"Pokemons\":[{\"Pokemon\":{\"value\":5.0,\"type\":1,\"pos\":\"4.0,1.0,0.0\"}},{\"Pokemon\":{\"value\":8.0,\"type\":-1,\"pos\":\"11.0,3,0,0.0\"}}]}";
        String agentStr = "{\"Agents\": [{\"Agent\": {\"id\": 0, \"value\": 0.0, \"src\": 0, \"dest\": 1, \"speed\": 1.0, \"pos\": \"11.0,1.0,0.0\"}},{\"Agent\": {\"id\": 1, \"value\": 0.0, \"src\": 0, \"dest\": 1, \"speed\": 3.0, \"pos\": \"1.0,5.0,0.0\"}}]}";
        pok.loadjsonstring(pokemonStr);
        ag.loadjsonstring(agentStr);
//        ArrayList<Integer> arr =  g.nextPos(pok,ag);
//        System.out.println(arr);
//        GUI gui= new GUI(graph,pok,ag);

//        assertEquals(g.findEdge(pok.GetPokeList().get(0)).getTag(),1);
//        assertEquals(g.findEdge(pok.GetPokeList().get(1)).getTag(),1);

    }

    @org.junit.jupiter.api.Test
    void nextPos2() {
        /**
         * Setting up the test graph.
         */
        MyNode n1 = new MyNode(new Point3D(1.0,1.0,0.0),1);
        MyNode n2 = new MyNode(new Point3D(1.0,5.0,0.0),2);
        MyNode n3 = new MyNode(new Point3D(6.0,3.0,0.0),3);
        MyNode n4 = new MyNode(new Point3D(11.0,5.0,0.0),4);
        MyNode n5 = new MyNode(new Point3D(11.0,1.0,0.0),5);
        MyNode n6 = new MyNode(new Point3D(1.0,10.0,0.0),6);
        MyNode n7 = new MyNode(new Point3D(6.0,10.0,0.0),7);
        MyNode n8 = new MyNode(new Point3D(9.0,8.0,0.0),8);
        MyNode n9 = new MyNode(new Point3D(11.0,10.0,0.0),9);

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
        graph.connect(5,6,5);
        graph.connect(6,5,5);
        graph.connect(6,7,5);
        graph.connect(7,6,5);
        graph.connect(6,4,Math.sqrt(125));
        graph.connect(4,6,Math.sqrt(125));
        graph.connect(7,9,5);
        graph.connect(9,7,5);
        graph.connect(9,8,Math.sqrt(8));
        graph.connect(8,9,Math.sqrt(8));
        graph.connect(8,4,Math.sqrt(13));
        graph.connect(4,8,Math.sqrt(13));
        MyDWG_Algo g = new MyDWG_Algo();
        g.init(graph);
        /**
         * Test for findEdge --> see if it finds the Pokemon position.
         */
        Pokemons pok = new Pokemons();
        Agents ag = new Agents();
        String pokemonStr = "{\"Pokemons\":[{\"Pokemon\":{\"value\":5.0,\"type\":1,\"pos\":\"4.0,1.0,0.0\"}},{\"Pokemon\":{\"value\":5.0,\"type\":1,\"pos\":\"1.0,9.0,0.0\"}},{\"Pokemon\":{\"value\":8.0,\"type\":-1,\"pos\":\"11.0,3,0,0.0\"}}]}";
        String agentStr = "{\"Agents\": [{\"Agent\": {\"id\": 0, \"value\": 0.0, \"src\": 0, \"dest\": 1, \"speed\": 1.0, \"pos\": \"11.0,1.0,0.0\"}},{\"Agent\": {\"id\": 1, \"value\": 0.0, \"src\": 0, \"dest\": 1, \"speed\": 3.0, \"pos\": \"1.0,5.0,0.0\"}}]}";
        pok.loadjsonstring(pokemonStr);
        ag.loadjsonstring(agentStr);
//        ArrayList<Integer> arr =  g.nextPos(pok,ag);
//        System.out.println(arr);
//        GUI gui= new GUI(graph,pok,ag);

//        assertEquals(g.findEdge(pok.GetPokeList().get(0)).getTag(),1);
//        assertEquals(g.findEdge(pok.GetPokeList().get(1)).getTag(),1);

    }

}