package com.lasse.watherapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.lasse.watherapp.controller.Constants;

public class MainActivity extends AppCompatActivity {


    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Hejsa");
        toolbar.setSubtitle("Alle sammen");

//        EditText userID = (EditText) findViewById(R.id.getID);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        String user = b.getString(Constants.KEY_USERID);

//        userID.setText(user);

    }
}
