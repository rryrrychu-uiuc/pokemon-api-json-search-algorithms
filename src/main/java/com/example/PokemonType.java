package com.example;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class PokemonType {

    @JsonProperty("name")
    private String pkmnType;

    @JsonProperty("url")
    private String typeStatsURL;

    public String getPkmnType() {
        return pkmnType;
    }

    public String getTypeStatsURL() {
        return typeStatsURL;
    }

    public void deserializeTypeRelations() {

    }

    public class TypeRelations {
        ArrayList<String> doubleDamageTaken;
        ArrayList<String> doubleDamageDealt;
        ArrayList<String> halfDamageTaken;
        ArrayList<String> halfDamageDealt;
        ArrayList<String> noDamageTaken;
        ArrayList<String> noDamageDealt;

        public TypeRelations() {
            doubleDamageTaken = new ArrayList<>();
            doubleDamageDealt = new ArrayList<>();
            halfDamageTaken = new ArrayList<>();
            halfDamageDealt = new ArrayList<>();
            noDamageTaken = new ArrayList<>();
            noDamageDealt = new ArrayList<>();
        }
    }
}
