package src.ex4_java_client;

import Graph.MyDWG;
import Graph.Point3D;
import api.NodeData;

import java.util.ArrayList;

public class Agent {
    private AgentClass Agent;
    public ArrayList<NodeData> path;
    public Agent() {
        this.Agent = null;
    }

    public double getSpeed(){
        return this.Agent.getSpeed();
    }

    public boolean getState(){
        return this.Agent.isState();
    }
    public void setState(boolean state){
        this.Agent.setState(state);
    }
    public int getSrc() {
        return Agent.getSrc();
    }
    public void setDest(int d){ Agent.setDest(d);}
    public int getDest(){
        return Agent.getDest();
    }
    public boolean isMoving(MyDWG graph){
        if(Agent.getDest()==-1){
            return true;
        }
        if(graph.FindNodeThroughPos((Point3D)graph.getNode(Agent.getDest()).getLocation()) != graph.FindNodeThroughPos(Agent.getPos())){
            return true;
        }
        else{
            return false;
        }
    }


    public Point3D getPos(){
        return Agent.getPos();
    }
}
