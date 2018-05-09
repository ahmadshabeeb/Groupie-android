package app.kth.com.groupie.utilities;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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

    public static String getWeekDay(long day, boolean fullName){
        Date date = new Date(day * 1000l);
        DateFormat sdf;

        if (fullName)
            sdf = new SimpleDateFormat("EEEE");
        else
            sdf = new SimpleDateFormat("EEE");

        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String weekDay = sdf.format(date);
        Log.d("Week day" , weekDay + "..." );
        return weekDay;
    }

    public static Boolean buttonTimeout(Button button) {
        if (button.isEnabled()) {
            Log.d("TAG", "BUTTON PRESSED");

            button.setEnabled(false);
            button.postDelayed(new Runnable() {
                @Override
                public void run() {
                    button.setEnabled(true);
                }
            }, 2500);

            return true;
        } else return false;
    }
}
