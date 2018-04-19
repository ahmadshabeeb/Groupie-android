package app.kth.com.groupie.Data.Structure;

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
}
