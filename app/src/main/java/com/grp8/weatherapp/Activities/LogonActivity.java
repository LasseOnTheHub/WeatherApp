package com.grp8.weatherapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

import com.grp8.weatherapp.Logic.SettingsManager;
import com.grp8.weatherapp.Logic.Constants;
import com.grp8.weatherapp.Logic.UserManager;
import com.grp8.weatherapp.Logic.Utils;
import com.grp8.weatherapp.R;

public class LogonActivity extends AppCompatActivity {

    private EditText userIDEditText;

    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());

        this.userManager = UserManager.getInstance(getApplicationContext());

        if(userManager.isValid())
        {
            userManager.resume(getApplicationContext());

            startActivity(this.forward(userManager.getUserID()));
            finish();

            return;
        }

        setContentView(R.layout.activity_logon);

        this.userIDEditText     = (EditText) findViewById(R.id.userIDText);
        EditText passwordIDEditText = (EditText) findViewById(R.id.userPasswordText);
        userIDEditText.setText("5");

        @SuppressLint("WrongViewCast")
        final AppCompatImageView logo = (AppCompatImageView) findViewById(R.id.imageLogo);

        View content = findViewById(R.id.logon_layout_content);

        content.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
        {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
            {
                if(bottom < oldBottom) // Keyboard is visible
                {
                    logo.setVisibility(View.GONE);
                }
                else if(bottom > oldBottom) // Keyboard is hidden
                {
                    logo.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void login(View view)
    {
        if (userIDEditText.getText().toString().matches(""))
        {
            Toast.makeText(this, R.string.logon_toast_username, Toast.LENGTH_SHORT).show();
        }
        else
        {
            int userID = Integer.parseInt(userIDEditText.getText().toString());

            if(this.userManager.login(getApplicationContext(), userID))
            {
                Toast.makeText(this, R.string.logon_success, Toast.LENGTH_SHORT).show();

                if (!Utils.isEmulator())
                {
                    logUser(Integer.toString(userID));
                }

                SettingsManager.setupSettings(getApplicationContext());

                startActivity(this.forward(userID));
                finish();
            }
            else
            {
                Toast.makeText(this, R.string.logon_failure, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Intent forward(int id)
    {
        return new Intent(this, MainActivityTab.class).putExtra(Constants.KEY_USERID, id);
    }

    private void logUser(String userName)
    {
        Crashlytics.setUserIdentifier(userName);
        Crashlytics.setUserEmail("Ikke angivet");
        Crashlytics.setUserName("Ikke angivet");
    }
}
