package com.team17.quest.model.storyDeck;
import com.team17.quest.model.StoryCard;

public class EventFactory {
    //use getEventCard method to get event card
    public EventCard getEventCard(String eventCardName, int i) {
        if (eventCardName.equalsIgnoreCase("chivalrous_deed")) {
            return new ChivalrousDeedEventCard(eventCardName, i);
        } else if (eventCardName.equalsIgnoreCase("court_called_to_camelot")) {
            return new CamelotEventCard(eventCardName, i);
        } else if (eventCardName.equalsIgnoreCase("kings_call_to_arms")) {
            return new KingsCallToArmsEventCard(eventCardName, i);
        } else if (eventCardName.equalsIgnoreCase("kings_recognition")) {
            return new KingsRecognitionEventCard(eventCardName, i);
        } else if (eventCardName.equalsIgnoreCase("plague")) {
            return new PlagueEventCard(eventCardName, i);
        } else if (eventCardName.equalsIgnoreCase("pox")) {
            return new PoxEventCard(eventCardName, i);
        } else if (eventCardName.equalsIgnoreCase("prosperity_throughout_the_realm")) {
            return new ProsperityEventCard(eventCardName, i);
        } else if (eventCardName.equalsIgnoreCase("queens_favor")) {
            return new QueensFavorEventCard(eventCardName, i);
        }
        return null;
    }
}
