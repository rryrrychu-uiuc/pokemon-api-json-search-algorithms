package com.example;

public class Main {
    public static void main(String[] args){
        Pokedex original151Pokedex = new Pokedex("src/main/resources/original151pokemon.json");
        original151Pokedex.printAllPokemon();
    }
}