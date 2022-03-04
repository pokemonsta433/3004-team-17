package com.team17.quest;

import java.util.ArrayList;
import java.util.Stack;

public class DeckFactory {

    public DeckFactory(){}

    String adventure_deck[][] = {
            {"excalibur", "weapon", "30", "2"},
            {"lance", "weapon", "20", "6"},
            {"battle-ax", "weapon", "15", "8"},
            {"sword", "weapon", "10", "16"},
            {"horse", "weapon", "10", "11"},
            {"dagger", "weapon", "5", "6"},
            {"dragon", "foe", "50", "70", "", "1"},
            {"giant", "foe", "40", "40", "", "2"},
            {"mordred", "foe", "30", "30", "", "4"},
            {"green_knight", "foe", "25", "40", "", "2"},
            {"black_knight", "foe", "25", "35", "", "3"},
            {"evil_knight", "foe", "20", "30", "", "6"},
            {"saxon_knight", "foe", "15", "25", "", "8"},
            {"robber_knight", "foe", "15", "15", "", "7"},
            {"saxons", "foe", "10", "20", "", "5"},
            {"boar", "foe", "5", "15", "", "4"},
            {"thieves", "foe", "5", "5", "", "8"},
    };

    public Stack<Card> build(String type){
        switch(type){
            case "Adventure":
                return populateAdventure();
        }
        return null;
    }

    public Stack<Card> populateAdventure(){
        Stack<Card> deck = new Stack<>();
        for(int i = 0; i < adventure_deck.length; i++){
            if(adventure_deck[i][1] == "weapon"){
                for(int j = 0; j <= Integer.parseInt(adventure_deck[i][3]); j++){
                    deck.add(new WeaponCard(adventure_deck[i][0], Integer.parseInt(adventure_deck[i][2])));
                }
            }
            else if(adventure_deck[i][1] == "foe"){
                for(int j = 0; j <= Integer.parseInt(adventure_deck[i][5]); j++){
                    deck.add(new FoeCard(adventure_deck[i][0],Integer.parseInt(adventure_deck[i][2]), Integer.parseInt(adventure_deck[i][3]), adventure_deck[i][4]));
                }
            }
        }
        return deck;
    }
}
