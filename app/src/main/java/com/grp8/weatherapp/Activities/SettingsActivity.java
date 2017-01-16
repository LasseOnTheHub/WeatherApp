package com.grp8.weatherapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.grp8.weatherapp.Fragments.SettingsFragment;
import com.grp8.weatherapp.R;

public class SettingsActivity extends AppCompatActivity {

    private boolean changed = false;

    public void setChangedStatus(boolean status)
    {
        this.changed = status;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupActionBar();
        getFragmentManager().beginTransaction().replace(R.id.settings_content, new SettingsFragment()).commit();
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("Settings");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish()
    {
        if(this.changed)
        {
            setResult(RESULT_OK, new Intent().putExtra("reload", true));
            Toast.makeText(this, R.string.settings_save_success, Toast.LENGTH_SHORT).show();
        }

        super.finish();
    }
    
}
