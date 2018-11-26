package com.example.android.drone_managingsystem;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetBalance extends AsyncTask<Object, Void, String> {

    //private final String host="10.0.2.2";
    private final String host="192.168.43.120";

    private WeakReference<TextView> balanceTextView;
    private String emailAddress;
    private MainActivity.Type type;


    public GetBalance(TextView balanceTextView) {

        this.balanceTextView = new WeakReference<>(balanceTextView);
    }


    @Override
    protected String doInBackground(Object... Objects) {
        emailAddress = (String) Objects[0];
        type = (MainActivity.Type) Objects[1];

        String api_url = "initial url address";
        switch (type) {
            case SENDER:
                api_url = "http://"+host+":3000/api/org.drone.mynetwork.Sender";
                break;
            case MIDDLER:
                api_url = "http://"+host+":3000/api/org.drone.mynetwork.Middler";
                break;
            case RECEIVER:
                api_url = "http://"+host+":3000/api/org.drone.mynetwork.Receiver";
                break;
        }

        return NetworkUtils.Get(api_url);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            boolean found=false;
            // Convert the response into a JSON Array.
            JSONArray jsonArray = new JSONArray(s);

            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject= jsonArray.getJSONObject(i);
                if(jsonObject.getString("email").equals(emailAddress)){
                    int balance=jsonObject.getInt("accountBalance");
                    balanceTextView.get().setText(Integer.toString(balance));
                    balanceTextView.get().setVisibility(View.VISIBLE);
                    found=true;
                    break;
                }
            }

            if(!found){
                balanceTextView.get().setText("Email Not Found.");
                balanceTextView.get().setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            // If onPostExecute() does not receive a proper JSON string,
            // update the UI to show failed results.
            balanceTextView.get().setText("Error occurs when parsing JSON. So the result cannot be shown.");
            balanceTextView.get().setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }
}
