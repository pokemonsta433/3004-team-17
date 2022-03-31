package com.team17.quest.model.storyDeck;

import com.team17.quest.model.Game;
import com.team17.quest.model.Player;
import com.team17.quest.model.StoryCard;

import java.util.ArrayList;

public class PlagueEventCard extends EventCard {
    public PlagueEventCard(String n, int i) {
        super(n, i);
    }

    // Drawer loses 2 shields if possible
    public void eventStrategy(Game game) {
        ArrayList<Player> playerList = game.getPlayers();
        int currentPlayer = game.getCurrentPlayer();
        for (int i = 0; i < playerList.size(); i++) {
            if (i == currentPlayer) {
                if (playerList.get(i).getShields() > 1) {
                    playerList.get(i).setShields(playerList.get(i).getShields() - 2);
                } else if (playerList.get(i).getShields() == 1) {
                    playerList.get(i).setShields(playerList.get(i).getShields() - 1);
                }
            }
        }
    }
}
