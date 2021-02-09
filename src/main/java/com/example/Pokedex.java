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
            System.out.println(toPrint.getPkmnName());
        }
    }

    public ArrayList<Pokemon> filterPokemonWithSpecificType(String pokemonType,boolean isPureTypeSearch) {
        ArrayList<Pokemon> pkmnWithSpecficType = new ArrayList<>();

        for(Pokemon pkmnToFind: listOfPokemon) {
            if(pkmnToFind.getTypes().contains(pokemonType)) {
                if(!isPureTypeSearch) {
                    pkmnWithSpecficType.add(pkmnToFind);
                } else {
                    if(pkmnToFind.getTypes().split(",").length < 2) {
                        pkmnWithSpecficType.add(pkmnToFind);
                    }
                }
            }
        }

        return pkmnWithSpecficType;
    }

    public int numOfPokemonWithSpecificType(String pokemonType, boolean isPureTypeSearch) {

        return filterPokemonWithSpecificType(pokemonType, isPureTypeSearch).size();
    }

    public Double averageBaseStatValueOfSpecificType(String statName, String pokemonType) {

        ArrayList<Pokemon> pkmnWithDesiredType = filterPokemonWithSpecificType(pokemonType, false);

        double averageStatValue = 0;
        for(Pokemon targetPokemon: pkmnWithDesiredType) {
            averageStatValue += targetPokemon.getBaseStats().get(statName);
        }

        return averageStatValue/pkmnWithDesiredType.size();
    }
}
