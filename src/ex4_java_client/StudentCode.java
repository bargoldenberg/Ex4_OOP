package src.ex4_java_client; /**
 * @author AchiyaZigi
 * A trivial example for starting the server and running all needed commands
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import Graph.MyDWG;
import Graph.MyDWG_Algo;
import GraphGUI.GUI;
import api.EdgeData;
import api.NodeData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class StudentCode {
    private static final double EPS = 0.0000001;
    private static final int FPS = 10;
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
                    if((g.getNode(e.getSrc()).getLocation().y()>g.getNode(e.getDest()).getLocation().y())&&p.getType()==1){
                        return g.getEdge(e.getDest(),e.getSrc());
                    }else if((g.getNode(e.getSrc()).getLocation().y()<g.getNode(e.getDest()).getLocation().y())&&p.getType()==-1){
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

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        try {
            client.startConnection("127.0.0.1", 6666);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long startTime;
        long URDTimeMillis;
        long waitTime;
        String graphStr = client.getGraph();
        MyDWG_Algo g = new MyDWG_Algo();
        g.loadjsonstring(graphStr);
        int numofagents = getNumOfAgents(client.getInfo());
        System.out.println(graphStr);
        try {
            int center = g.center().getKey();
            for(int i =0;i<numofagents;i++) {
                client.addAgent("{\"id\":"+center+"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        GUI gui =new GUI((MyDWG)g.getGraph(),p,a);
        client.start();
        long targetTime =1000/FPS;
        while (client.isRunning().equals("true")) {
            g.loadjsonstring(client.getGraph());
            System.out.println(client.getAgents());
            System.out.println(client.timeToEnd());
            ArrayList<Integer> al = g.nextPos(p,a);
            ArrayList<NodeData> path =(ArrayList<NodeData>)g.shortestPath(((MyDWG) g.getGraph()).FindNodeThroughPos(a.GetAgentList().get(0).getPos()),al.get(1));
            path.add(path.size(),g.getGraph().getNode(al.get(2)));
            path.remove(0);
            a.GetAgentList().get(al.get(0)).path=path;
            int next = path.get(0).getKey();
            a.GetAgentList().get(al.get(0)).setDest(next);
            for(int i=0;i<a.GetAgentList().size();i++) {
                int id = al.get(0);
                if(a.GetAgentList().get(i).path==null){
                    continue;
                }
                if (!a.GetAgentList().get(i).path.isEmpty()) {
                    next = path.remove(0).getKey();
                    while (a.AreMoving((MyDWG) g.getGraph())) {
//                        if(a.GetAgentList().get(i).path==null){
//                            al=g.nextPos(p,a);
//                            continue;
//                        }
                        startTime = System.nanoTime();
                        client.chooseNextEdge("{\"agent_id\":" + id + ", \"next_node_id\": " + next + "}");
                        client.move();
                        a.loadjsonstring(client.getAgents());
                        p.loadjsonstring(client.getPokemons());
                        gui.updateScreen(p, a);
                        URDTimeMillis = (System.nanoTime() - startTime) / 1000000;
                        waitTime = targetTime - URDTimeMillis;
                        try {
                            Thread.sleep(waitTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                }
            }
        }
     }



