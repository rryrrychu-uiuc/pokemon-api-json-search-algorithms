package com.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Pokemon {

    @JsonProperty("name")
    private String pkmnName;

    @JsonProperty("url")
    private String pkmnStatsURL;

    private ArrayList<String> pokemonTypes;

    public Pokemon(){ }

    public String getPkmnName() {
        return pkmnName;
    }

    public String getPkmnStatsURL() {
        return pkmnStatsURL;
    }

    public String getTypes() {
        StringBuilder returnedPokemonTypes = new StringBuilder();
        returnedPokemonTypes.append("Types: ");
        for(String pokeType: pokemonTypes) {
            returnedPokemonTypes.append(pokeType);
            returnedPokemonTypes.append(", ");
        }

    return returnedPokemonTypes.substring(0, returnedPokemonTypes.length()-2);
    }

    public void extractPokemonStatsFromUrl() {
        pokemonTypes = new ArrayList<>();
        try {
            JsonNode allPokemonStats = new ObjectMapper().readValue(new URL(pkmnStatsURL), JsonNode.class);
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
