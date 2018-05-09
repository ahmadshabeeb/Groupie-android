package app.kth.com.groupie.data;

import java.util.HashMap;

/**
 * Created by Ahmad on 4/11/2018.
 */

public class User {
    private HashMap<String, Boolean> groupHistory;

    public User() {}

    public HashMap<String, Boolean> getGroupHistory() {
        return groupHistory;
    }

    public void setGroupHistory(HashMap<String, Boolean> groupHistory) {
        this.groupHistory = groupHistory;
    }
}
