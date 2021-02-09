package com.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class Pokedex {

    private ArrayList<Pokemon> listOfPokemon;

    public Pokedex(String path){

        File file = new File(path);
        listOfPokemon = new ArrayList<>();
        try {
            JsonNode allPokemon = new ObjectMapper().readValue(file, JsonNode.class);
            for(JsonNode pokemon: allPokemon) {
                JsonNode pokemonName = pokemon.get("name");
                JsonNode statsURL = pokemon.get("url");
                Pokemon newPokemon = new Pokemon(pokemonName.asText(), statsURL.asText());
                listOfPokemon.add(newPokemon);
            }
        } catch(IOException e) {
            throw new IllegalArgumentException("Invalid path");
        }
    }

    public void printAllPokemon() {
        for(Pokemon toPrint: listOfPokemon) {
            System.out.println(toPrint.getPkmnName() + "\n" + toPrint.getBaseStats() + "\n");
        }
    }

}
