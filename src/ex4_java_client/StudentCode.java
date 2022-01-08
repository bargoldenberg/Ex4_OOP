package src.ex4_java_client; /**
 * @author AchiyaZigi
 * A trivial example for starting the server and running all needed commands
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import Graph.MyDWG;
import Graph.MyDWG_Algo;
import Graph.Point3D;
import GraphGUI.GUI;
import api.EdgeData;
import api.NodeData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class StudentCode {
    private static final double EPS = 0.0000001;
    private static final int FPS = 10;
    private static int FLAG=0;
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
                if(i==0) {
                    client.addAgent("{\"id\":" + center + "}");

                }else{
                    client.addAgent("{\"id\":" + i + "}");
                }
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
        HashMap<Integer,ArrayList<NodeData>> allPaths = new HashMap<>();
        long targetTime =1000/FPS;
        int FLAG2=1;
        while (client.isRunning().equals("true")) {
            g.loadjsonstring(client.getGraph());
            ArrayList<ArrayList<Integer>> allNextPaths = g.nextPos(p, a);
           // p.loadjsonstring(client.getPokemons());
            System.out.println("all paths = "+ allNextPaths);
            FLAG=0;
            while (FLAG==0) {

                if(FLAG==1){
                    break;
                }
                int next=-1;
                ArrayList<NodeData> path = new ArrayList<NodeData>();
                //System.out.println(allPaths);
                for (int k = 0; k < allNextPaths.size(); k++) {
                    ArrayList<Integer> al = allNextPaths.get(k);
                    System.out.println("path for " + k + ": " + al);
                    if(al != null&&al.size()!=0) {
                        System.out.println(al.toString());
                        if(!allPaths.containsKey(al.get(0))) {
                            if(a.GetAgentList().get(al.get(0)).getSrc()==al.get(2)){
                                FLAG=1;
                                break;
                            }
                            path = (ArrayList<NodeData>) g.shortestPath(((MyDWG) g.getGraph()).FindNodeThroughPos((Point3D) g.getGraph().getNode(a.GetAgentList().get(al.get(0)).getSrc()).getLocation()), al.get(1));
                            allPaths.put(al.get(0),path);
                            System.out.println("hi");

                        }else {
                            if(allPaths.get(al.get(0)).isEmpty()){
                                FLAG=1;
                                break;
                            }
                            if (a.GetAgentList().get(al.get(0)).getSrc() != al.get(2) || allPaths.get(al.get(0)).size() > 1) {
                                path = (ArrayList<NodeData>) g.shortestPath(((MyDWG) g.getGraph()).FindNodeThroughPos((Point3D) g.getGraph().getNode(a.GetAgentList().get(al.get(0)).getSrc()).getLocation()), al.get(1));
                            }else{
                                FLAG=1;
                                System.out.println(allPaths);
                                break;
                            }
                            path.add(path.size(), g.getGraph().getNode(al.get(2)));
                            System.out.println(path);
                            allPaths.put(al.get(0),path);
                        }

                        int id = al.get(0);
                        if (allPaths.get(al.get(0)).size()>1) {
                            if (a.GetAgentList().get(al.get(0)).getSrc() == ((MyDWG) g.getGraph()).FindNodeThroughPos(a.GetAgentList().get(al.get(0)).getPos())) {
                                allPaths.get(id).remove(0).getKey();
                                next = allPaths.get(id).get(0).getKey();
                            } else {
                                next = allPaths.get(id).remove(0).getKey();
                            }
                            startTime = System.nanoTime();
                            client.chooseNextEdge("{\"agent_id\":" + id + ", \"next_node_id\": " + next + "}");
                            System.out.println("{\"agent_id\":" + id + ", \"next_node_id\": " + next + "}");
                            System.out.println("agent src ="+a.GetAgentList().get(al.get(0)).getSrc());
                            client.move();
                            g.loadjsonstring(client.getGraph());
                            a.loadjsonstring(client.getAgents());
                            p.loadjsonstring(client.getPokemons());
                            //allNextPaths = g.nextPos(p,a);
                            gui.updateScreen(p, a);
                            URDTimeMillis = (System.nanoTime() - startTime) / 1000000;
                            waitTime = Math.abs(targetTime - URDTimeMillis);
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
}



