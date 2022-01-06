package src.ex4_java_client;

import Graph.Point3D;

public class Pokemon {
    private PokemonClass Pokemon;

    public Pokemon() {
        this.Pokemon = null;
    }

    public Point3D getPosition() {
        return Pokemon.getPos();
    }
    public int getType() {
        return Pokemon.getType();
    }
}
