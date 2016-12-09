package com.greencode.enticement_android.Models;

public class Message {

    private boolean isChecked = false;
    private boolean isWarning = false;
    private boolean mIsShowSentStatus = false;
    private boolean mIsShowDate = false;

    public enum MessageType {
        MESSAGE_OUT,
        MESSAGE_IN,
        STICKER_OUT,
        STICKER_IN, nextItemType, prevItemType
    }

    private MessageType type;
    private String message;
    private long time;

    public Message() {

    }

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
}
