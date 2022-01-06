package src.ex4_java_client; /**
 * @author AchiyaZigi
 * A trivial example for starting the server and running all needed commands
 */
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import Graph.MyDWG;
import Graph.MyDWG_Algo;
import Graph.MyEdge;
import Graph.fromJsonToGraph;
import GraphGUI.GUI;
import api.DirectedWeightedGraph;
import api.EdgeData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;


public class StudentCode {
    private static final double EPS = 0.0000001;
    public static int getNumOfAgents(String str){
        JsonObject jobj = new Gson().fromJson(str, JsonObject.class);
        JsonObject server = jobj.get("GameServer").getAsJsonObject();
        int res = server.get("agents").getAsInt();
        return res;
    }
    public static EdgeData findEdge(Pokemon p, MyDWG g){

        try {
            Iterator<EdgeData> it= g.edgeIter();
            while(it.hasNext()){
                EdgeData e = it.next();
                double dist1 = g.getNode(e.getSrc()).getLocation().distance(g.getNode(e.getDest()).getLocation());
                double dist2 =       (g.getNode(e.getSrc()).getLocation().distance(p.getPosition())+
                        p.getPosition().distance(g.getNode(e.getDest()).getLocation()));
                double delta = Math.abs(dist1-dist2);
                boolean onedge = delta<EPS;
                if(onedge){
                    if((g.getNode(e.getSrc()).getLocation().y()<g.getNode(e.getDest()).getLocation().y())&&p.getType()==1){
                        return g.getEdge(e.getDest(),e.getSrc());
                    }else if((g.getNode(e.getSrc()).getLocation().y()>g.getNode(e.getDest()).getLocation().y())&&p.getType()==-1){
                        return g.getEdge(e.getDest(),e.getSrc());
                    }else {
                        return e;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
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
        int numofagents = getNumOfAgents(client.getInfo());
        System.out.println(graphStr);
        for(int i =0;i<numofagents;i++) {
            client.addAgent("{\"id\":"+i+"}");
        }
        System.out.println("the agents are"+client.getAgents());
        Agents a = new Agents();
        String agentsStr = client.getAgents();
        a.loadjsonstring(agentsStr);
        System.out.println(agentsStr);
        String pokemonsStr = client.getPokemons();
        Pokemons p = new Pokemons();
        p.loadjsonstring(pokemonsStr);
        System.out.println(findEdge(p.GetPokeList().get(0),(MyDWG)g.getGraph()));
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
