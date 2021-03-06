package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * Pokemon stores information about a single pokemon and extracts data from the PokeAPI given a URL
 *
 * @author Harry Chu
 */
public class Pokemon {

  private final String pokemonName;
  private final PokemonStats specificStats;

  public Pokemon(String name, String statsURL) {
    pokemonName = name;
    specificStats = deserializePokemonStats(statsURL);
  }

  public Pokemon() {
    pokemonName = "";
    specificStats = new PokemonStats();
  }

  public String getPkmnName() {
    return pokemonName;
  }

  public ArrayList<String> getTypes() {

    return specificStats.pokemonTypes;
  }

  public ArrayList<String> getPossibleMoves() {

    return specificStats.pokemonMoves;
  }

  public Map<String, Integer> getBaseStats() {

    return specificStats.pokemonBaseStats;
  }

  public int getHeight() {
    return specificStats.height;
  }

  public int getWeight() {
    return specificStats.weight;
  }

  private PokemonStats deserializePokemonStats(String pkmnStatsURL) {

    PokemonStats newStatsToReturn = new PokemonStats();

    // based on the format of the JSON,
    try {
      JsonNode allPokemonStats =
          new ObjectMapper().readValue(new URL(pkmnStatsURL), JsonNode.class);
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

    ArrayList<String> typesToReturn = new ArrayList<>();
    JsonNode pkmnTypeList = allPokemonStats.get("types");
    for (JsonNode type : pkmnTypeList) {
      JsonNode typeStats = type.get("type");
      String typeName = typeStats.get("name").asText();
      typesToReturn.add(typeName);
    }

    return typesToReturn;
  }

  private ArrayList<String> deserializePkmnMoves(JsonNode allPokemonStats) {

    ArrayList<String> movesToReturn = new ArrayList<>();
    JsonNode pkmnMoveList = allPokemonStats.get("moves");
    for (JsonNode move : pkmnMoveList) {
      JsonNode moveStats = move.get("move");
      String moveName = moveStats.get("name").asText();
      movesToReturn.add(moveName);
    }

    return movesToReturn;
  }

  private HashMap<String, Integer> deserializePkmnBaseStats(JsonNode allPokemonStats) {

    HashMap<String, Integer> movesToReturn = new HashMap<>();
    JsonNode baseStats = allPokemonStats.get("stats");

    for (JsonNode variousStats : baseStats) {
      int statValue = variousStats.get("base_stat").asInt();
      String statName = variousStats.get("stat").get("name").asText();
      movesToReturn.put(statName, statValue);
    }

    return movesToReturn;
  }

  /** PokemonStats stores specific details a single pokemon */
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