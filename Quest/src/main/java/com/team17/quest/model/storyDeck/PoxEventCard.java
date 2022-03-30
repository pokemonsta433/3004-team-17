package com.team17.quest.model.storyDeck;

import com.team17.quest.model.Game;
import com.team17.quest.model.Player;
import com.team17.quest.model.StoryCard;

import java.util.ArrayList;

public class PoxEventCard extends StoryCard {
    public PoxEventCard(String n, int i) {
        super(n, i);
    }

    // All players except the player drawing this card loses 1 shield
    public void eventStrategy(Game game) {
        ArrayList<Player> playerList = game.getPlayers();
        int currentPlayer = game.getCurrentPlayer();
        for (int i = 0; i < playerList.size(); i++) {
            if (i != currentPlayer) {
                if (playerList.get(i).getShields() > 0) {
                    playerList.get(i).setShields(playerList.get(i).getShields() - 1);
                }
            }
        }
    }
}
