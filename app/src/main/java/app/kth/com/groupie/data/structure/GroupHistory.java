package app.kth.com.groupie.data.structure;

import java.util.HashMap;

/**
 * Created by Ahmad on 4/12/2018.
 */

public class GroupHistory {
    private HashMap<String, Boolean> groups;

    public GroupHistory() {}

    public HashMap<String, Boolean> getGroups() {
        return groups;
    }

    public void setGroups(HashMap<String, Boolean> groups) {
        this.groups = groups;
    }
}
