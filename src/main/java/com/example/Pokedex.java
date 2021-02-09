package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

public class Pokedex {

  private ArrayList<Pokemon> listOfPokemon;
  private static ArrayList<String> pokemonTypes;
  private static ArrayList<String> pokemonStatNames;

  public Pokedex(String fileName) {

    this();
    addPokemonFromJson(fileName);
  }

  public Pokedex() {

    listOfPokemon = new ArrayList<>();
    pokemonTypes = new ArrayList<>();
    pokemonStatNames = new ArrayList<>();

    addPokemonTypes();
    addPokemonStatNames();
  }

  private void addPokemonFromJson(String fileName) {

    File file = new File("src/main/resources/"+fileName);
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
  }

  private void addPokemonTypes() {
    try {
      JsonNode typeList =
              new ObjectMapper().readValue(new URL("https://pokeapi.co/api/v2/type/"), JsonNode.class).get("results");
      for (JsonNode typeName : typeList) {
        pokemonTypes.add(typeName.get("name").asText());
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("Invalid path for pokemon types");
    }
  }

  private void addPokemonStatNames() {
    try {
      JsonNode statList =
              new ObjectMapper().readValue(new URL("https://pokeapi.co/api/v2/stat/"), JsonNode.class).get("results");
      for (JsonNode statName : statList) {
        pokemonStatNames.add(statName.get("name").asText());
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("Invalid url");
    }

  }

  public void addPokemon(Pokemon toAdd) {
    listOfPokemon.add(toAdd);
  }

  public int pokedexSize() {
    return listOfPokemon.size();
  }

  public static ArrayList<String> getPokemonNames(Pokedex targetPokedex) {

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
    checkValidPokemonType(pokemonType);

    pokemonType = pokemonType.toLowerCase();
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
    checkValidPokemonType(pokemonType);
    if (pokemonMove == null || pokemonMove.length() == 0) {
      throw new IllegalArgumentException("Pokemon move cannot be null or empty");
    }

    Pokedex pkmnWithSpecificType =
        filterPokemonByType(targetPokedex, pokemonType, false);

    return findPokemonByMove(pkmnWithSpecificType, pokemonMove);
  }

  public static Pokedex filterPokemonByMinHeightAndWeight(
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

  public static ArrayList<String> filterPokemonMovesWithKeyword(
      Pokedex targetPokedex, String pokemonName, String keyword) {

    checkIfPokedexIsNull(targetPokedex);
    if (keyword == null || keyword.length() == 0) {
      throw new IllegalArgumentException("Keyword cannot be null or empty");
    }

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

  public static Pokemon findPokemonByName(Pokedex targetPokedex, String pokemonName) {

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

  public static Pokedex findPokemonByMove(Pokedex targetPokedex, String pokemonMove) {

    checkIfPokedexIsNull(targetPokedex);
    if (pokemonMove == null || pokemonMove.length() == 0) {
      throw new IllegalArgumentException("Pokemon move cannot be null or empty");
    }

    Pokedex pkmnWithSpecificMove = new Pokedex();
    pokemonMove = pokemonMove.toLowerCase();

    for (Pokemon targetPokemon : targetPokedex.listOfPokemon) {
      if (targetPokemon.getPossibleMoves().contains(pokemonMove)) {
        pkmnWithSpecificMove.addPokemon(targetPokemon);
      }
    }

    return pkmnWithSpecificMove;
  }

  // Analysis Methods Begin Here

  public static int numOfPokemonWithSpecificType(
      Pokedex targetPokedex, String pokemonType, boolean isPureTypeSearch) {

    return Pokedex.filterPokemonByType(targetPokedex, pokemonType, isPureTypeSearch)
        .pokedexSize();
  }

  public static Double averageBaseStatValueOfSpecificType(
      Pokedex targetPokedex, String statName, String pokemonType) {

    checkValidPokemonStat(statName);

    Pokedex pkmnWithDesiredType =
        Pokedex.filterPokemonByType(targetPokedex, pokemonType, false);

    double averageStatValue = 0;
    for (Pokemon targetPokemon : pkmnWithDesiredType.listOfPokemon) {
      averageStatValue += targetPokemon.getBaseStats().get(statName);
    }

    return averageStatValue / pkmnWithDesiredType.pokedexSize();
  }

  public static Pokedex sortPokemonByStatGivenMove(Pokedex targetPokedex, String statName, String pokemonMove, boolean hasGreatestFirst) {

    checkIfPokedexIsNull(targetPokedex);
    checkValidPokemonStat(statName);

    Pokedex pkmnWithSpecificMove =  findPokemonByMove(targetPokedex, pokemonMove);

    PokemonBaseStatComparator statComparator = new PokemonBaseStatComparator(statName);
    pkmnWithSpecificMove.listOfPokemon.sort(statComparator);

    if(hasGreatestFirst) {
      Collections.reverse(pkmnWithSpecificMove.listOfPokemon);
      return pkmnWithSpecificMove;
    }

    return pkmnWithSpecificMove;
  }

  public static int numOfSpecificTypeOfAttacker(Pokedex targetPokedex, boolean isSpecialAttacker) {

    checkIfPokedexIsNull(targetPokedex);

    int numOfAttackers = 0;

    for(Pokemon targetPokemon: targetPokedex.listOfPokemon) {
      int specialAttackStat = targetPokemon.getBaseStats().get("special-attack");
      int attackStat = targetPokemon.getBaseStats().get("attack");

      if(isSpecialAttacker) {
        if(specialAttackStat >= attackStat) {
          numOfAttackers++;
        }
      } else {
        if(attackStat >= specialAttackStat) {
          numOfAttackers++;
        }
      }
    }

    return numOfAttackers;
  }

  //Exception Checking Methods Begin Here

  private static void checkIfPokedexIsNull(Pokedex targetPokedex) {
    if (targetPokedex == null) {
      throw new IllegalArgumentException("Passed Pokedex cannot be null");
    }
  }

  private static void checkValidPokemonType(String pokemonType) {

    if (pokemonType == null || pokemonType.length() == 0) {
      throw new IllegalArgumentException("Pokemon type cannot be null or empty");
    }
    if (!pokemonTypes.contains(pokemonType.toLowerCase())) {
      throw new NoSuchElementException("Pokemon type does not exist");
    }
  }

  private static void checkValidPokemonStat(String statName) {

    if (statName == null || statName.length() == 0) {
      throw new IllegalArgumentException("Pokemon stat cannot be null or empty");
    }
    if (!pokemonStatNames.contains(statName.toLowerCase())) {
      throw new NoSuchElementException("Pokemon stat does not exist");
    }
  }
}
