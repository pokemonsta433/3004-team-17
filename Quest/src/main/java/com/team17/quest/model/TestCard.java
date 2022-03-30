package com.team17.quest.model;

public class TestCard extends AdventureCard {

    int minimumBid;
    int augmentedMinBid;
    String name;

    public TestCard(String name, int i, int minBid, int bonus){
        super(name, i, 0);
        minimumBid = minBid;
        augmentedMinBid = bonus;
    }
}