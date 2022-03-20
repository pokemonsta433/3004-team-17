package com.team17.quest.model.storyDeck;

// called by Game
// strategy pattern: one strategy per different type of card

import com.team17.quest.model.Player;
import com.team17.quest.model.StoryCard;

import java.util.ArrayList;

public class EventCard extends StoryCard {
    public EventCard(String n, int i) {
        super(n, i);
    }
    public void eventStrategy(ArrayList<Player> playerList) {
    }
}
