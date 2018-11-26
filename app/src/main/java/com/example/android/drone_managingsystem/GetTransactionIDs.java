package com.example.android.drone_managingsystem;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.stream.Stream;

public class GetTransactionIDs extends AsyncTask<Object,Void,String[]> {

    //private final String host="10.0.2.2";
    private final String host="192.168.43.120";

    private WeakReference<LinearLayout> linearLayout;
    private WeakReference<Context> weakContext;
    private String emailAddress;
    private MainActivity.Type type;


    public GetTransactionIDs(LinearLayout linearLayout, Context context) {

        this.linearLayout = new WeakReference<>(linearLayout);
        this.weakContext=new WeakReference<>(context);
    }


    @Override
    protected String[] doInBackground(Object... Objects) {
        emailAddress = (String) Objects[0];
        type = (MainActivity.Type) Objects[1];

        String api_url1 = "http://"+host+":3000/api/org.drone.mynetwork.MiddlerPassingRecord";
        String api_url2 = "http://"+host+":3000/api/org.drone.mynetwork.ShipmentRecord";
        String jsonString1=NetworkUtils.Get(api_url1);
        String jsonString2="";
        if(type!=MainActivity.Type.MIDDLER){
            jsonString2=NetworkUtils.Get(api_url2);
        }


        return new String[] {jsonString1,jsonString2};
    }

    @Override
    protected void onPostExecute(String[] ss) {
        super.onPostExecute(ss);
        String middlerPassingRecordsJsonString=ss[0];
        String shipmentRecordsJsonString=ss[1];

        String typeStr = "initial type";
        switch (type) {
            case SENDER:
                typeStr = "sender";
                break;
            case MIDDLER:
                typeStr = "middler";
                break;
            case RECEIVER:
                typeStr = "receiver";
                break;
        }

        try {
            boolean found=false;
            // Convert the response into a JSON Array.
            JSONArray jsonArray = new JSONArray(middlerPassingRecordsJsonString);
            String middlerPassingTransactionID="";
            String ShipmentTransactionID="";

            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject= jsonArray.getJSONObject(i);
                if(jsonObject.getString(typeStr).split("#")[1].equals(emailAddress)){

                    middlerPassingTransactionID=jsonObject.getString("middlerPassingRecordId");
                    /*
                    String record=jsonObject.toString();
                    TextView tv=new TextView(weakContext.get());
                    tv.setText(record);

                    linearLayout.get().addView(tv);
                    linearLayout.get().setVisibility(View.VISIBLE);
                    */
                    found=true;
                }
            }

            if(typeStr!="middler"){
                JSONArray jsonArray2 = new JSONArray(shipmentRecordsJsonString);
                for(int i=0; i<jsonArray2.length(); i++){
                    JSONObject jsonObject= jsonArray2.getJSONObject(i);
                    if(jsonObject.getString(typeStr).split("#")[1].equals(emailAddress)){

                        ShipmentTransactionID=jsonObject.getString("shipmentRecordId");

                        /*
                        String record=jsonObject.toString();

                        TextView tv=new TextView(weakContext.get());
                        tv.setText(record);

                        linearLayout.get().addView(tv);
                        linearLayout.get().setVisibility(View.VISIBLE);
                        */
                        found=true;
                    }
                }
            }

            new GetTransactionHistory(linearLayout,weakContext).execute(new String[] {middlerPassingTransactionID,ShipmentTransactionID});

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
