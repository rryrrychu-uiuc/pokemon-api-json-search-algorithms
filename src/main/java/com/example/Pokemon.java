package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Pokemon {

    private String name;
    private String url;
    private ArrayList<String> pokemonTypes;

    public Pokemon(){ }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getTypes() {
        StringBuilder returnedPokemonTypes = new StringBuilder();
        returnedPokemonTypes.append("Types: ");
        for(String pokeType: pokemonTypes) {
            returnedPokemonTypes.append(pokeType);
            returnedPokemonTypes.append(" ");
        }

        return returnedPokemonTypes.toString();
    }

    public void extractPokemonStatsFromUrl() {

        pokemonTypes = new ArrayList<>();
        try {
            JsonNode allPokemonStats = new ObjectMapper().readValue(new URL(url), JsonNode.class);
            JsonNode pkmnTypeList = allPokemonStats.get("types");
            for(JsonNode type: pkmnTypeList) {
                JsonNode typeStats = type.get("type");
                String typeName = typeStats.get("name").asText();
                pokemonTypes.add(typeName);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid url");
        }
    }
}
