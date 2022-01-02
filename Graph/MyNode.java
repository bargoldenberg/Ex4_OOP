package Graph;
import api.EdgeData;
import api.GeoLocation;
import api.NodeData;

import java.util.ArrayList;

public class MyNode implements NodeData {
    private Point3D Location;
    private int Key;
    private double Weight;
    private String Info;
    private int Tag;
    // Trying different approach, instead of HashMaps I will use ArrayList with the src/dest values only.
    // I'm using new name - so I would see where the old HashMap was used.
    private ArrayList<Integer> edgesIn; // This array will contain the edge src values - since the dest is this node.
    private ArrayList<Integer> edgesOut;// This array will contain the edge dest values - since the src is this node.

    public MyNode(){
        this.Location= null;
        this.Key = 0;
        this.Weight=0;
        this.Info="";
        this.Tag = 0;
        this.edgesIn = new ArrayList<Integer>();
        this.edgesOut = new ArrayList<Integer>();
    }

    public MyNode(Point3D Location, int Key){
        this.Location= Location;
        this.Key = Key;
        this.Weight=0;
        this.Info="";
        this.Tag = 0;
        this.edgesIn = new ArrayList<Integer>();
        this.edgesOut = new ArrayList<Integer>();
    }

    public MyNode(NodeData n){
        this.Location= (Point3D) n.getLocation();
        this.Key = n.getKey();
        this.Weight= n.getWeight();
        this.Info= n.getInfo();
        this.Tag = n.getTag();
        this.edgesIn = new ArrayList<Integer>();
        this.edgesOut = new ArrayList<Integer>();
    }

    @Override
    public int getKey() {
        return this.Key;
    }
    public void setKey(int key){
        this.Key = key;
    }
    @Override
    public GeoLocation getLocation() {
        return this.Location;
    }

    @Override
    public void setLocation(GeoLocation p) {
        this.Location = (Point3D)p;
    }

    @Override
    public double getWeight() {
        return this.Weight;
    }

    @Override
    public void setWeight(double w) {
        this.Weight=w;
    }

    @Override
    public String getInfo() {
        return this.Info;
    }

    @Override
    public void setInfo(String s) {
        this.Info=s;
    }

    @Override
    public int getTag() {
        return this.Tag;
    }

    @Override
    public void setTag(int t) {
        this.Tag=t;
    }

    public ArrayList<Integer> getEdgeInList() {
        return this.edgesIn;
    }

    public void setEdgeInList(ArrayList<Integer> ot) {
        this.edgesIn.addAll(ot);
    }

    public void setEdgeOutList(ArrayList<Integer> ot) {
        this.edgesOut.addAll(ot);
    }

    public ArrayList<Integer> getEdgeOutList() {
        return this.edgesOut;
    }

    public boolean addEdgelist(EdgeData edge) {
        if(edge.getSrc() == this.Key){
            if(this.edgesOut == null){
                this.edgesOut = new ArrayList<Integer>();
            }
            ArrayList<Integer> key = new ArrayList<Integer>();
            key.add(edge.getSrc());
            key.add(edge.getDest());
            this.edgesOut.add(edge.getDest());
            return true;
        }
        else if(edge.getDest() == this.Key){
            if(this.edgesIn == null){
                this.edgesIn = new ArrayList<Integer>();
            }
            ArrayList<Integer> key = new ArrayList<Integer>();
            key.add(edge.getDest());
            key.add(edge.getDest());
            this.edgesIn.add(edge.getSrc());
            return true;
        }
        else{
            return false;
        }
    }

    public boolean removeEdgelist(int src, int dest) {
        if(src == this.Key){
            if (this.edgesOut.contains(dest)){
                this.edgesOut.remove(this.edgesOut.indexOf(dest));
            }
            return true;
        }
        else if(dest == this.Key){
            if(this.edgesIn.contains(src)){
                this.edgesIn.remove(this.edgesIn.indexOf(src));
            }
            return true;
        }
        else{
            return false;
        }
    }

    public String toString(){
        return "["+"Location: "+this.Location+", "+
                "Weight: "+this.Weight+", "+"Key: "+
                this.Key+", "+"Info: "+this.Info+", "
                +"Tag: "+this.Tag+"]";
    }
}
