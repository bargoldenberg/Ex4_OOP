package src.ex4_java_client;

import Graph.Point3D;

public class AgentClass {
    private int id;
    private double value;
    private int src;
    private int dest;
    private double speed;
    private String pos;
    private boolean state;

    public AgentClass(){
        this.id=0;
        this.value=0;
        this.src=0;
        this.dest=0;
        this.speed=0;
        this.pos = "";
        this.state = false;
    }

    public AgentClass(int id, double val, int src, int dest, double speed, String pos){
        this.id=id;
        this.value=val;
        this.src=src;
        this.dest=dest;
        this.speed=speed;
        this.pos=pos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public int getDest() {
        return dest;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isState(){
        return this.state;
    }
    public void setState(boolean state){
        this.state = state;
    }

    public Point3D getPos() {
        String arr[] = this.pos.split(",");
        Point3D position = new Point3D(Double.parseDouble(arr[0]),Double.parseDouble(arr[1]),Double.parseDouble(arr[2]));
        return position;
    }

}
