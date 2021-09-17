package com.example.chalkboard_copy;

public class ChatRecordClass {
    String sender,reciever,message,time;
    boolean isseen;

    public ChatRecordClass() {
    }

    public ChatRecordClass(String sender, String reciever, String message, String time, boolean isseen) {
        this.sender = sender;
        this.reciever = reciever;
        this.message = message;
        this.time = time;
        this.isseen = isseen;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    @Override
    public String toString() {
        return "ChatRecordClass{" +
                "sender='" + sender + '\'' +
                ", reciever='" + reciever + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
