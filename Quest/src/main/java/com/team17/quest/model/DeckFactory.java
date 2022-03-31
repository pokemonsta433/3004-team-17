package com.team17.quest.model;

import com.team17.quest.model.storyDeck.*;

import java.util.Stack;

public class DeckFactory {

    public DeckFactory(){}

    String[][] adventure_deck = {
            //name, weapon, power, frequency
            {"excalibur", "weapon", "30", "2"},
            {"lance", "weapon", "20", "6"},
            {"battle-ax", "weapon", "15", "8"},
            {"sword", "weapon", "10", "16"},
            {"horse", "weapon", "10", "11"},
            {"dagger", "weapon", "5", "6"},
            //name, foe, defaultpower, augmentedPower, RelevantQuests, frequency
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
            //name, ally, bp, bonusbp, bids, bonusquest, frequency
            {"king_arthur", "ally", "10", "10", "2", "", "1"},
            {"queen_guinevere", "ally", "0", "0", "3", "", "1"},
            {"sir_galahad", "ally", "15", "15", "0", "", "1"},
            {"queen_iseult", "ally", "0", "0", "2", "", "1"},
            {"sir_gawain", "ally", "10", "20", "0", "test_of_the_green_knight", "1"},
            {"sir_lancelot", "ally", "15", "25", "0", "defend_the_queens_honor", "1"},
            {"sir_percival", "ally", "5", "20", "0", "search_for_the_holy_grail", "1"},
            {"sir_tristan", "ally", "10", "10", "0", "", "1"},
            {"king_pellinore", "ally", "10", "10", "0", "", "1"},
            {"merlin", "ally", "0", "0", "0", "", "1"},
            {"amour", "8"},
            //name, test, defaultMinBid, minBidForQuestingBeast, frequency
            {"test_of_valor","test", "0", "0", "2"},
            {"test_of_temptation","test", "0", "0", "2"},
            {"test_of_morgan_le_fey","test", "3", "3", "2"},
            {"test_of_the_questing_beast","test", "0","4", "2"},
    };

    String[][] story_deck = {
            //name, stages, frequency
            {"search_for_the_holy_grail", "5", "1"},
            {"defend_the_queens_honor", "4", "1"},
            {"test_of_the_green_knight", "4", "1"},
            {"search_for_the_questing_beast", "4", "1"},
            {"rescue_the_fair_maiden", "3", "1"},
            {"journey_through_the_enchanted_forest", "3", "1"},
            {"vanquish_king_arthurs_enemies", "3", "2"},
            {"slay_the_dragon", "3", "1"},
            {"boar_hunt", "2", "2"},
            {"repel_saxon_raiders", "2", "2"},
            {"tournament","at_camelot", "3", "1"},
            {"tournament","at_orkney", "2", "1"},
            {"tournament","at_tintagel", "1", "1"},
            {"tournament","at_york", "0", "1"},
    };

    String[][] event_story_deck = {
            //name, frequency
            {"chivalrous_deed","1"},
            {"court_called_to_camelot","2"},
            {"king_s_call_to_arms","1"},
            {"king_s_recognition","2"},
            {"plague","1"},
            {"pox","1"},
            {"prosperity_throughout_the_realm", "1"},
            {"queen_s_favor", "2"}
    };

    public Stack<Card> build(String type){
        return switch (type) {
            case "Adventure" -> populateAdventure();
            case "Story" -> populateStory();
            default -> null;
        };
    }

    public Stack<Card> populateAdventure(){
        Stack<Card> deck = new Stack<>();
        int i = 0;
        for (String[] strings : adventure_deck) {
            switch (strings[1]) {
                case "weapon":
                    for (int j = 0; j <= Integer.parseInt(strings[3]); j++) {
                        deck.add(new WeaponCard(strings[0], i, Integer.parseInt(strings[2])));
                        i++;
                    }
                    break;
                case "foe":
                    for (int j = 0; j <= Integer.parseInt(strings[5]); j++) {
                        deck.add(new FoeCard(strings[0], i, Integer.parseInt(strings[2]), Integer.parseInt(strings[3]), strings[4].split(",")));
                        i++;
                    }
                    break;
                case "test":
                    for (int j = 0; j <= Integer.parseInt(strings[4]); j++) {
                        deck.add(new TestCard(strings[0], i, Integer.parseInt(strings[2]), Integer.parseInt(strings[3])));
                        i++;
                    }
                    break;
                case "ally":
                    for (int j = 0; j <= Integer.parseInt(strings[6]); j++) {
                        deck.add(new AllyCard(strings[0], i, Integer.parseInt(strings[2]), Integer.parseInt(strings[3]), Integer.parseInt(strings[4]), strings[5]));
                        i++;
                    }
                    break;
            }
            if(strings[0].equals("amour")){
                for (int j = 0; j <= Integer.parseInt(strings[1]); j++) {
                    deck.add(new AmourCard(i));
                    i++;
                }
            }

        }
        return deck;
    }

    public Stack<Card> populateStory(){
        Stack<Card> deck = new Stack<>();
        int i = 0;
        // quest cards in adventure deck
        for(String[] strings : story_deck){
            if(strings[0].equals("tournament")){
                deck.add(new TournamentCard(strings[1], i, Integer.parseInt(strings[2])));
                i++;
            }
            else{
                for(int j = 0; j <= Integer.parseInt(strings[2]); j++){
                    deck.add(new QuestCard(strings[0], i, Integer.parseInt(strings[1])));
                    i++;
                }
            }
        }
        // event cards for story deck
        // TODO uncomment when other thing is ready
        EventFactory eventFactory = new EventFactory();

        for (String[] strings : event_story_deck) {
            for (int j = 0; j <= Integer.parseInt(strings[1]); j++) {
                if (strings[0].equals("chivalrous_deed")) {
                    deck.add(eventFactory.getEventCard(strings[0], i));
                } else if (strings[0].equals("court_called_to_camelot")) {
                    deck.add(eventFactory.getEventCard(strings[0], i));
                } else if (strings[0].equals("king_s_call_to_arms")) {
                    deck.add(eventFactory.getEventCard(strings[0], i));
                } else if (strings[0].equals("king_s_recognition")) {
                    deck.add(eventFactory.getEventCard(strings[0], i));
                } else if (strings[0].equals("plague")) {
                    deck.add(eventFactory.getEventCard(strings[0], i));
                } else if (strings[0].equals("pox")) {
                    deck.add(eventFactory.getEventCard(strings[0], i));
                } else if (strings[0].equals("prosperity_throughout_the_realm")) {
                    deck.add(eventFactory.getEventCard(strings[0], i));
                } else if (strings[0].equals("queen_s_favor")) {
                    deck.add(eventFactory.getEventCard(strings[0], i));
                }
                i++;
            }
        }
        return deck;
    }
}
