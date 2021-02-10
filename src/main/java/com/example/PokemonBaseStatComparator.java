package com.example;

import java.util.Comparator;

/** Compares pokemon based on the value of passed statName for a Pokemon */
public class PokemonBaseStatComparator implements Comparator<Pokemon> {

  private final String statToCompare;

  public PokemonBaseStatComparator(String statName) {
    statToCompare = statName;
  }

  @Override
  public int compare(Pokemon pkmn1, Pokemon pkmn2) {

    int pkmn1BaseStat = pkmn1.getBaseStats().get(statToCompare);
    int pkmn2BaseStat = pkmn2.getBaseStats().get(statToCompare);

    return Integer.compare(pkmn1BaseStat, pkmn2BaseStat);
  }
}
