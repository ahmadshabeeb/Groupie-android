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
    private List<Profile> members;
    private List<Message> messages;

    public DateFormat getTimeOfLastMsg() {
        return timeOfLastMsg;
    }

    public DateFormat getCreationDate() {
        return creationDate;
    }

    public String getLastSender() {
        return lastSender;
    }

    public List<Profile> getMembers() {
        return members;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
