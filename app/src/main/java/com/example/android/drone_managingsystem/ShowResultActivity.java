package com.example.android.drone_managingsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ShowResultActivity extends AppCompatActivity {

    private TextView balanceTextView;
    private ScrollView recordsScrollView;
    private String emailAddress;
    private MainActivity.Type type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        balanceTextView=findViewById(R.id.balance_textView);
        recordsScrollView=findViewById(R.id.records_scrollView);

        Intent intent=getIntent();
        emailAddress = intent.getStringExtra(MainActivity.EXTRA_EMAIL);
        type=(MainActivity.Type) intent.getSerializableExtra(MainActivity.EXTRA_TYPE);

    }

    public void getBalance(View view) {
        new GetBalance(balanceTextView).execute(emailAddress,type);
    }

    public void getRecords(View view) {
        recordsScrollView.setVisibility(View.VISIBLE);
        LinearLayout linearLayout= (LinearLayout) recordsScrollView.getChildAt(0);
        new GetTransactionIDs(linearLayout,this).execute(emailAddress,type);
    }

    /*public void sendPost(View view) {
        new SendPost().execute();
    }*/
}
