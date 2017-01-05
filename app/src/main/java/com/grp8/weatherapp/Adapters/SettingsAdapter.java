package com.grp8.weatherapp.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.grp8.weatherapp.SupportingFiles.Constants;
import com.grp8.weatherapp.R;

/**
 * Created by Frederik on 05/01/2017.
 */

public class SettingsAdapter extends BaseAdapter {

        private String[] settings = Constants.SETTINGS;
        private LayoutInflater inflater;

        public SettingsAdapter(Activity activity) {
            super();
            inflater = activity.getLayoutInflater();
        }

        private static class ViewHolder {
            TextView title;
            Spinner dropDown;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.settings_element, parent, false);
                viewHolder.title = (TextView) convertView.findViewById(R.id.settingTitleLabel);
                viewHolder.dropDown = (Spinner) convertView.findViewById(R.id.dropDown);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.title.setText(settings[position]);
            // Sæt spinnerens adapter

            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getCount() {
            return settings.length;
        }

        @Override
        public Object getItem(int position) {
            return settings[position];
        }



}


