package com.grp8.weatherapp.Activities;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.grp8.weatherapp.R;
import com.grp8.weatherapp.TestData.WeatherStation;
import com.grp8.weatherapp.TestData.WeatherStations;

import java.util.Map;

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
            TextView stationTitle;
            TextView timeLabel;
            TextView tempLabel;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
/*


            ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.stationlistelement, parent, false);
                viewHolder.stationTitle = (TextView) convertView.findViewById(R.id.title_label);
                viewHolder.timeLabel = (TextView) convertView.findViewById(R.id.time_label);
                viewHolder.tempLabel = (TextView) convertView.findViewById(R.id.temp_label);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (com.grp8.weatherapp.Activities.WeatherStationsAdapter.ViewHolder) convertView.getTag();
            }

            viewHolder.stationTitle.setText(station.getTitle());
            viewHolder.timeLabel.setText("11:52");
            if (position%2==0) {
                viewHolder.timeLabel.setText("13:52");
            }
            viewHolder.tempLabel.setText(String.valueOf(station.getWeatherData().getAirTemp())+"º");

            return convertView;*/
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            // Missing implementation
            return 0;
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }



}


