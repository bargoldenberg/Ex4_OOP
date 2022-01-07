package src.ex4_java_client;

import Graph.Point3D;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Pokemons {
    private ArrayList<Pokemon> Pokemons;

    public Pokemons(){
        this.Pokemons = new ArrayList<Pokemon>();
    }


    public void loadjsonstring(String json){
        Gson gson = new Gson();
        String j ="{\"Pokemons\":[{\"Pokemon\"{\"value\":5.0,\"type\":-1,\"pos\":\"35.188900353135324,32.105320110855615,0.0\"}},{\"Pokemon\":{\"value\":8.0,\"type\":-1,\"pos\":\"35.206679711961414,32.10571613186106,0.0\"}}]}";


        Pokemons poke = gson.fromJson(json, Pokemons.class);
        this.Pokemons=poke.Pokemons;
    }
    public ArrayList<Pokemon> GetPokeList() {
        return this.Pokemons;
    }

}
