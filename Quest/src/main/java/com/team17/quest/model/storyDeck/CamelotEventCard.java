package com.team17.quest.model.storyDeck;

import com.team17.quest.model.Card;
import com.team17.quest.model.Game;
import com.team17.quest.model.Player;
import com.team17.quest.model.StoryCard;

import java.util.ArrayList;

public class CamelotEventCard extends EventCard {
    public CamelotEventCard(String n, int i) {
        super(n, i);
    }

    // All Allies in play must be discarded
    public void eventStrategy(Game game) {
        ArrayList<Player> playerList = game.getPlayers();
        ArrayList<Card> adventure_discardPile = game.getAdventure_discardPile();
        for (int i = 0; i < playerList.size(); i++) {
            for (int j = playerList.get(i).getAllies().size() - 1; j >= 0; j--) {
                adventure_discardPile.add(playerList.get(i).getAllies().get(j));
                playerList.get(i).getAllies().remove(j);
            }
        }
    }
}