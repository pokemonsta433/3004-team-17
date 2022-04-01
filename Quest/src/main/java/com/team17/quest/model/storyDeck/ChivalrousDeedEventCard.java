package com.team17.quest.model.storyDeck;

import com.team17.quest.model.Game;
import com.team17.quest.model.Player;
import com.team17.quest.model.StoryCard;

import java.util.ArrayList;

public class ChivalrousDeedEventCard extends EventCard {

    public ChivalrousDeedEventCard(String n, int i) {
        super(n, i);
    }

    public void eventStrategy(Game game) {
        ArrayList<Player> playerList = game.getPlayers();
        // The lowest ranked (least shields) players immediately receives 2 Adventure cards
        int leastShields = 10000;
        // find player with the lowest rank and shield
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getShields() < leastShields) {
                leastShields = playerList.get(i).getShields();
            }
        }
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getShields() == leastShields) {
                playerList.get(i).setShields(3 + playerList.get(i).getShields());
            }
        }
    }
}
