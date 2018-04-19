package app.kth.com.groupie.data;

import java.util.List;

import app.kth.com.groupie.data.structure.GroupHistory;

/**
 * Created by Ahmad on 4/11/2018.
 */

public class User {
    private String userId;
    private List<GroupHistory> history;

    public String getUserId() {
        return userId;
    }

    public List<GroupHistory> getHistory() {
        return history;
    }
}
