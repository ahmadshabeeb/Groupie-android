package app.kth.com.groupie.data;

import com.google.firebase.database.Exclude;

import java.util.Map;

import app.kth.com.groupie.data.recycleViewData.RecyclerListItem;

/**
 * Created by Ahmad on 4/11/2018.
 */

public class Group implements RecyclerListItem {
    private String groupId;
    private String subject;
    private String topic;
    private String description;
    private Map<String, Boolean> members;
    private int numberOfMembers;
    private int maxNumberOfMembers;
    private String timeOfCreation;
    private String location;
    private String conversationId;
    private boolean isPublic;
    private String dateOfMeeting;
    private boolean hasMeetingDate;
    private String owner;
    private long meetingDateTimeStamp;

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

    public Map<String, Boolean> getMembers() {
        return members;
    }

    public void setMembers(Map<String, Boolean> members) {
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
    public String getTimeOfCreation() {
        return timeOfCreation;
    }
    public void setTimeOfCreation(String timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getConversationId() {
        return conversationId;
    }
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
    public boolean getIsPublic() {
        return isPublic;
    }
    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
    public String getDateOfMeeting() {
        return dateOfMeeting;
    }
    public void setDateOfMeeting(String dateOfMeeting) {
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


    public long getMeetingDateTimeStamp() { return meetingDateTimeStamp; }

    public void setMeetingDateTimeStamp(long meetingDateTimeStamp) { this.meetingDateTimeStamp = meetingDateTimeStamp; }

    @Exclude
    @Override
    public boolean isHeader() {
        return false;
    }
}