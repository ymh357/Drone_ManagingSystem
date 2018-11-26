package com.example.android.drone_managingsystem;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class SendPost extends AsyncTask<Void,Void, Integer> {

    @Override
    protected Integer doInBackground(Void... voids) {
        int responseCode=-2;
        try{
            String jsonDataStr="{\n" +
                    "  \"$class\": \"org.drone.mynetwork.PassingMiddlerTransaction\",\n" +
                    "  \"middlerPassingRecord\": \"resource:org.drone.mynetwork.MiddlerPassingRecord#MIDPASS_001\"\n" +
                    "}";
            JSONObject jsonObject=new JSONObject(jsonDataStr);
            responseCode=NetworkUtils.Post("http://10.0.2.2:3000/api/org.drone.mynetwork.PassingMiddlerTransaction",jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            return responseCode;
        }

    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        Log.d("hello",integer.toString());
    }
}
