package com.example;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PokedexFunctionalityTest {

  public static Pokedex original151Pokedex;

  @BeforeClass
  public static void setUp() {

    original151Pokedex = new Pokedex("src/main/resources/original151pokemon.json");
  }

  @Test
  public void testFilterCorrectPokemonType() {

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
        Pokedex.filterPokemonWithSpecificType(original151Pokedex, "fire", false);

    ArrayList<String> actualPokemonNames = new ArrayList<>();

    for (Pokemon toAdd : targetedPokemon) {
      actualPokemonNames.add(toAdd.getPkmnName());
    }

    assertArrayEquals(expectedPokemonNames, actualPokemonNames.toArray());
  }

  @Test
  public void testCorrectNumberOfPokemon() {
    int expectedNumOfGrassTypes = 14;
    int numOfPokemon = Pokedex.numOfPokemonWithSpecificType(original151Pokedex, "grass", false);
    assertEquals(expectedNumOfGrassTypes, numOfPokemon);
  }
}
