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

    original151Dex = new Pokedex("original151pokemon.json");
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
  public void testTypeFilterWhenTypeDNE() {
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

  // Test cases for filterPokemonByMove

  @Test(expected = IllegalArgumentException.class)
  public void testMoveFilterWithNullPokedex() {
    Pokedex.filterPokemonByMove(null, "transform");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveFilterWithNullMove() {
    Pokedex.filterPokemonByMove(original151Dex, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveFilterWithEmptyMove() {
    Pokedex.filterPokemonByMove(original151Dex, "");
  }

  @Test
  public void testMoveFilterWithMoveDNE() {
    assertEquals(0, Pokedex.filterPokemonByMove(original151Dex, "cats").pokedexSize());
  }

  @Test
  public void testValidMoveFilter() {
    String[] expectedPokemon = {"ditto", "mew"};
    Pokedex actualPokemon = Pokedex.filterPokemonByMove(original151Dex, "transform");

    assertArrayEquals(expectedPokemon, Pokedex.getPokemonNames(actualPokemon).toArray());
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

  // Test cases for filterPokemonByTypeAndMove

  @Test(expected = IllegalArgumentException.class)
  public void testMoveTypeFilterWithNullPokedex() {
    Pokedex.filterPokemonByTypeAndMove(null, "grass", "water-gun");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveTypeFilterWithNullType() {
    Pokedex.filterPokemonByTypeAndMove(original151Dex, null, "water-gun");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveTypeFilterWithEmptyType() {
    Pokedex.filterPokemonByTypeAndMove(original151Dex, "", "water-gun");
  }

  @Test(expected = NoSuchElementException.class)
  public void testMoveTypeFilterWhenTypeDNE() {
    Pokedex.filterPokemonByTypeAndMove(original151Dex, "cat", "water-gun");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveTypeFilterWithNullMove() {
    Pokedex.filterPokemonByTypeAndMove(original151Dex, "grass", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveTypeFilterWithEmptyMove() {
    Pokedex.filterPokemonByTypeAndMove(original151Dex, "grass", "");
  }

  @Test
  public void testMoveTypeFilterWhenMoveDNE() {
    int numMoves =
        Pokedex.filterPokemonByTypeAndMove(original151Dex, "grass", "cats").pokedexSize();

    assertEquals(0, numMoves);
  }

  @Test
  public void testValidMoveTypeFilter() {
    String[] expectedPokemonNames = {
      "venusaur", "vileplume", "parasect", "victreebel", "exeggutor", "tangela"
    };

    Pokedex actualPokemon =
        Pokedex.filterPokemonByTypeAndMove(original151Dex, "grass", "hyper-beam");

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
  public void testKeywordFilterWithNullPokedex() {
    Pokedex.filterPokemonMovesWithKeyword(null, "geodude", "rock");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testKeywordFilterWithNullName() {
    Pokedex.filterPokemonMovesWithKeyword(original151Dex, null, "rock");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testKeywordFilterWithEmptyName() {
    Pokedex.filterPokemonMovesWithKeyword(original151Dex, "", "rock");
  }

  @Test(expected = NoSuchElementException.class)
  public void testKeywordFilterWhenNameDNE() {
    Pokedex.filterPokemonMovesWithKeyword(original151Dex, "cat", "rock");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testKeywordFilterWithNullKeyword() {
    Pokedex.filterPokemonMovesWithKeyword(original151Dex, "geodude", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testKeywordFilterWithEmptyMove() {
    Pokedex.filterPokemonMovesWithKeyword(original151Dex, "geodude", "");
  }

  @Test
  public void testMoveFilterWhenKeywordDNE() {
    String[] expectedMoves = new String[0];
    ArrayList<String> actualMoves =
        Pokedex.filterPokemonMovesWithKeyword(original151Dex, "geodude", "cat");

    assertArrayEquals(expectedMoves, actualMoves.toArray());
  }

  @Test
  public void testValidKeywordFilter() {
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