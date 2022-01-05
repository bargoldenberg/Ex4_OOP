package src.ex4_java_client; /**
 * @author AchiyaZigi
 * A trivial example for starting the server and running all needed commands
 */
import java.io.IOException;
import java.util.Scanner;
import Graph.MyDWG;
import Graph.MyDWG_Algo;
import Graph.fromJsonToGraph;
import GraphGUI.GUI;
import api.DirectedWeightedGraph;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class StudentCode {
    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.startConnection("127.0.0.1", 6666);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String graphStr = client.getGraph();
        MyDWG_Algo g = new MyDWG_Algo();
        g.loadjsonstring(graphStr);
        System.out.println(graphStr);
        client.addAgent("{\"id\":0}");
        Agents a = new Agents();
        String agentsStr = client.getAgents();
        a.loadjsonstring(agentsStr);
        System.out.println(agentsStr);
        String pokemonsStr = client.getPokemons();
        Pokemons p = new Pokemons();
        p.loadjsonstring(pokemonsStr);
        System.out.println(pokemonsStr);
        String isRunningStr = client.isRunning();
        System.out.println(isRunningStr);

        client.start();
        while (client.isRunning().equals("true")) {
            client.move();
            a.loadjsonstring(client.getAgents());
            GUI gui =new GUI((MyDWG)g.getGraph(),p,a);
            System.out.println(client.getAgents());
            System.out.println(client.timeToEnd());
            Scanner keyboard = new Scanner(System.in);
            System.out.println("enter the next dest: ");
            int next = keyboard.nextInt();
            client.chooseNextEdge("{\"agent_id\":0, \"next_node_id\": " + next + "}");

        }
    }

}
