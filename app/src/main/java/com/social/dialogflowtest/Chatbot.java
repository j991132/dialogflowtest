package com.social.dialogflowtest;

public class Chatbot {
    private String message;

    public Chatbot() {}

    public Chatbot(String message) {

        this.message = message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
