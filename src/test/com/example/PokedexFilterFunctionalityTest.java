package com.example;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PokedexFilterFunctionalityTest {

  public static Pokedex original151Pokedex;

  @BeforeClass
  public static void setUp() {

    original151Pokedex = new Pokedex("src/main/resources/original151pokemon.json");
  }

  //Test cases for filterPokemonByType

  @Test(expected = IllegalArgumentException.class)
  public void testTypeFilterWithNullPokedex() {
    Pokedex.filterPokemonByType(null, "water", false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeFilterWithTypeDoesNotExist() {
    Pokedex.filterPokemonByType(original151Pokedex, "calculator", false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeFilterWithTypeEmpty() {
    Pokedex.filterPokemonByType(original151Pokedex, "", false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeFilterWithTypeNull() {
    Pokedex.filterPokemonByType(original151Pokedex, null, false);
  }

  @Test
  public void testWithNotPurePokemonType() {

    String[] expectedPokemonNames = {
      "charmander",
      "charmeleon",
      "charizard",
      "vulpix",
      "ninetales",
      "growlithe",
      "arcanine",
      "ponyta",
      "rapidash",
      "magmar",
      "flareon",
      "moltres"
    };

    ArrayList<Pokemon> targetedPokemon =
        Pokedex.filterPokemonByType(original151Pokedex, "fire", false);

    ArrayList<String> actualPokemonNames = new ArrayList<>();

    for (Pokemon toAdd : targetedPokemon) {
      actualPokemonNames.add(toAdd.getPkmnName());
    }

    assertArrayEquals(expectedPokemonNames, actualPokemonNames.toArray());
  }

  @Test
  public void testWithPurePokemonType() {
    String[] expectedPokemonNames = {"tangela"};

    ArrayList<Pokemon> targetedPokemon =
            Pokedex.filterPokemonByType(original151Pokedex, "grass", true);

    ArrayList<String> actualPokemonNames = new ArrayList<>();

    for (Pokemon toAdd : targetedPokemon) {
      actualPokemonNames.add(toAdd.getPkmnName());
    }

    assertArrayEquals(expectedPokemonNames, actualPokemonNames.toArray());
  }

  //Test cases for filterPokemonByMinTotalBaseStats

  @Test(expected = IllegalArgumentException.class)
  public void testStatsFilterWithNullPokedex() {
    Pokedex.filterPokemonByMinTotalBaseStats(null, 250);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStatsFilterWithNegativeTotal() {
    Pokedex.filterPokemonByMinTotalBaseStats(original151Pokedex, -1);
  }
/*
  @Test
  public void testStatsFilterIsCorrect() {
    String[] expectedPokemonNames = {"mew", "dragonite", "mewtwo"};

    ArrayList<String>
  }*/

}
