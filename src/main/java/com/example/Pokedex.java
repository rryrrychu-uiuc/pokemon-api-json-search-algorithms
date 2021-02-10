package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

/**
 * Pokedex acts as a storage system for Pokemon that implements various analysis functions (to
 * extrapolate details about a set of Pokemon) and filter functions (to create new subsets of
 * Pokemon)
 *
 * @author Harry Chu
 */
public class Pokedex {

  private static ArrayList<String> pokemonTypes;
  private static ArrayList<String> pokemonStatNames;
  private ArrayList<Pokemon> listOfPokemon;

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

  /**
   * Extracts a list of Pokemon with a specific type (filter).
   *
   * @param targetPokedex pokedex to filter
   * @param pokemonType the type to filter for
   * @param isPureTypeSearch true if searching for pokemon with only one type
   * @return a pokedex containing the pokemon with desired type
   */
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

  /**
   * Extracts a list of Pokemon with a specific move (filter).
   *
   * @param targetPokedex pokedex to filter
   * @param pokemonMove the move to search for
   * @return a pokedex containing the pokemon with the desired move
   */
  public static Pokedex filterPokemonByMove(Pokedex targetPokedex, String pokemonMove) {

    checkIfPokedexIsNull(targetPokedex);
    if (pokemonMove == null || pokemonMove.length() == 0) {
      throw new IllegalArgumentException("Pokemon move cannot be null or empty");
    }

    pokemonMove = pokemonMove.toLowerCase();

    Pokedex pkmnWithSpecificMove = new Pokedex();
    for (Pokemon targetPokemon : targetPokedex.listOfPokemon) {
      if (targetPokemon.getPossibleMoves().contains(pokemonMove)) {
        pkmnWithSpecificMove.addPokemon(targetPokemon);
      }
    }

    return pkmnWithSpecificMove;
  }

  /**
   * Extracts a list of Pokemon with stats above a minimum level (filter).
   *
   * @param targetPokedex pokedex to filter
   * @param minimumTotalBaseStats the lowest total base stat to filter
   * @return a pokedex containing the pokemon with desired stats
   */
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

  /**
   * Extract a list of Pokemon with a specific type and move (filter).
   *
   * @param targetPokedex pokedex to filter
   * @param pokemonType the type to filter for
   * @param pokemonMove the move to filter for
   * @return a pokedex containing the pokemon with the desired type and move
   */
  public static Pokedex filterPokemonByTypeAndMove(
      Pokedex targetPokedex, String pokemonType, String pokemonMove) {

    checkIfPokedexIsNull(targetPokedex);
    checkValidPokemonType(pokemonType);
    if (pokemonMove == null || pokemonMove.length() == 0) {
      throw new IllegalArgumentException("Pokemon move cannot be null or empty");
    }

    Pokedex pkmnWithSpecificType = filterPokemonByType(targetPokedex, pokemonType, false);

    return filterPokemonByMove(pkmnWithSpecificType, pokemonMove);
  }

  /**
   * Extract the list of Pokemon above a certain height and weight (filter).
   *
   * @param targetPokedex pokedex to filter
   * @param minHeight the minimum height
   * @param minWeight the minimum weight
   * @return a pokedex containing the pokemon above the desired weight and height
   */
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

  /**
   * Extract the list of moves from a pokemon given a keyword (filter).
   *
   * @param targetPokedex pokedex where the pokemon is
   * @param pokemonName name of the pokemon
   * @param keyword word to search for
   * @return list of moves with the keyword from a pokemon
   */
  public static ArrayList<String> filterPokemonMovesWithKeyword(
      Pokedex targetPokedex, String pokemonName, String keyword) {

    checkIfPokedexIsNull(targetPokedex);
    if (keyword == null || keyword.length() == 0) {
      throw new IllegalArgumentException("Keyword cannot be null or empty");
    }

    Pokemon targetPokemon = searchForPokemonByName(targetPokedex, pokemonName);
    if (targetPokemon == null) {
      throw new NoSuchElementException("Pokemon does not exist in the pokedex");
    }

    ArrayList<String> possibleMoves = targetPokemon.getPossibleMoves();
    keyword = keyword.toLowerCase();

    ArrayList<String> validMoves = new ArrayList<>();
    for (String targetMove : possibleMoves) {
      if (targetMove.contains(keyword)) {
        validMoves.add(targetMove);
      }
    }

    return validMoves;
  }

