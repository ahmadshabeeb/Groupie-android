package app.kth.com.groupie.data;

import android.location.Location;

import java.text.DateFormat;
import java.util.List;

import app.kth.com.groupie.data.structure.Profile;

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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Profile> getMembers() {
        return members;
    }

    public void setMembers(List<Profile> members) {
        this.members = members;
    }

    public int getNumberOfMembers() {
        return numberOfMembers;
    }

    public void setNumberOfMembers(int numberOfMembers) {
        this.numberOfMembers = numberOfMembers;
    }

    public int getMaxNumberOfMembers() {
        return maxNumberOfMembers;
    }

    public void setMaxNumberOfMembers(int maxNumberOfMembers) {
        this.maxNumberOfMembers = maxNumberOfMembers;
    }

    public DateFormat getTimeOfCreation() {
        return timeOfCreation;
    }

    public void setTimeOfCreation(DateFormat timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public DateFormat getDateOfMeeting() {
        return dateOfMeeting;
    }

    public void setDateOfMeeting(DateFormat dateOfMeeting) {
        this.dateOfMeeting = dateOfMeeting;
    }

    public boolean isHasMeetingDate() {
        return hasMeetingDate;
    }

    public void setHasMeetingDate(boolean hasMeetingDate) {
        this.hasMeetingDate = hasMeetingDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}


