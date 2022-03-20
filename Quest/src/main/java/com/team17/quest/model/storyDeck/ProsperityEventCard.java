package com.team17.quest.model.storyDeck;

import com.team17.quest.model.Card;
import com.team17.quest.model.Player;

import java.util.ArrayList;
import java.util.Stack;

public class ProsperityEventCard extends EventCard {
    public ProsperityEventCard(String n, int i) {
        super(n, i);
    }

    // All players may immediately draw 2 Adventure cards
    public void eventStrategy(ArrayList<Player> playerList, Stack<Card> adventure_deck) {
        for (int i = 0; i < playerList.size(); i++) {
            playerList.get(i).drawCard(adventure_deck, 2);
        }
    }
}
