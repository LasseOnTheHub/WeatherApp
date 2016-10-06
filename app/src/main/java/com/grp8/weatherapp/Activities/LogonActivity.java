package com.grp8.weatherapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.grp8.weatherapp.BussinessLogic.Authorizer;
import com.grp8.weatherapp.R;

public class LogonActivity extends AppCompatActivity {

    private EditText passwordEditText;
    private EditText userIDEditText;

    Authorizer authroizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);

        passwordEditText = (EditText) findViewById(R.id.userPasswordText);
        userIDEditText = (EditText) findViewById(R.id.userIDText);
        authroizer = new Authorizer();
    }


    public void login(View view) {
        if (userIDEditText.getText().toString().matches(""))
        {
            Toast.makeText(this, "Indtast brugernavn", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String userId = userIDEditText.getText().toString();
            int userIdInt = Integer.parseInt(userId);
            if (authroizer.Authorize(userIdInt)) {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LogonActivity.this,MainActivity.class);



                intent.putExtra(Constants.KEY_USERID,userId);
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.forkert_login, Toast.LENGTH_SHORT).show();
            }

        }
    }
}
