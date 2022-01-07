package src.ex4_java_client;

import Graph.MyDWG;
import api.NodeData;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Agents {
    private ArrayList<Agent> Agents;
    public Agents(){
        this.Agents = new ArrayList<Agent>();
    }


    public void loadjsonstring(String json){
        Gson gson = new Gson();
        // String j ="{\"Pokemons\":[{\"value\":5.0,\"type\":-1,\"pos\":\"35.188900353135324,32.105320110855615,0.0\"},{\"value\":8.0,\"type\":-1,\"pos\":\"35.206679711961414,32.10571613186106,0.0\"},{\"value\":13.0,\"type\":-1,\"pos\":\"35.212669424769075,32.105340746955505,0.0\"},{\"value\":5.0,\"type\":-1,\"pos\":\"35.21120742821597,32.10240519983585,0.0\"},{\"value\":9.0,\"type\":-1,\"pos\":\"35.2107064115802,32.10181728154006,0.0\"}]}";
        Agents agent = gson.fromJson(json, Agents.class);
        this.Agents=agent.Agents;
    }

    public void sort(){
        Agents.sort((a,b)-> (int) (b.getSpeed()-a.getSpeed()));
    }

    public ArrayList<Agent> GetAgentList(){
        return this.Agents;
    }
    public boolean AreMoving(MyDWG g){
        for(int i =0;i<this.Agents.size();i++){
            if(Agents.get(i).isMoving(g)){
                return true;
            }
        }
        return false;
    }
}