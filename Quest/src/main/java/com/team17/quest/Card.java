package com.team17.quest;

public abstract class Card {
    String name;
    int id;
    public Card(){
        name = "";
        id = 0;
    }
    public Card(String n, int i){
        name = n;
        id = i;
    }
    public String getName(){return name;}

    public int getId(){return id;}
}
