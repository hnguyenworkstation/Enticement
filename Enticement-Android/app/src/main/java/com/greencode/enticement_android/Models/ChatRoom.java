package com.greencode.enticement_android.Models;

import java.io.Serializable;

/**
 * Created by Hung Nguyen on 12/23/2016.
 */

public class ChatRoom implements Serializable {
    private String id;
    private String name;
    private String lastMessage;
    private String lastMessageFrom;
    private String timestamp;
    private UserProfile user;
    private int unreadCount;

    private String userID1;
    private String userID2;

    public ChatRoom() {}

    public ChatRoom(String id, String name, String lastMessage, String lastMessageFrom, String timestamp, UserProfile user) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.user = user;
        this.lastMessageFrom = lastMessageFrom;
    }

    /*This constrcutor is used to create new chatroom*/
    public ChatRoom(String id, String userID1, String userID2, String lastMessage, String lastMessageFrom, String timestamp) {
        this.id = id;
        this.userID1 = userID1;
        this.userID2 = userID2;
        this.lastMessage = lastMessage;
        this.lastMessageFrom = lastMessageFrom;
        this.timestamp = timestamp;
    }

    public String getLastMessageFrom() {
        return lastMessageFrom;
    }

    public void setLastMessageFrom(String lastMessageFrom) {
        this.lastMessageFrom = lastMessageFrom;
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

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getUserID1() {
        return userID1;
    }

    public void setUserID1(String userID1) {
        this.userID1 = userID1;
    }

    public String getUserID2() {
        return userID2;
    }

    public void setUserID2(String userID2) {
        this.userID2 = userID2;
    }
}
