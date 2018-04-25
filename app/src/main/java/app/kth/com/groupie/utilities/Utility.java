package app.kth.com.groupie.utilities;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
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

    public static Task<String> callCloudFunctions(String functionName, Object data) {
        FirebaseFunctions mFunction = FirebaseFunctions.getInstance();

        return mFunction
                .getHttpsCallable(functionName)
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {

                        if(task.getResult().getData() != null){
                            return task.getResult().getData().toString();

                        }else {
                            Log.d("ERROR", "task.getResult().getData() is returning null");
                            return null;
                        }
                    }
                });
    }
}
