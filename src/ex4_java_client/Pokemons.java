package src.ex4_java_client;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import src.ex4_java_client.Pokemon;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Pokemons {
    Pokemon Pokemon;
    private ArrayList<Pokemon> Pokemons;
    public Pokemons(){
        this.Pokemons = new ArrayList<Pokemon>();
    }
    public Pokemons(String json){
        Gson gson =new Gson();
        Pokemons a = gson.fromJson(json, Pokemons.class);
        this.Pokemons=a.Pokemons;
        System.out.println(this.Pokemons.get(0).getSpeed());
    }
}
