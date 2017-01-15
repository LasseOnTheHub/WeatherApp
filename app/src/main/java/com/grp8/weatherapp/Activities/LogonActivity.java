package com.grp8.weatherapp.Activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.grp8.weatherapp.Logic.Authorizer;
import com.grp8.weatherapp.Data.DataRepositoryFactory;
import com.grp8.weatherapp.Logic.SettingsManager;
import com.grp8.weatherapp.R;
import com.grp8.weatherapp.Logic.Constants;
import com.grp8.weatherapp.Logic.Utils;

import io.fabric.sdk.android.Fabric;

public class LogonActivity extends AppCompatActivity {

    private EditText passwordEditText;
    private EditText userIDEditText;
    private TextInputLayout inputLayoutName, inputLayoutPassword;

    Authorizer authroizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        if (!Utils.isEmulator()) {
            Fabric.with(this, new Crashlytics());
        }
        setContentView(R.layout.activity_logon);

        inputLayoutName = (TextInputLayout) findViewById(R.id.userIDTextLayout);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.userPasswordTextLayout);
        passwordEditText = (EditText) findViewById(R.id.userPasswordText);
        userIDEditText = (EditText) findViewById(R.id.userIDText);
        authroizer = new Authorizer();
    }

    public void login(View view) {
        if (userIDEditText.getText().toString().matches(""))
        {
            Toast.makeText(this, "Indtast brugernavn", Toast.LENGTH_SHORT).show(); // TODO: add lang string
        }
        else
        {
            String userId = userIDEditText.getText().toString();
            int userIdInt = Integer.parseInt(userId);
            if (DataRepositoryFactory.build(getApplicationContext()).authorize(userIdInt)) {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                if (!Utils.isEmulator()) {
                    logUser(userId);
                }

                DataRepositoryFactory.build(getApplicationContext()).setUser(5);

                Intent intent = new Intent(LogonActivity.this,MainActivityTab.class);
                intent.putExtra(Constants.KEY_USERID,userId);
                startActivity(intent);

                SettingsManager.setupSettings(getApplicationContext());
                finish();


            } else {
                Toast.makeText(this, R.string.forkert_login, Toast.LENGTH_SHORT).show();
            }

        }
    }
    //Bruges til at logge brugeren til identifikation under nedbrudsrapportering
    private void logUser(String userName) {
        Crashlytics.setUserIdentifier(userName);
        Crashlytics.setUserEmail("Ikke angivet");
        Crashlytics.setUserName("Ikke angivet");
    }
}
