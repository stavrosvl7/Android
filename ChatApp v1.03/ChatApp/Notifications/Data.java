package com.unipi.stavrosvl7.ChatApp.Notifications;

public class Data {

    private String user;
    private String messageBody;
    private String title;
    private String sented;

    public Data(String user, String messageBody, String title, String sented) {
        this.user = user;
        this.messageBody = messageBody;
        this.title = title;
        this.sented = sented;
    }

    public Data() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
