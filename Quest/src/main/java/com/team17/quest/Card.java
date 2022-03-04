package com.team17.quest;

public abstract class Card {
    String name;
    public Card(){name = "";}
    public Card(String n){
        name = n;
    }
    public String getName(){return name;}
}
