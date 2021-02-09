package com.example;

import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;

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
        StringBuilder returnPokemonMoves = new StringBuilder();
        returnPokemonMoves.append("Types: ");
        for (String pokeMove : pkmnStats.pokemonMoves) {
            returnPokemonMoves.append(pokeMove);
            returnPokemonMoves.append(", ");
        }

        return returnPokemonMoves.substring(0, returnPokemonMoves.length()-2);
    }

    public String getBaseStats() {
        StringBuilder returnBaseStats = new StringBuilder();
        returnBaseStats.append("Base Stats: ");
        Set<String> statNames = pkmnStats.pokemonBaseStats.keySet();
        for(String stat: statNames) {
            returnBaseStats.append("\n");
            returnBaseStats.append(stat);
            returnBaseStats.append(": ");
            returnBaseStats.append(pkmnStats.pokemonBaseStats.get(stat));
        }

        return returnBaseStats.toString();
    }

    private PokemonStats deserializePokemonStats(String pkmnStatsURL) {

        PokemonStats newStatsToReturn = new PokemonStats();

        try {
            JsonNode allPokemonStats = new ObjectMapper().readValue(new URL(pkmnStatsURL), JsonNode.class);
            newStatsToReturn.pokemonTypes = deserializePkmnTypes(allPokemonStats);
            newStatsToReturn.pokemonMoves = deserializePkmnMoves(allPokemonStats);
            newStatsToReturn.pokemonBaseStats = deserializePkmnBaseStats(allPokemonStats);
            newStatsToReturn.weight = allPokemonStats.get("weight").asInt();
            newStatsToReturn.height = allPokemonStats.get("height").asInt();
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

    private HashMap<String, Integer> deserializePkmnBaseStats(JsonNode allPokemonStats) {

        HashMap<String, Integer> movesToReturn =  new HashMap<>();
        JsonNode baseStats = allPokemonStats.get("stats");

        for(JsonNode variousStats: baseStats) {
            int statValue = variousStats.get("base_stat").asInt();
            String statName = variousStats.get("stat").get("name").asText();
            movesToReturn.put(statName, statValue);
        }

        return movesToReturn;
    }

    public class PokemonStats {
        public ArrayList<String> pokemonTypes;
        public ArrayList<String> pokemonMoves;
        public HashMap<String, Integer> pokemonBaseStats;
        public int weight;
        public int height;

        public PokemonStats() {
            pokemonTypes = new ArrayList<>();
            pokemonMoves = new ArrayList<>();
            pokemonBaseStats = new HashMap<>();
        }
    }
}
