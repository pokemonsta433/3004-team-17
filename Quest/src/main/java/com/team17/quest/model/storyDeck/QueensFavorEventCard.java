package com.team17.quest.model.storyDeck;

import com.team17.quest.model.Card;
import com.team17.quest.model.Game;
import com.team17.quest.model.Player;
import com.team17.quest.model.StoryCard;

import java.util.ArrayList;
import java.util.Stack;

public class QueensFavorEventCard extends StoryCard {
    public QueensFavorEventCard(String n, int i) {
        super(n, i);
    }

    // Players with both lowest rank and least amount of shields, receives 3 shields
    public void eventStrategy(Game game) {
        ArrayList<Player> playerList = game.getPlayers();
        Stack<Card> adventure_deck = game.getAdventure_deck();
        int lowestRank = 10000;
        // find player with the lowest rank
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getRank() < lowestRank) {
                lowestRank = playerList.get(i).getRank();
            }
        }
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getRank() == lowestRank) {
                playerList.get(i).drawCard(adventure_deck, 2);
            }
        }
    }
}
