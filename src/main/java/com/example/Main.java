package com.example;

public class Main {
    public static void main(String[] args){
        Pokedex original151Dex = new Pokedex("src/main/resources/original151pokemon.json");
        System.out.println(original151Dex.numOfPokemonWithSpecificType("fire", false));
    }
}