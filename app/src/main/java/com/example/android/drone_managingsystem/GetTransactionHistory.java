package com.example.android.drone_managingsystem;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class GetTransactionHistory extends AsyncTask<String,Void,String[]> {

    //private final String host="10.0.2.2";
    private final String host="192.168.43.120";

    private WeakReference<LinearLayout> linearLayout;
    private WeakReference<Context> weakContext;

    private String passingMiddlerTransactionID;
    private String ShipmentArrivedTransactionID;

    public GetTransactionHistory(WeakReference<LinearLayout> linearLayout, WeakReference<Context> context) {

        this.linearLayout = linearLayout;
        this.weakContext=context;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        String jsonString1="";
        String jsonString2="";
        if(strings[0] != "" && strings[0]!=null){
            passingMiddlerTransactionID=strings[0];
            jsonString1=NetworkUtils.Get("http://"+host+":3000/api/org.drone.mynetwork.PassingMiddlerTransaction");
        }
        if(strings[1] != "" && strings[1]!=null){
            ShipmentArrivedTransactionID=strings[1];
            jsonString2=NetworkUtils.Get("http://"+host+":3000/api/org.drone.mynetwork.ShipmentArrivedTransaction");
        }
        return new String[] {jsonString1,jsonString2};
    }

    @Override
    protected void onPostExecute(String[] strings) {
        super.onPostExecute(strings);
        String jsonString1=strings[0];
        String jsonString2=strings[1];
        try {
            boolean found=false;
            // Convert the response into a JSON Array.
            JSONArray jsonArray = new JSONArray(jsonString1);

            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject= jsonArray.getJSONObject(i);
                if(jsonObject.getString("middlerPassingRecord").split("#")[1].equals(passingMiddlerTransactionID)){


                    String record=jsonObject.toString();
                    TextView tv=new TextView(weakContext.get());
                    tv.setText(record);

                    linearLayout.get().addView(tv);
                    linearLayout.get().setVisibility(View.VISIBLE);

                    found=true;
                }
            }

            if(jsonString2!="" && jsonString2!=null){
                JSONArray jsonArray2 = new JSONArray(jsonString2);
                for(int i=0; i<jsonArray2.length(); i++){
                    JSONObject jsonObject= jsonArray2.getJSONObject(i);
                    if(jsonObject.getString("shipmentRecord").split("#")[1].equals(ShipmentArrivedTransactionID)){


                        String record=jsonObject.toString();

                        TextView tv=new TextView(weakContext.get());
                        tv.setText(record);

                        linearLayout.get().addView(tv);
                        linearLayout.get().setVisibility(View.VISIBLE);

                        found=true;
                    }
                }
            }



            if(!found){
                TextView tv=new TextView(weakContext.get());
                tv.setText("Email Not Found.");
                linearLayout.get().addView(tv);
                linearLayout.get().setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            // If onPostExecute() does not receive a proper JSON string,
            // update the UI to show failed results.
            TextView tv=new TextView(weakContext.get());
            tv.setText("Error occurs when parsing JSON. So the result cannot be shown.");

            linearLayout.get().addView(tv);
            linearLayout.get().setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }
}