  /**
   * Extracts a Pokemon with a specific name (analysis).
   *
   * @param targetPokedex pokedex to analyze
   * @param pokemonName the name to search for
   * @return a pokemon with the desired name
   */
  public static Pokemon searchForPokemonByName(Pokedex targetPokedex, String pokemonName) {

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

  /**
   * Find the number of Pokemon with a specific type (analysis).
   *
   * @param targetPokedex pokedex to analyze
   * @param pokemonType the type to search for
   * @param isPureTypeSearch true if searching for pokemon with only one type
   * @return the number of pokemon with the desired type
   */
  public static int numOfPokemonWithSpecificType(
      Pokedex targetPokedex, String pokemonType, boolean isPureTypeSearch) {

    return Pokedex.filterPokemonByType(targetPokedex, pokemonType, isPureTypeSearch).pokedexSize();
  }

  /**
   * Find the average value of a type's stat (analysis)
   *
   * @param targetPokedex pokedex to analyze
   * @param pokemonType the type to search for
   * @param statName the stat to average
   * @return the average of a specific type's stat
   */
  public static Double averageBaseStatValueOfSpecificType(
      Pokedex targetPokedex, String pokemonType, String statName) {

    checkValidPokemonStat(statName);

    Pokedex pkmnWithDesiredType = Pokedex.filterPokemonByType(targetPokedex, pokemonType, false);

    double averageStatValue = 0;
    for (Pokemon targetPokemon : pkmnWithDesiredType.listOfPokemon) {
      averageStatValue += targetPokemon.getBaseStats().get(statName);
    }

    return averageStatValue / pkmnWithDesiredType.pokedexSize();
  }

  /**
   * A sorted list of Pokemon with a specific move (analysis).
   *
   * @param targetPokedex pokedex to analyze
   * @param statName the stat to sort by
   * @param pokemonMove the move to search for
   * @param hasGreatestFirst the order to sort by
   * @return a pokedex containing a sorted list of pokemon
   */
  public static Pokedex sortPokemonByStatGivenMove(
      Pokedex targetPokedex, String statName, String pokemonMove, boolean hasGreatestFirst) {

    checkIfPokedexIsNull(targetPokedex);
    checkValidPokemonStat(statName);

    Pokedex pkmnWithSpecificMove = filterPokemonByMove(targetPokedex, pokemonMove);

    PokemonBaseStatComparator statComparator = new PokemonBaseStatComparator(statName);
    pkmnWithSpecificMove.listOfPokemon.sort(statComparator);

    if (hasGreatestFirst) {
      Collections.reverse(pkmnWithSpecificMove.listOfPokemon);
      return pkmnWithSpecificMove;
    }

    return pkmnWithSpecificMove;
  }

  /**
   * Find the number of special or physical attacking Pokemon (analysis).
   *
   * @param targetPokedex pokedex to analyze
   * @param isSpecialAttacker true if searching for special attackers
   * @return the number of special or physical attaking pokemon
   */
  public static int numOfSpecificTypeOfAttacker(Pokedex targetPokedex, boolean isSpecialAttacker) {

    checkIfPokedexIsNull(targetPokedex);

    int numOfAttackers = 0;

    for (Pokemon targetPokemon : targetPokedex.listOfPokemon) {
      int specialAttackStat = targetPokemon.getBaseStats().get("special-attack");
      int attackStat = targetPokemon.getBaseStats().get("attack");

      if (isSpecialAttacker) {
        if (specialAttackStat >= attackStat) {
          numOfAttackers++;
        }
      } else {
        if (attackStat >= specialAttackStat) {
          numOfAttackers++;
        }
      }
    }

    return numOfAttackers;
  }

  /**
   * Extract the list of Pokemon names from a Pokedex (analysis).
   *
   * @param targetPokedex pokedex to extract names from
   * @return list of pokemon names
   */
  public static ArrayList<String> getPokemonNames(Pokedex targetPokedex) {

    checkIfPokedexIsNull(targetPokedex);

    ArrayList<String> pokemonNames = new ArrayList<>();
    for (Pokemon targetPokemon : targetPokedex.listOfPokemon) {
      pokemonNames.add(targetPokemon.getPkmnName());
    }

    return pokemonNames;
  }

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

  // populates Pokedex with first 151 pokemon
  private void addPokemonFromJson(String fileName) {
    File file = new File("src/main/resources/" + fileName);
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

  // deserializes PokemonTypes from API
  private void addPokemonTypes() {
    try {
      JsonNode typeList =
          new ObjectMapper()
              .readValue(new URL("https://pokeapi.co/api/v2/type/"), JsonNode.class)
              .get("results");
      for (JsonNode typeName : typeList) {
        pokemonTypes.add(typeName.get("name").asText());
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("Invalid path for pokemon types");
    }
  }

  // deserializes PokemonStats from API
  private void addPokemonStatNames() {
    try {
      JsonNode statList =
          new ObjectMapper()
              .readValue(new URL("https://pokeapi.co/api/v2/stat/"), JsonNode.class)
              .get("results");
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
}