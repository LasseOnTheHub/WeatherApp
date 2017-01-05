package com.grp8.weatherapp.Data.Mappers;

import com.grp8.weatherapp.Entities.Station;

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
        List<Station> results = new ArrayList<>();

        for(String item : collection)
        {
            results.add(this.map(item));
        }

        return results;
    }

    @Override
    public Station map(String json)
    {
        JSONObject main;

        try
        {
            main = new JSONObject(json);

            int id   = main.getInt("id");
            int type = main.getInt("deviceType");

            String notes = main.getString("notes");

            double latitude  = main.getDouble("latitude");
            double longitude = main.getDouble("longitude");

            return new Station(id, type, notes, latitude, longitude);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
