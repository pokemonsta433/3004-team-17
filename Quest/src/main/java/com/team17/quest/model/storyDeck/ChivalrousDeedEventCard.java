package com.team17.quest.model.storyDeck;

import com.team17.quest.model.Player;

import java.util.ArrayList;

public class ChivalrousDeedEventCard extends EventCard {
    public ChivalrousDeedEventCard(String n, int i) {
        super(n, i);
    }

    @Override
    public void eventStrategy(ArrayList<Player> playerList) {
        // The lowest ranked players immediately receives 2 Adventure cards
        int lowestRank = 10000;
        int leastShields = 10000;
        // find player with the lowest rank and shield
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getRank() < lowestRank) {
                lowestRank = playerList.get(i).getRank();
            }
            if (playerList.get(i).getShields() < leastShields) {
                leastShields = playerList.get(i).getShields();
            }
        }
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getRank() == lowestRank &&
                    playerList.get(i).getShields() == leastShields) {
                playerList.get(i).setShields(3 + playerList.get(i).getShields());
            }
        }
    }
}