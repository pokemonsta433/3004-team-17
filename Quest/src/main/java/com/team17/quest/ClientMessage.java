package com.team17.quest;

public class ClientMessage { //all messages send with a name and a message

    private String name;

    private String msg;

    public ClientMessage(String name, String msg) {
        this.name = name;
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   public String getMsg() {
        return msg;
   }

   public void setMsg(){
        this.msg = msg;
   }
}