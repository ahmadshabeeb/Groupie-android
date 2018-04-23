package app.kth.com.groupie.utilities;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {

    // Converting any JAVA object to a JSON object
    public static JSONObject toJson(Object object){
        Gson gson = new Gson();
        String jsonString = gson.toJson(object);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
