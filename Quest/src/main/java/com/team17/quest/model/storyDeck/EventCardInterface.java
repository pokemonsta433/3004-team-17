package com.team17.quest.model.storyDeck;

// called by Game
// strategy pattern: one strategy per different type of card

import com.team17.quest.model.Game;

public interface EventCardInterface {
    public void eventStrategy(Game game);
}
