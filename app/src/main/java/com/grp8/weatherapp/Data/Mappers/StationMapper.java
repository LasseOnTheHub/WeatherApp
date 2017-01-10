package com.grp8.weatherapp.Data.Mappers;

import android.text.TextUtils;

import com.grp8.weatherapp.Entities.Station;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by t_bit on 05-01-2017.
 */

public class StationMapper implements IListableMapper<Station>
{
    @Override
    public List<Station> map(String[] collection)
    {
        try
        {
            return this.map(new JSONArray(TextUtils.join(",", collection)));
        }
        catch(JSONException e)
        {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public Station map(String json)
    {
        try
        {
            return this.map(new JSONObject(json));
        }
        catch(JSONException e)
        {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public List<Station> map(JSONArray collection)
    {
        List<Station> results = new ArrayList<>();

        for(int index = 0; index < collection.length(); index++)
        {
            try
            {
                results.add(this.map(collection.getJSONObject(index)));
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
        }

        return results;
    }

    @Override
    public Station map(JSONObject json)
    {
        try
        {
            int id   = json.getInt("id");
            int type = json.getInt("deviceType");

            String notes = json.getString("notes");

            double latitude  = json.getDouble("latitude");
            double longitude = json.getDouble("longitude");

            return new Station(id, type, notes, latitude, longitude);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
