package com.greencode.enticement_android.Models;

import java.io.Serializable;

/**
 * Created by Hung Nguyen on 12/23/2016.
 */

public class ChatRoom implements Serializable {
    private String id;
    private String name;
    private String lastMessage;
    private String timestamp;
    private Users.User user;

    public ChatRoom() {}

    public ChatRoom(String id, String name, String lastMessage, String timestamp, Users.User user) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.user = user;
    }

    public ChatRoom(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Users.User getUser() {
        return user;
    }

    public void setUser(Users.User user) {
        this.user = user;
    }
}
