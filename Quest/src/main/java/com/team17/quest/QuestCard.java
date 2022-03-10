package com.team17.quest;

public class QuestCard extends Card {
    int stages;
    public QuestCard(String name, int i, int s){
        super(name, i);
        stages = s;
    }
}
