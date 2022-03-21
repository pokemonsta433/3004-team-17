package com.team17.quest.model.storyDeck;

import com.team17.quest.model.Card;
import com.team17.quest.model.Game;
import com.team17.quest.model.Player;
import com.team17.quest.model.StoryCard;

import java.util.ArrayList;

public class KingsCallToArmsEventCard extends StoryCard {
    public KingsCallToArmsEventCard(String n, int i) {
        super(n, i);
    }

    // The highest ranked players must place 1 weapon in the discard pile, if unable to do so, Foe cards must be discarded
    public void eventStrategy(Game game) {
        ArrayList<Player> playerList = game.getPlayers();
        ArrayList<Card> adventure_discardPile = game.getAdventure_discardPile();
        int highestRank = 10000;
        // find player with the highest rank
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getRank() > highestRank) {
                highestRank = playerList.get(i).getRank();
            }
        }
        boolean weaponCardFlag;
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getRank() == highestRank) {
                weaponCardFlag = false;
                for (int j = 0; j < playerList.get(i).getHand().size(); j++) {
                    if (playerList.get(i).getHand().get(j).isWeapon()) {
                        adventure_discardPile.add(playerList.get(i).getHand().get(j));
                        playerList.get(i).getHand().remove(j);
                        weaponCardFlag = true;
                        break;
                    }
                }
                // no weapons found; Foe cards must be discarded
                if (!weaponCardFlag) {
                    for (int j = playerList.get(i).getHand().size() - 1; j >= 0; j--)  {
                        if (playerList.get(i).getHand().get(j).isFoe()) {
                            adventure_discardPile.add(playerList.get(i).getHand().get(j));
                            playerList.get(i).getHand().remove(j);
                        }
                    }
                }
            }
        }
    }
}
