package com.grp8.weatherapp.Data.Mappers;

import com.grp8.weatherapp.Entities.Data.Air;
import com.grp8.weatherapp.Entities.Data.Soil;
import com.grp8.weatherapp.Entities.Data.Wind;
import com.grp8.weatherapp.Entities.DataReading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by t_bit on 05-01-2017.
 */
public class DataReadingMapper implements IListableMapper<DataReading>
{
    @Override
    public List<DataReading> map(JSONArray collection)
    {
        List<DataReading> results = new ArrayList<>();

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
    public DataReading map(JSONObject json)
    {
        try
        {
            int  id        = json.getInt("id");
            int  device    = json.getInt("deviceID");
            long timestamp = json.getLong("timestamp");

            double rainfall = json.getDouble("rainfall");

            Air air = new Air(
                json.getInt("airPressure"),
                json.getInt("relativeAirHumidity"),
                json.getDouble("airTemp")
            );

            Wind wind = new Wind(
                json.getDouble("windSpeed"),
                json.getInt("windDirection")
            );

            /*
             * Soil
             */
            int[] moisture    = new int[4];
            int[] temperature = new int[4];

            moisture[0] = json.getInt("soilMoistureOne");
            moisture[1] = json.getInt("soilMoistureTwo");
            moisture[2] = json.getInt("soilMoistureThree");
            moisture[3] = json.getInt("soilMoistureFour");

            temperature[0] = json.getInt("soilTemperatureOne");
            temperature[1] = json.getInt("soilTemperatureTwo");
            temperature[2] = json.getInt("soilTemperatureThree");
            temperature[3] = json.getInt("soilTemperatureFour");

            return new DataReading(id, device, new Date(timestamp), rainfall, air, wind, new Soil(moisture, temperature));
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
