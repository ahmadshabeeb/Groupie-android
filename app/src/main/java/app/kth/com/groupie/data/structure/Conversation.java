package app.kth.com.groupie.data.structure;

import android.text.format.DateFormat;

import java.util.List;

/**
 * Created by Ahmad on 4/11/2018.
 */

public class Conversation {
    private DateFormat timeOfLastMsg;
    private DateFormat creationDate;
    private String lastSender;
    private List<String> members;
    private List<Message> messages;

    public DateFormat getTimeOfLastMsg() {
        return timeOfLastMsg;
    }

    public void setTimeOfLastMsg(DateFormat timeOfLastMsg) {
        this.timeOfLastMsg = timeOfLastMsg;
    }

    public DateFormat getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateFormat creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastSender() {
        return lastSender;
    }

    public void setLastSender(String lastSender) {
        this.lastSender = lastSender;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
