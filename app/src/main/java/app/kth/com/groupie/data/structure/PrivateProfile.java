package app.kth.com.groupie.data.structure;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PrivateProfile extends Profile implements Serializable {
    private String favoriteSubject;
    private String studyLocation;


    public String getFavoriteSubject() {
        return favoriteSubject;
    }

    public void setFavoriteSubject(String favoriteSubject) {
        this.favoriteSubject = favoriteSubject;
    }

    public String getStudyLocation() {
        return studyLocation;
    }

    public void setStudyLocation(String studyLocation) {
        this.studyLocation = studyLocation;
    }

}
