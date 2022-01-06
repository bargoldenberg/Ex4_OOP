package src.ex4_java_client;

import Graph.Point3D;

public class Pokemon {
    private PokemonClass Pokemon;
    private boolean Caught;

    public Pokemon() {
        this.Pokemon = null;
        this.Caught = false;
    }

    public Point3D getPosition() {
        return Pokemon.getPos();
    }
    public int getType() {
        return Pokemon.getType();
    }
    public double getValue() {
        return Pokemon.getValue();
    }

    public boolean isCaught(){
        return this.Caught;
    }

    public void FellAndCaught(){
        this.Caught = true;
    }
}
