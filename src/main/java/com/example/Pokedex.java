package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

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

  //Filtering Methods Begin Here

  public static ArrayList<Pokemon> filterPokemonWithSpecificType(
      Pokedex targetPokedex, String pokemonType, boolean isPureTypeSearch) {

    ArrayList<Pokemon> pkmnWithSpecficType = new ArrayList<>();
    for (Pokemon toExamine : targetPokedex.listOfPokemon) {

      if (toExamine.getTypes().contains(pokemonType)) {
        if (!isPureTypeSearch) {
          pkmnWithSpecficType.add(toExamine);
        } else {
          if (toExamine.getTypes().split(",").length < 2) {
            pkmnWithSpecficType.add(toExamine);
          }
        }
      }
    }

    return pkmnWithSpecficType;
  }

  public static ArrayList<Pokemon> filterPokemonByMinimumTotalBaseStats(
      int minimumTotalBaseStats, Pokedex targetPokedex) {

    ArrayList<Pokemon> validPokemon = new ArrayList<>();
    for (Pokemon toExamine : targetPokedex.listOfPokemon) {

      int totalBaseStats = 0;
      for (Integer statValue : toExamine.getBaseStats().values()) {
        totalBaseStats += statValue;
      }

      if (totalBaseStats >= minimumTotalBaseStats) {
        validPokemon.add(toExamine);
      }
    }

    return validPokemon;
  }

  public static ArrayList<Pokemon> filterPokemonByTypeWithMove(Pokedex targetPokedex, String pokemonType, String pokemonMove) {
    ArrayList<Pokemon> pkmnWithSpecificType = filterPokemonWithSpecificType(targetPokedex, pokemonType, false);
    ArrayList<Pokemon> pkmnWithSpecificMove = new ArrayList<>();

    for(Pokemon toExamine: pkmnWithSpecificType) {
      if(toExamine.getPossibleMoves().contains(pokemonMove)) {
        pkmnWithSpecificMove.add(toExamine);
      }
    }

    return pkmnWithSpecificMove;
  }

  public static ArrayList<Pokemon> filterPokemonByMinimumumHeightandWeight(Pokedex targetPokedex, int minHeight, int minWeight) {

    ArrayList<Pokemon> validPkmn = new ArrayList<>();

    for(Pokemon toExamine: targetPokedex.listOfPokemon) {
      if(toExamine.getHeight() >= minHeight && toExamine.getWeight() >= minWeight) {
        validPkmn.add(toExamine);
      }
    }

    return validPkmn;
  }

  public static Pokemon findPokemonByName(Pokedex targetPokeDex, String name) {
    for(Pokemon toExamine: targetPokeDex.listOfPokemon) {
      if(toExamine.getPkmnName().equals(name)) {
        return toExamine;
      }
    }

    return null;
  }

  public static ArrayList<String> filterMovesWithKeywordFromPokemon(Pokedex targetPokedex, String pokemonName, String keyword) {

    Pokemon toExamine = findPokemonByName(targetPokedex, pokemonName);

    if(toExamine == null) {
      throw new NoSuchElementException("Pokemon does not exist in the pokedex");
    }

    int indexOfPokemon = targetPokedex.listOfPokemon.indexOf(toExamine);
    keyword = keyword.toLowerCase();

    ArrayList<String> possibleMoves = targetPokedex.listOfPokemon.get(indexOfPokemon).getPossibleMoves();
    ArrayList<String> validMoves = new ArrayList<>();

    for(String move: possibleMoves) {
      if(move.contains(keyword)) {
        validMoves.add(move);
      }
    }

    return validMoves;
  }

  //Analysis Methods Begin Here

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
    for (Pokemon toExamine : pkmnWithDesiredType) {
      averageStatValue += toExamine.getBaseStats().get(statName);
    }

    return averageStatValue / pkmnWithDesiredType.size();
  }
}
