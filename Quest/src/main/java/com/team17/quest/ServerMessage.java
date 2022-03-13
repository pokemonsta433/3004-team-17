package com.team17.quest;

public class ServerMessage {

    private String messagetype;
    private String content;

    public ServerMessage() {
    }

    public ServerMessage(String messagetype, String content) {
        this.messagetype = messagetype;
        this.content = content;
    }

    public String getContent() {
        return content;
    }
    public String getMessagetype() {
        return messagetype;
    }

}