package com.greencode.enticement_android.Models;

/**
 * Created by jasonnguyen on 11/28/16.
 */

public class Message {

    public enum MessageType {
        MESSAGE_IN,
        STICKER_IN,
        MESSAGE_OUT,
        nextItemType, prevItemType, STICKER_OUT
    }

    private MessageType type;
    private String message;
    private long time;

    public Message(MessageType type, String message, long time) {
        this.type = type;
        this.message = message;
        this.time = time;
    }

    public MessageType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public long getTime() {
        return time;
    }
}
