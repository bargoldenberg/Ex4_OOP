package src.ex4_java_client;

import Graph.Point3D;

public class PokemonClass {
    private double value;
    private int type;
    private String pos;

    public PokemonClass(){
        this.value=0;
        this.type=0;
        this.pos="";
    }

    public PokemonClass(double val, int type, String pos, double speed){
        this.value=val;
        this.type=type;
        this.pos=pos;
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





}
