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

    private EditText userIDEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (!Utils.isEmulator()) {
            Fabric.with(this, new Crashlytics());
        }

        setContentView(R.layout.activity_logon);

        this.userIDEditText = (EditText) findViewById(R.id.userIDText);
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

            if (DataRepositoryFactory.build(getApplicationContext()).authorize(userID))
            {
                Toast.makeText(this, R.string.logon_success, Toast.LENGTH_SHORT).show();

                if (!Utils.isEmulator())
                {
                    logUser(Integer.toString(userID));
                }

                DataRepositoryFactory.build(getApplicationContext()).setUser(5);
                SettingsManager.setupSettings(getApplicationContext());

                Intent intent = new Intent(LogonActivity.this,MainActivityTab.class);
                intent.putExtra(Constants.KEY_USERID, userID);

                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(this, R.string.logon_failure, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void logUser(String userName)
    {
        Crashlytics.setUserIdentifier(userName);
        Crashlytics.setUserEmail("Ikke angivet");
        Crashlytics.setUserName("Ikke angivet");
    }
}
