package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Pokedex {

  private static ArrayList<String> pokemonTypes;
  private ArrayList<Pokemon> listOfPokemon;

  public Pokedex(String path) {

    File file = new File(path);
    listOfPokemon = new ArrayList<>();
    try {
      JsonNode allPokemon = new ObjectMapper().readValue(file, JsonNode.class);
      for (JsonNode pokemon : allPokemon) {
        JsonNode pokemonName = pokemon.get("name");
        JsonNode statsURL = pokemon.get("url");
        Pokemon newPokemon = new Pokemon(pokemonName.asText(), statsURL.asText());
        listOfPokemon.add(newPokemon);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("Invalid path");
    }

    addPokemonTypesFromJson();
  }

  public Pokedex() {
    listOfPokemon = new ArrayList<>();
  }

  public void addPokemonTypesFromJson() {

    File file = new File("src/main/resources/pokemontypes.json");
    pokemonTypes = new ArrayList<>();
    try {
      JsonNode allTypes = new ObjectMapper().readValue(file, JsonNode.class).get("results");
      for (JsonNode typeStats : allTypes) {
        pokemonTypes.add(typeStats.get("name").asText());
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("Invalid path for pokemon types");
    }
  }

  public void addPokemon(Pokemon toAdd) {
    listOfPokemon.add(toAdd);
  }

  public int pokedexSize() {
    return listOfPokemon.size();
  }

  public static ArrayList<String> getALlPokemonNames(Pokedex targetPokedex) {

    checkIfPokedexIsNull(targetPokedex);

    ArrayList<String> pokemonNames = new ArrayList<>();
    for (Pokemon targetPokemon : targetPokedex.listOfPokemon) {
      pokemonNames.add(targetPokemon.getPkmnName());
    }

    return pokemonNames;
  }

  // Filtering Methods Begin Here

  public static Pokedex filterPokemonByType(
      Pokedex targetPokedex, String pokemonType, boolean isPureTypeSearch) {

    checkIfPokedexIsNull(targetPokedex);
    if (pokemonType == null || pokemonType.length() == 0) {
      throw new IllegalArgumentException("Pokemon type cannot be null or empty");
    }
    pokemonType = pokemonType.toLowerCase();
    checkValidPokemonType(pokemonType);

    Pokedex pkmnWithSpecificType = new Pokedex();
    for (Pokemon targetPokemon : targetPokedex.listOfPokemon) {

      if (targetPokemon.getTypes().contains(pokemonType)) {
        if (!isPureTypeSearch) {
          pkmnWithSpecificType.addPokemon(targetPokemon);
        } else {
          if (targetPokemon.getTypes().size() < 2) {
            pkmnWithSpecificType.addPokemon(targetPokemon);
          }
        }
      }
    }

    return pkmnWithSpecificType;
  }

  public static Pokedex filterPokemonByMinTotalBaseStats(
      Pokedex targetPokedex, int minimumTotalBaseStats) {

    checkIfPokedexIsNull(targetPokedex);

    if (minimumTotalBaseStats < 0) {
      throw new IllegalArgumentException("Total base stats cannot be negative");
    }

    Pokedex validPokemon = new Pokedex();
    for (Pokemon targetPokemon : targetPokedex.listOfPokemon) {

      int totalBaseStats = 0;
      for (Integer statValue : targetPokemon.getBaseStats().values()) {
        totalBaseStats += statValue;
      }

      if (totalBaseStats >= minimumTotalBaseStats) {
        validPokemon.addPokemon(targetPokemon);
      }
    }

    return validPokemon;
  }

  public static Pokedex filterPokemonByTypeWithMove(
      Pokedex targetPokedex, String pokemonType, String pokemonMove) {

    checkIfPokedexIsNull(targetPokedex);

    if (pokemonType == null || pokemonType.length() == 0) {
      throw new IllegalArgumentException("Pokemon type cannot be null or empty");
    }

    if (pokemonMove == null || pokemonMove.length() == 0) {
      throw new IllegalArgumentException("Pokemon move cannot be null or empty");
    }

    pokemonType = pokemonType.toLowerCase();
    checkValidPokemonType(pokemonType);

    Pokedex pkmnWithSpecificType =
        filterPokemonByType(targetPokedex, pokemonType, false);
    Pokedex pkmnWithSpecificMove = new Pokedex();

    pokemonMove = pokemonMove.toLowerCase();
    for (Pokemon targetPokemon : pkmnWithSpecificType.listOfPokemon) {
      if (targetPokemon.getPossibleMoves().contains(pokemonMove)) {
        pkmnWithSpecificMove.addPokemon(targetPokemon);
      }
    }

    return pkmnWithSpecificMove;
  }

  public static Pokedex filterPokemonByMinimumumHeightAndWeight(
      Pokedex targetPokedex, int minHeight, int minWeight) {

    checkIfPokedexIsNull(targetPokedex);

    if (minHeight < 0 || minWeight < 0) {
      throw new IllegalArgumentException("Minimum height and weight cannot be negative");
    }

    Pokedex validPkmn = new Pokedex();
    for (Pokemon targetPokemon : targetPokedex.listOfPokemon) {
      if (targetPokemon.getHeight() >= minHeight && targetPokemon.getWeight() >= minWeight) {
        validPkmn.addPokemon(targetPokemon);
      }
    }

    return validPkmn;
  }

  public static ArrayList<String> filterMovesWithKeywordFromPokemon(
      Pokedex targetPokedex, String pokemonName, String keyword) {

    Pokemon targetPokemon = findPokemonByName(targetPokedex, pokemonName);

    if (targetPokemon == null) {
      throw new NoSuchElementException("Pokemon does not exist in the pokedex");
    }

    keyword = keyword.toLowerCase();
    ArrayList<String> possibleMoves = targetPokemon.getPossibleMoves();
    ArrayList<String> validMoves = new ArrayList<>();

    for (String targetMove : possibleMoves) {
      if (targetMove.contains(keyword)) {
        validMoves.add(targetMove);
      }
    }

    return validMoves;
  }

  private static Pokemon findPokemonByName(Pokedex targetPokedex, String pokemonName) {

    checkIfPokedexIsNull(targetPokedex);

    if (pokemonName == null || pokemonName.length() == 0) {
      throw new IllegalArgumentException("Pokemon name cannot be null or empty");
    }

    pokemonName = pokemonName.toLowerCase();
    for (Pokemon targetPokemon : targetPokedex.listOfPokemon) {
      if (targetPokemon.getPkmnName().equals(pokemonName)) {
        return targetPokemon;
      }
    }

    return null;
  }

  // Analysis Methods Begin Here

  public static int numOfPokemonWithSpecificType(
      Pokedex targetPokedex, String pokemonType, boolean isPureTypeSearch) {

    return Pokedex.filterPokemonByType(targetPokedex, pokemonType, isPureTypeSearch)
        .pokedexSize();
  }

  public static Double averageBaseStatValueOfSpecificType(
      Pokedex targetPokedex, String statName, String pokemonType) {

    Pokedex pkmnWithDesiredType =
        Pokedex.filterPokemonByType(targetPokedex, pokemonType, false);

    double averageStatValue = 0;
    for (Pokemon targetPokemon : pkmnWithDesiredType.listOfPokemon) {
      averageStatValue += targetPokemon.getBaseStats().get(statName);
    }

    return averageStatValue / pkmnWithDesiredType.pokedexSize();
  }

  //Exception Checking Methods Begin Here

  private static void checkIfPokedexIsNull(Pokedex targetPokedex) {
    if (targetPokedex == null) {
      throw new IllegalArgumentException("Passed Pokedex cannot be null");
    }
  }

  private static void checkValidPokemonType(String pokemonType) {
    if (!pokemonTypes.contains(pokemonType)) {
      throw new IllegalArgumentException("Passed pokemon type does not exist");
    }
  }
}
