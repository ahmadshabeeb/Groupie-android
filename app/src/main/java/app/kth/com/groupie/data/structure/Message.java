package app.kth.com.groupie.data.structure;

import java.text.DateFormat;

/**
 * Created by Ahmad on 4/11/2018.
 */

//Do we need getters and setters for everything?

public class Message {
    private String senderUserId;
    private String name;
    private String senderPicture;
    private String text;
    private String imageUrl;
    private DateFormat timeSent;

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSenderPicture() {
        return senderPicture;
    }

    public void setSenderPicture(String senderPicture) {
        this.senderPicture = senderPicture;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public DateFormat getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(DateFormat timeSent) {
        this.timeSent = timeSent;
    }
}
