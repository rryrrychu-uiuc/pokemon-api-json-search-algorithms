package com.example;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class PokedexAnalysisFunctionalityTest {

  public static Pokedex original151Dex;

  @BeforeClass
  public static void setUp() {

    original151Dex = new Pokedex("original151pokemon.json");
  }

  // Test cases for searchForPokemonByName

  @Test(expected = IllegalArgumentException.class)
  public void testNameSearchWithNullPokedex() {
    Pokedex.searchForPokemonByName(null, "bulbasaur");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNameSearchWithNullName() {
    Pokedex.searchForPokemonByName(original151Dex, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNameSearchWithEmptyName() {
    Pokedex.searchForPokemonByName(original151Dex, "");
  }

  @Test
  public void testNameSearchWhenNameDNE() {

    Pokemon actualPokemon = Pokedex.searchForPokemonByName(original151Dex, "cats");
    assertNull(actualPokemon);
  }

  @Test
  public void testValidNameSearch() {

    Pokemon actualPokemon = Pokedex.searchForPokemonByName(original151Dex, "bulbasaur");
    assertEquals("bulbasaur", actualPokemon.getPkmnName());
  }

  // Test cases for numOfPokemonWithSpecificType

  @Test(expected = IllegalArgumentException.class)
  public void testAnalyzeTypeWithNullPokedex() {
    Pokedex.numOfPokemonWithSpecificType(null, "water", false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnalyzeTypeWithNullType() {
    Pokedex.numOfPokemonWithSpecificType(original151Dex, null, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnalyzeTypeEmptyType() {
    Pokedex.numOfPokemonWithSpecificType(original151Dex, "", false);
  }

  @Test(expected = NoSuchElementException.class)
  public void testAnalyzeTypeWithTypeDNE() {
    Pokedex.numOfPokemonWithSpecificType(original151Dex, "cats", false);
  }

  @Test
  public void testValidNumberOfTypedPokemon() {
    int expectedNumOfType = 14;
    int actualNumOfType = Pokedex.numOfPokemonWithSpecificType(original151Dex, "grass", false);
    assertEquals(expectedNumOfType, actualNumOfType);
  }

  @Test
  public void testValidNumberOfUniqueTypedPokemon() {
    int expectedNumOfUnique = 6;
    int actualNumOfUnique = Pokedex.numOfPokemonWithSpecificType(original151Dex, "electric", true);
    assertEquals(expectedNumOfUnique, actualNumOfUnique);
  }

  // Test cases for averageBaseStatValueOfSpecificType

  @Test(expected = IllegalArgumentException.class)
  public void testAnalyzeAvgStatWithNullPokedex() {
    Pokedex.averageBaseStatValueOfSpecificType(null, "water", "attack");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnalyzeAvgStatWithNullType() {
    Pokedex.averageBaseStatValueOfSpecificType(original151Dex, null, "attack");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnalyzeAvgStatWithEmptyType() {
    Pokedex.averageBaseStatValueOfSpecificType(original151Dex, "", "attack");
  }

  @Test(expected = NoSuchElementException.class)
  public void testAnalyzeAvgStatWithTypeDNE() {
    Pokedex.averageBaseStatValueOfSpecificType(original151Dex, "cats", "attack");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnalyzeAvgStatWithNullStat() {
    Pokedex.averageBaseStatValueOfSpecificType(original151Dex, "water", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnalyzeAvgStatWithEmptyStat() {
    Pokedex.averageBaseStatValueOfSpecificType(original151Dex, "water", "");
  }

  @Test(expected = NoSuchElementException.class)
  public void testAnalyzeAvgStatWithStatDNE() {
    Pokedex.averageBaseStatValueOfSpecificType(original151Dex, "water", "cats");
  }

  @Test
  public void testValidAvgStat() {
    Double expectedAverage = 70.6875;
    Double actualAverage =
        Pokedex.averageBaseStatValueOfSpecificType(original151Dex, "water", "attack");

    assertEquals(expectedAverage, actualAverage);
  }

  // Test cases for sortPokemonByStatGivenMove

  @Test(expected = IllegalArgumentException.class)
  public void testSortWithNullPokedex() {
    Pokedex.sortPokemonByStatGivenMove(null, "speed", "transform", false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSortWithNullStat() {
    Pokedex.sortPokemonByStatGivenMove(original151Dex, null, "transform", false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSortWithEmptyStat() {
    Pokedex.sortPokemonByStatGivenMove(original151Dex, "", "transform", false);
  }

  @Test(expected = NoSuchElementException.class)
  public void testSortWithStatDNE() {
    Pokedex.sortPokemonByStatGivenMove(original151Dex, "cats", "transform", false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSortWithNullMove() {
    Pokedex.sortPokemonByStatGivenMove(original151Dex, "speed", null, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSortWithEmptyMove() {
    Pokedex.sortPokemonByStatGivenMove(original151Dex, "speed", "", false);
  }

  @Test
  public void testSortWithMoveDNE() {
    String[] expectedPkmn = new String[0];
    Pokedex actualPkmn = Pokedex.sortPokemonByStatGivenMove(original151Dex, "speed", "cats", false);
    assertArrayEquals(expectedPkmn, Pokedex.getPokemonNames(actualPkmn).toArray());
  }

  @Test
  public void testValidSortWithMove() {
    String[] expectedPkmn = {"mew", "ditto"};
    Pokedex actualPkmn =
        Pokedex.sortPokemonByStatGivenMove(original151Dex, "speed", "transform", true);
    assertArrayEquals(expectedPkmn, Pokedex.getPokemonNames(actualPkmn).toArray());
  }

  @Test
  public void testValidSortWithMoveReverse() {
    String[] expectedPkmn = {"ditto", "mew"};
    Pokedex actualPkmn =
        Pokedex.sortPokemonByStatGivenMove(original151Dex, "speed", "transform", false);
    assertArrayEquals(expectedPkmn, Pokedex.getPokemonNames(actualPkmn).toArray());
  }

  // Test cases for numOfSpecificTypeOfAttacker

  @Test(expected = IllegalArgumentException.class)
  public void testAnalyzeAttackersNullPokedex() {
    Pokedex.numOfSpecificTypeOfAttacker(null, false);
  }

  @Test
  public void testValidNumOfSpecialAttackers() {
    int expectedNum = 68;
    int actualNum = Pokedex.numOfSpecificTypeOfAttacker(original151Dex, true);
    assertEquals(expectedNum, actualNum);
  }

  @Test
  public void testValidNumOfAttackers() {
    int expectedNum = 93;
    int actualNum = Pokedex.numOfSpecificTypeOfAttacker(original151Dex, false);
    assertEquals(expectedNum, actualNum);
  }

  // Test cases for getPokemonNames

  @Test(expected = IllegalArgumentException.class)
  public void testGetNamesNullPokedex() {
    Pokedex.getPokemonNames(null);
  }

  @Test
  public void testValidGetNames() {
    String[] expectedPokemon = {"ditto", "mew"};
    Pokedex actualPokemon = Pokedex.filterPokemonByMove(original151Dex, "transform");

    assertArrayEquals(expectedPokemon, Pokedex.getPokemonNames(actualPokemon).toArray());
  }
}