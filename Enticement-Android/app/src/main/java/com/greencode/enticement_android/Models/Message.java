package com.greencode.enticement_android.Models;

public class Message {

    private boolean mIsShowSentStatus = false;
    private boolean mIsShowDate = false;

    public enum MessageType {
        MESSAGE,
        STICKER,
        MESSAGE_OUT,
        MESSAGE_IN,
        STICKER_OUT,
        STICKER_IN, nextItemType, prevItemType
    }

    public enum MessageStatus {
        SENT,
        CHECKED,
        WARNING
    }

    private MessageType type;
    private MessageStatus status;
    private String message;
    private String fromId;
    private long time;

    public Message() {

    }

    public Message(String fromId, MessageType type, String message, long time) {
        this.type = type;
        this.message = message;
        this.time = time;
        this.status = MessageStatus.SENT;
        this.fromId = fromId;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public MessageStatus getStatus() {return status;}

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public long getTime() {
        return time;
    }

    public boolean getVisibilityStatus(){
        return this.mIsShowSentStatus;
    }

    public void setVisibilityStatus(boolean status){
        this.mIsShowSentStatus = status;
    }

    public boolean getVisibilityDate(){
        return this.mIsShowDate;
    }

    public void setVisibilityDate(boolean status){
        this.mIsShowDate = status;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
