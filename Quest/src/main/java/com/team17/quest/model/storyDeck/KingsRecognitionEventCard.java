package com.team17.quest.model.storyDeck;

import com.team17.quest.model.Game;
import com.team17.quest.model.Player;
import com.team17.quest.model.StoryCard;

import java.util.ArrayList;

public class KingsRecognitionEventCard extends EventCard {
    public KingsRecognitionEventCard(String n, int i) {
        super(n, i);
    }

    // The next player to complete a Quest will receive 2 extra shields
    public void eventStrategy(Game game) {
        ArrayList<Player> playerList = game.getPlayers();
        int nextPlayer = game.getNextPlayer();
        for (int i = 0; i < playerList.size(); i++) {
            // TODO: check if player has completed the quest i.e. all stages are complete
            System.out.println("TODO: check if player has completed the quest");
            /*if (playerList.get(nextPlayer).getStagesLeft() == 0) {
                playerList.get(nextPlayer).setShields(2 + playerList.get(i).getShields());
                break;
            }*/
            nextPlayer++;
            nextPlayer %= playerList.size();
        }
    }
}
