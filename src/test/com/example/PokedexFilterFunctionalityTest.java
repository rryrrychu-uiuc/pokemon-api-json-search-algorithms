package com.example;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PokedexFilterFunctionalityTest {

  public static Pokedex original151Dex;

  @BeforeClass
  public static void setUp() {

    original151Dex = new Pokedex("src/main/resources/original151pokemon.json");
  }

  // Test cases for filterPokemonByType

  @Test(expected = IllegalArgumentException.class)
  public void testTypeFilterWithNullPokedex() {
    Pokedex.filterPokemonByType(null, "water", false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeFilterWithNullType() {
    Pokedex.filterPokemonByType(original151Dex, null, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeFilterWithEmptyType() {
    Pokedex.filterPokemonByType(original151Dex, "", false);
  }

  @Test(expected = NoSuchElementException.class)
  public void testTypeFilterWithTypeDNE() {
    Pokedex.filterPokemonByType(original151Dex, "cats", false);
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

    Pokedex actualPokemon = Pokedex.filterPokemonByType(original151Dex, "fire", false);

    assertArrayEquals(expectedPokemonNames, Pokedex.getPokemonNames(actualPokemon).toArray());
  }

  @Test
  public void testWithPurePokemonType() {
    String[] expectedPokemonNames = {"tangela"};
    Pokedex actualPokemon = Pokedex.filterPokemonByType(original151Dex, "grass", true);

    assertArrayEquals(expectedPokemonNames, Pokedex.getPokemonNames(actualPokemon).toArray());
  }

  // Test cases for filterPokemonByMinTotalBaseStats

  @Test(expected = IllegalArgumentException.class)
  public void testStatsFilterWithNullPokedex() {
    Pokedex.filterPokemonByMinTotalBaseStats(null, 250);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStatsFilterWithNegativeTotal() {
    Pokedex.filterPokemonByMinTotalBaseStats(original151Dex, -1);
  }

  @Test
  public void testStatsFilterIsCorrect() {
    String[] expectedPokemonNames = {
      "arcanine", "articuno", "zapdos", "moltres", "dragonite", "mewtwo", "mew"
    };

    Pokedex actualPokemon = Pokedex.filterPokemonByMinTotalBaseStats(original151Dex, 550);

    assertArrayEquals(expectedPokemonNames, Pokedex.getPokemonNames(actualPokemon).toArray());
  }

  // Test cases for filterPokemonByTypeWithMove

  @Test(expected = IllegalArgumentException.class)
  public void testMoveTypeFilterWithNullPokedex() {
    Pokedex.filterPokemonByTypeWithMove(null, "grass", "water-gun");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveTypeFilterWithNullType() {
    Pokedex.filterPokemonByTypeWithMove(original151Dex, null, "water-gun");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveTypeFilterWithEmptyType() {
    Pokedex.filterPokemonByTypeWithMove(original151Dex, "", "water-gun");
  }

  @Test(expected = NoSuchElementException.class)
  public void testMoveTypeFilterWithTypeDNE() {
    Pokedex.filterPokemonByTypeWithMove(original151Dex, "cat", "water-gun");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveTypeFilterWithNullMove() {
    Pokedex.filterPokemonByTypeWithMove(original151Dex, "grass", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveTypeFilterWithEmptyMove() {
    Pokedex.filterPokemonByTypeWithMove(original151Dex, "grass", "");
  }

  @Test
  public void testMoveTypeFilterWithMoveDNE() {
    int numMoves =
        Pokedex.filterPokemonByTypeWithMove(original151Dex, "grass", "cats").pokedexSize();

    assertEquals(0, numMoves);
  }

  @Test
  public void testValidMoveTypeFilter() {
    String[] expectedPokemonNames = {
      "venusaur", "vileplume", "parasect", "victreebel", "exeggutor", "tangela"
    };

    Pokedex actualPokemon =
        Pokedex.filterPokemonByTypeWithMove(original151Dex, "grass", "hyper-beam");

    assertArrayEquals(expectedPokemonNames, Pokedex.getPokemonNames(actualPokemon).toArray());
  }

  // Test cases for filterPokemonByMinHeightAndWeight

  @Test(expected = IllegalArgumentException.class)
  public void testHeightWeightFilterWithNullPokedex() {
    Pokedex.filterPokemonByMinHeightAndWeight(null, 1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHeightWeightFilterWithNegHeight() {
    Pokedex.filterPokemonByMinHeightAndWeight(original151Dex, -1, 120);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHeightWeightFilterWithNegWeight() {
    Pokedex.filterPokemonByMinHeightAndWeight(original151Dex, 120, -1);
  }

  @Test
  public void testValidHeightWeightFilter() {
    String[] expectedPokemonNames = {"onix", "gyarados"};
    Pokedex actualPokemon = Pokedex.filterPokemonByMinHeightAndWeight(original151Dex, 50, 120);

    assertArrayEquals(expectedPokemonNames, Pokedex.getPokemonNames(actualPokemon).toArray());
  }

  // Test cases for filterPokemonMovesWithKeyword

  @Test(expected = IllegalArgumentException.class)
  public void testMoveFilterWithNullPokedex() {
    Pokedex.filterPokemonMovesWithKeyword(null, "geodude", "rock");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveFilterWithNullName() {
    Pokedex.filterPokemonMovesWithKeyword(original151Dex, null, "rock");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveFilterWithEmptyName() {
    Pokedex.filterPokemonMovesWithKeyword(original151Dex, "", "rock");
  }

  @Test(expected = NoSuchElementException.class)
  public void testMoveFilterWithNameDNE() {
    Pokedex.filterPokemonMovesWithKeyword(original151Dex, "cat", "rock");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveFilterWithNullKeyword() {
    Pokedex.filterPokemonMovesWithKeyword(original151Dex, "geodude", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveFilterWithEmptyMove() {
    Pokedex.filterPokemonMovesWithKeyword(original151Dex, "geodude", "");
  }

  @Test
  public void testMoveFilterWithKeywordDNE() {
    String[] expectedMoves = new String[0];
    ArrayList<String> actualMoves =
        Pokedex.filterPokemonMovesWithKeyword(original151Dex, "geodude", "cat");

    assertArrayEquals(expectedMoves, actualMoves.toArray());
  }

  @Test
  public void testValidMoveFilter() {
    String[] expectedMoves = {
      "rock-throw",
      "rock-slide",
      "rock-smash",
      "rock-tomb",
      "rock-blast",
      "rock-polish",
      "rock-climb",
      "stealth-rock"
    };

    ArrayList<String> actualMoves =
        Pokedex.filterPokemonMovesWithKeyword(original151Dex, "geodude", "rock");

    assertArrayEquals(expectedMoves, actualMoves.toArray());
  }
}
