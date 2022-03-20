package com.team17.quest.model.storyDeck;

import com.team17.quest.model.Player;

import java.util.ArrayList;

public class PoxEventCard extends EventCard {
    public PoxEventCard(String n, int i) {
        super(n, i);
    }

    // All players except the player drawing this card loses 1 shield
    public void eventStrategy(ArrayList<Player> playerList, int currentPlayer) {
        for (int i = 0; i < playerList.size(); i++) {
            if (i != currentPlayer) {
                if (playerList.get(i).getShields() > 0) {
                    playerList.get(i).setShields(playerList.get(i).getShields() - 1);
                }
            }
        }
    }
}
