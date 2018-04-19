package app.kth.com.groupie.Data;

import android.location.Location;

import java.text.DateFormat;
import java.util.List;

import app.kth.com.groupie.Data.Structure.Profile;

/**
 * Created by Ahmad on 4/11/2018.
 */

public class Group {
    private String groupId;
    private String subject;
    private String topic;
    private String description;
    private List<Profile>members;
    private int numberOfMembers;
    private int maxNumberOfMembers;
    private DateFormat timeOfCreation;
    private Location location;
    private String conversationId;
    private boolean isPublic;
    private DateFormat dateOfMeeting;
    private boolean hasMeetingDate;
    private String owner;

}
