package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Pokedex {

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
  }

  public static void printAllPokemon(Pokedex targetPokedex) {
    for (Pokemon toPrint : targetPokedex.listOfPokemon) {
      System.out.println(toPrint.getPkmnName());
    }
  }

  public static ArrayList<Pokemon> filterPokemonWithSpecificType(
      Pokedex targetPokedex, String pokemonType, boolean isPureTypeSearch) {
    ArrayList<Pokemon> pkmnWithSpecficType = new ArrayList<>();

    for (Pokemon pkmnToFind : targetPokedex.listOfPokemon) {
      if (pkmnToFind.getTypes().contains(pokemonType)) {
        if (!isPureTypeSearch) {
          pkmnWithSpecficType.add(pkmnToFind);
        } else {
          if (pkmnToFind.getTypes().split(",").length < 2) {
            pkmnWithSpecficType.add(pkmnToFind);
          }
        }
      }
    }

    return pkmnWithSpecficType;
  }

  public static ArrayList<Pokemon> filterPokemonByTotalBaseStats(
      int minimumTotalBaseStats, Pokedex targetPokedex) {

    ArrayList<Pokemon> validPokemon = new ArrayList<>();

    for (Pokemon pkmnToExamine : targetPokedex.listOfPokemon) {

      int totalBaseStats = 0;
      for (Integer statValue : pkmnToExamine.getBaseStats().values()) {
        totalBaseStats += statValue;
      }

      if (totalBaseStats > minimumTotalBaseStats) {
        validPokemon.add(pkmnToExamine);
      }
    }

    return validPokemon;
  }

  public static int numOfPokemonWithSpecificType(
      Pokedex targetPokedex, String pokemonType, boolean isPureTypeSearch) {

    return Pokedex.filterPokemonWithSpecificType(targetPokedex, pokemonType, isPureTypeSearch)
        .size();
  }

  public static Double averageBaseStatValueOfSpecificType(
      Pokedex targetPokedex, String statName, String pokemonType) {

    ArrayList<Pokemon> pkmnWithDesiredType =
        Pokedex.filterPokemonWithSpecificType(targetPokedex, pokemonType, false);

    double averageStatValue = 0;
    for (Pokemon targetPokemon : pkmnWithDesiredType) {
      averageStatValue += targetPokemon.getBaseStats().get(statName);
    }

    return averageStatValue / pkmnWithDesiredType.size();
  }
}
