package com.team17.quest.model.storyDeck;

import com.team17.quest.model.Card;
import com.team17.quest.model.Game;
import com.team17.quest.model.Player;
import com.team17.quest.model.StoryCard;

import java.util.ArrayList;
import java.util.Stack;

public class QueensFavorEventCard extends EventCard {
    public QueensFavorEventCard(String n, int i) {
        super(n, i);
    }

    // The lowest ranked players (least shields) immediately receives 2 Adventure cards
    public void eventStrategy(Game game) {
        ArrayList<Player> playerList = game.getPlayers();
        Stack<Card> adventure_deck = game.getAdventure_deck();
        int leastShields = 10000;
        // find player with the least shields
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getShields() < leastShields) {
                leastShields = playerList.get(i).getShields();
            }
        }
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getShields() == leastShields) {
                playerList.get(i).drawCard(adventure_deck, 2);
            }
        }
    }
}
