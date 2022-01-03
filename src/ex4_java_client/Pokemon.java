package src.ex4_java_client;

import Graph.Point3D;
import com.google.gson.Gson;

public class Pokemon {
    private double value;
    private int type;
    private String pos;
    private double speed;

    public Pokemon(){
        this.value=0;
        this.type=0;
        this.pos="";
        this.speed=0;
    }

    public Pokemon(double val, int type, String pos, double speed){
        this.value=val;
        this.type=type;
        this.pos=pos;
        this.speed=speed;
    }
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Point3D getPos() {
        String arr[] = this.pos.split(",");
        Point3D position = new Point3D(Double.parseDouble(arr[0]),Double.parseDouble(arr[1]),Double.parseDouble(arr[2]));
        return position;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }



}
