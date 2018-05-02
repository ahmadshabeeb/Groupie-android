package app.kth.com.groupie.data;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ahmad on 4/11/2018.
 */

public class Group implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(groupId);
        dest.writeString(subject);
        dest.writeString(topic);
        dest.writeString(description);
        dest.writeMap(members);
        dest.writeInt(numberOfMembers);
        dest.writeInt(maxNumberOfMembers);
        dest.writeString(timeOfCreation);
        dest.writeString(location);
        dest.writeString(conversationId);
        dest.writeValue(isPublic); // must cast to boolean when retrieving
        dest.writeString(dateOfMeeting);
        dest.writeValue(hasMeetingDate); // must cast to boolean when retrieving
        dest.writeString(owner);
    }
    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>(){
        @Override
        public Group createFromParcel(Parcel source) {
            Group group = new Group();
            group.setGroupId(source.readString());
            group.setSubject(source.readString());
            group.setTopic(source.readString());
            group.setDescription(source.readString());
            group.setMembers(source.readHashMap(HashMap.class.getClassLoader()));
            group.setNumberOfMembers(source.readInt());
            group.setMaxNumberOfMembers(source.readInt());
            group.setTimeOfCreation(source.readString());
            group.setLocation(source.readString());
            group.setConversationId(source.readString());
            group.setIsPublic((Boolean) source.readValue(Boolean.class.getClassLoader()));
            group.setDateOfMeeting(source.readString());
            group.setHasMeetingDate((Boolean) source.readValue(Boolean.class.getClassLoader()));
            group.setOwner(source.readString());
            return group;
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public Group() {}

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

}

