package com.example;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PokedexAnalysisFunctionalityTest {

    public static Pokedex original151Pokedex;

    @BeforeClass
    public static void setUp() {

        original151Pokedex = new Pokedex("original151pokemon.json");
    }

    @Test
    public void testCorrectNumberOfPokemon() {
        int expectedNumOfGrassTypes = 14;
        int numOfPokemon = Pokedex.numOfPokemonWithSpecificType(original151Pokedex, "grass", false);
        assertEquals(expectedNumOfGrassTypes, numOfPokemon);
    }
}
