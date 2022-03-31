package com.team17.quest.model.storyDeck;

import com.team17.quest.model.Card;
import com.team17.quest.model.Game;
import com.team17.quest.model.Player;
import com.team17.quest.model.StoryCard;

import java.util.ArrayList;
import java.util.Stack;

public class ProsperityEventCard extends EventCard {
    public ProsperityEventCard(String n, int i) {
        super(n, i);
    }

    // All players may immediately draw 2 Adventure cards
    public void eventStrategy(Game game) {
        ArrayList<Player> playerList = game.getPlayers();
        Stack<Card> adventure_deck = game.getAdventure_deck();
        for (int i = 0; i < playerList.size(); i++) {
            System.out.println("Handle ProsperityEventCard. draw 2 cards: for player" + playerList.get(i).getName());

            playerList.get(i).drawCard(adventure_deck, 2);
        }
    }
}
