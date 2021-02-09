package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Pokemon {

    private String pkmnName;
    private PokemonStats pkmnStats;

    public Pokemon(String name, String statsURL){
        pkmnName = name;
        pkmnStats = deserializePokemonStats(statsURL);
    }

    public String getPkmnName() {
        return pkmnName;
    }

    public String getTypes() {
        StringBuilder returnedPokemonTypes = new StringBuilder();
        returnedPokemonTypes.append("Types: ");
        for (String pokeType : pkmnStats.pokemonTypes) {
            returnedPokemonTypes.append(pokeType);
            returnedPokemonTypes.append(", ");
        }

        return returnedPokemonTypes.substring(0, returnedPokemonTypes.length()-2);
    }

    public String getPossibleMoves() {
        StringBuilder pokemonMoves = new StringBuilder();
        pokemonMoves.append("Types: ");
        for (String pokeMove : pkmnStats.pokemonMoves) {
            pokemonMoves.append(pokeMove);
            pokemonMoves.append(", ");
        }

        return pokemonMoves.substring(0, pokemonMoves.length()-2);
    }

    private PokemonStats deserializePokemonStats(String pkmnStatsURL) {

        PokemonStats newStatsToReturn = new PokemonStats();

        try {
            JsonNode allPokemonStats = new ObjectMapper().readValue(new URL(pkmnStatsURL), JsonNode.class);
            newStatsToReturn.pokemonTypes = deserializePkmnTypes(allPokemonStats);
            newStatsToReturn.pokemonMoves = deserializePkmnMoves(allPokemonStats);
            newStatsToReturn.weight = allPokemonStats.get("weight").asInt();
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid url");
        }
        return newStatsToReturn;
    }

    private ArrayList<String> deserializePkmnTypes(JsonNode allPokemonStats) {

        ArrayList<String> typesToReturn =  new ArrayList<>();
        JsonNode pkmnTypeList = allPokemonStats.get("types");
        for(JsonNode type: pkmnTypeList) {
            JsonNode typeStats = type.get("type");
            String typeName = typeStats.get("name").asText();
            typesToReturn.add(typeName);
        }

        return typesToReturn;
    }

    private ArrayList<String> deserializePkmnMoves(JsonNode allPokemonStats) {

        ArrayList<String> movesToReturn =  new ArrayList<>();
        JsonNode pkmnMoveList = allPokemonStats.get("moves");
        for(JsonNode move: pkmnMoveList) {
            JsonNode moveStats = move.get("move");
            String moveName = moveStats.get("name").asText();
            movesToReturn.add(moveName);
        }

        return movesToReturn;
    }

    public class PokemonStats {
        public ArrayList<String> pokemonTypes;
        public ArrayList<String> pokemonMoves;
        public int weight;

        public PokemonStats() {
            pokemonTypes = new ArrayList<>();
            pokemonMoves = new ArrayList<>();
        }
    }
}
