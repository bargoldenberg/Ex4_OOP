package src.ex4_java_client;

import Graph.MyDWG;
import Graph.Point3D;

public class Agent {
    private AgentClass Agent;

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

    public boolean isMoving(MyDWG graph){
        if(graph.FindNodeThroughPos(getPos()) == -1){
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
