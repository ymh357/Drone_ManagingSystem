package com.example.android.drone_managingsystem;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText emailAddress;
    private Type type;

    public static final String EXTRA_EMAIL =
            "com.example.android.Drone_ManagingSystem.extra.EMAIL";
    public static final String EXTRA_TYPE =
            "com.example.android.Drone_ManagingSystem.extra.TYPE";

    public enum Type{
        SENDER,
        MIDDLER,
        RECEIVER
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailAddress=findViewById(R.id.email);
    }



    public void submitRequest(View view) {

        if(emailAddress.getText().length()==0 || type==null){
            Toast toast=Toast.makeText(this,"Please check the input before query.",Toast.LENGTH_SHORT);
            toast.show();
        }else{
            // Hide keyboard while querying.
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null ) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }

            // Check the status of the network connection.
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (connMgr != null) {
                networkInfo = connMgr.getActiveNetworkInfo();
            }
            // If the network is available, connected, and the search field
            // is not empty, start a FetchBook AsyncTask.
            if (networkInfo != null && networkInfo.isConnected()) {

                Intent intent = new Intent(this, ShowResultActivity.class);
                intent.putExtra(EXTRA_EMAIL,emailAddress.getText().toString());
                intent.putExtra(EXTRA_TYPE,type);
                startActivity(intent);
            }
            // Otherwise update the TextView to tell the user there is no
            // connection, or no search term.
            else {
                Toast toast=Toast.makeText(this,"Query failed due to network issues.",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void onRadioButtonChecked(View view) {
        switch (view.getId()){
            case R.id.radioButton_sender:
                type=Type.SENDER;
                break;
            case R.id.radioButton_middler:
                type=Type.MIDDLER;
                break;
            case R.id.radioButton_receiver:
                type=Type.RECEIVER;
                break;
        }
    }
}
