package com.team17.quest.model;

import java.util.Stack;

public class DeckFactory {

    public DeckFactory(){}

    String[][] adventure_deck = {
            {"excalibur", "weapon", "30", "2"},
            {"lance", "weapon", "20", "6"},
            {"battle-ax", "weapon", "15", "8"},
            {"sword", "weapon", "10", "16"},
            {"horse", "weapon", "10", "11"},
            {"dagger", "weapon", "5", "6"},
            {"dragon", "foe", "50", "70", "defend_the_queens_honor,search_for_the_holy_grail,slay_the_dragon", "1"},
            {"giant", "foe", "40", "40", "defend_the_queens_honor,search_for_the_holy_grail", "2"},
            {"mordred", "foe", "30", "30", "defend_the_queens_honor,search_for_the_holy_grail", "4"},
            {"green_knight", "foe", "25", "40", "defend_the_queens_honor,search_for_the_holy_grail,test_of_the_green_knight", "2"},
            {"black_knight", "foe", "25", "35", "defend_the_queens_honor,rescue_the_fair_maiden,search_for_the_holy_grail", "3"},
            {"evil_knight", "foe", "20", "30", "defend_the_queens_honor,journey_through_the_enchanted_forest,search_for_the_holy_grail", "6"},
            {"saxon_knight", "foe", "15", "25", "defend_the_queens_honor,repel_saxon_raiders,search_for_the_holy_grail", "8"},
            {"robber_knight", "foe", "15", "15", "defend_the_queens_honor,search_for_the_holy_grail", "7"},
            {"saxons", "foe", "10", "20", "defend_the_queens_honor,repel_saxon_raiders,search_for_the_holy_grail", "5"},
            {"boar", "foe", "5", "15", "boar_hunt,defend_the_queens_honor,search_for_the_holy_grail", "4"},
            {"thieves", "foe", "5", "5", "defend_the_queens_honor,search_for_the_holy_grail", "8"},
    };

    String[][] story_deck = {
            {"search_for_the_holy_grail", "5", "1"},
            {"defend_the_queens_honor", "4", "1"},
            {"test_of_the_green_knight", "4", "1"},
            {"search_for_the_questing_beast", "4", "1"},
            {"rescue_the_fair_maiden", "3", "1"},
            {"journey_through_the_enchanted_forest", "3", "1"},
            {"vanquish_king_arthurs_enemies", "3", "2"},
            {"slay_the_dragon", "3", "1"},
            {"boar_hunt", "2", "2"},
            {"repel_saxon_raiders", "2", "2"}
    };

    public Stack<Card> build(String type){
        switch(type){
            case "Adventure":
                return populateAdventure();
            case "Story":
                return populateStory();
        }
        return null;
    }

    public Stack<Card> populateAdventure(){
        Stack<Card> deck = new Stack<>();
        int i = 0;
        for (String[] strings : adventure_deck) {
            if (strings[1].equals("weapon")) {
                for (int j = 0; j <= Integer.parseInt(strings[3]); j++) {
                    deck.add(new WeaponCard(strings[0], i, Integer.parseInt(strings[2])));
                    i++;
                }
            } else if (strings[1].equals("foe")) {
                for (int j = 0; j <= Integer.parseInt(strings[5]); j++) {
                    deck.add(new FoeCard(strings[0], i, Integer.parseInt(strings[2]), Integer.parseInt(strings[3]), strings[4].split(",")));
                    i++;
                }
            }
        }
        return deck;
    }

    public Stack<Card> populateStory(){
        Stack<Card> deck = new Stack<>();
        int i = 0;
        for(String[] strings : story_deck){
            for(int j = 0; j <= Integer.parseInt(strings[2]); j++){
                deck.add(new QuestCard(strings[0], i, Integer.parseInt(strings[1])));
            }
        }
        return deck;
    }
}