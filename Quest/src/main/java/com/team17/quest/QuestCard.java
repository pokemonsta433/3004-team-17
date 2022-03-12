package com.team17.quest;

public class QuestCard extends StoryCard {
    int stages;
    public QuestCard(String name, int i, int s){
        super(name, i);
        stages = s;
    }
}
