package com.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Pokedex {

    private ArrayList<Pokemon> listOfPokemon;

    public Pokedex(String path){

        File file = new File(path);
        try {
            listOfPokemon = new ObjectMapper().readValue(file, new TypeReference<ArrayList<Pokemon>>(){});
        } catch(IOException e) {
            throw new IllegalArgumentException("Invalid path");
        }

        initializePokemonStats();
    }

    public void printAllPokemon() {
        for(Pokemon toPrint: listOfPokemon) {
            System.out.println(toPrint.getPkmnName() + " - " + toPrint.getTypes());
        }
    }

    private void initializePokemonStats() {
        for(Pokemon toInitialize: listOfPokemon) {
            toInitialize.extractPokemonStatsFromUrl();
        }
    }
}
