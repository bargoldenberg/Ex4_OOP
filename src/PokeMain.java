package src; /**
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


public class PokeMain {
    private static final double EPS = 0.0000001;
    private static final int FPS = 10;
    private static int FLAG=0;

    /**
     * returns the number of agents in this case.
     * @param str
     * @return
     */
    public static int getNumOfAgents(String str){
        JsonObject jobj = new Gson().fromJson(str, JsonObject.class);
        JsonObject server = jobj.get("GameServer").getAsJsonObject();
        int res = server.get("agents").getAsInt();
        return res;
    }

    /**
     * returns the current score.
     * @param str
     * @return
     */
    public static int getscore(String str){
        JsonObject jobj = new Gson().fromJson(str, JsonObject.class);
        JsonObject server = jobj.get("GameServer").getAsJsonObject();
        int res = server.get("grade").getAsInt();
        return res;
    }

    /**
     * main function responsible for creating a gui object and running it, it consists of a "gameloop"
     * running at 10 FPS which calls algorithms from the MyDWG_Algo class, these algorithms give the next location for
     * the agents at every given moment.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        /**
         * connect to server
         */
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
        /**
         * position agents
         */
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
        Agents a = new Agents();
        String agentsStr = client.getAgents();
        a.loadjsonstring(agentsStr);
        String pokemonsStr = client.getPokemons();
        Pokemons p = new Pokemons();
        p.loadjsonstring(pokemonsStr);
        String isRunningStr = client.isRunning();
        GUI gui =new GUI((MyDWG)g.getGraph(),p,a);
        client.start();
        //client.login("209894286");
        HashMap<Integer,ArrayList<NodeData>> allPaths = new HashMap<>();
        long targetTime =1000/FPS;
        int FLAG2=1;
        /**
         * Game Loop
         */
        while (client.isRunning().equals("true")) {
            g.loadjsonstring(client.getGraph());
            ArrayList<ArrayList<Integer>> allNextPaths = g.nextPos(p, a);
            FLAG=0;
            while (FLAG==0) {

                if(FLAG==1){
                    break;
                }
                int next=-1;
                ArrayList<NodeData> path = new ArrayList<NodeData>();
                for (int k = 0; k < allNextPaths.size(); k++) {
                    ArrayList<Integer> al = allNextPaths.get(k);
                    if(al != null&&al.size()!=0) {
                        if(!allPaths.containsKey(al.get(0))) {
                            if(a.GetAgentList().get(al.get(0)).getSrc()==al.get(2)){
                                FLAG=1;
                                break;
                            }
                            path = (ArrayList<NodeData>) g.shortestPath(((MyDWG) g.getGraph()).FindNodeThroughPos((Point3D) g.getGraph().getNode(a.GetAgentList().get(al.get(0)).getSrc()).getLocation()), al.get(1));
                            allPaths.put(al.get(0),path);

                        }else {
                            if(allPaths.get(al.get(0)).isEmpty()){
                                FLAG=1;
                                break;
                            }
                            if (a.GetAgentList().get(al.get(0)).getSrc() != al.get(2) || allPaths.get(al.get(0)).size() > 1) {
                                path = (ArrayList<NodeData>) g.shortestPath(((MyDWG) g.getGraph()).FindNodeThroughPos((Point3D) g.getGraph().getNode(a.GetAgentList().get(al.get(0)).getSrc()).getLocation()), al.get(1));
                            }else{
                                FLAG=1;
                                break;
                            }
                            path.add(path.size(), g.getGraph().getNode(al.get(2)));
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
                            client.move();
                            g.loadjsonstring(client.getGraph());
                            a.loadjsonstring(client.getAgents());
                            p.loadjsonstring(client.getPokemons());
                            gui.updateScreen(p, a,getscore(client.getInfo()),Double.parseDouble(client.timeToEnd()));
                            if(gui.getFLAG()==1){
                                client.stop();
                                gui.setVisible(false);
                                gui.dispose();
                            }
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



