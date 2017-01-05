package com.grp8.weatherapp.Data.Mappers;

import com.grp8.weatherapp.Entities.Data.Air;
import com.grp8.weatherapp.Entities.Data.Soil;
import com.grp8.weatherapp.Entities.Data.Wind;
import com.grp8.weatherapp.Entities.DataReading;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by t_bit on 05-01-2017.
 */
public class DataReadingMapper implements IListableMapper<DataReading>
{
    @Override
    public List<DataReading> map(String[] collection)
    {
        List<DataReading> results = new ArrayList<>();

        for(String item : collection)
        {
            results.add(this.map(item));
        }

        return results;
    }

    @Override
    public DataReading map(String json)
    {
        JSONObject main;

        try
        {
            main = new JSONObject(json);

            int id        = main.getInt("id");
            int timestamp = main.getInt("timestamp");

            double rainfall = main.getDouble("rainfall");

            Air air = new Air(
                main.getInt("airPressure"),
                main.getInt("relativeAirHumidity"),
                main.getDouble("airTemp")
            );

            Wind wind = new Wind(
                main.getDouble("windSpeed"),
                main.getInt("windDirection")
            );

            /*
             * Soil
             */
            int[] moisture    = new int[4];
            int[] temperature = new int[4];

            moisture[0] = main.getInt("soilMoistureOne");
            moisture[1] = main.getInt("soilMoistureTwo");
            moisture[2] = main.getInt("soilMoistureThree");
            moisture[3] = main.getInt("soilMoistureFour");

            temperature[0] = main.getInt("soilTemperatureOne");
            temperature[1] = main.getInt("soilTemperatureTwo");
            temperature[2] = main.getInt("soilTemperatureThree");
            temperature[3] = main.getInt("soilTemperatureFour");

            return new DataReading(id, timestamp, rainfall, air, wind, new Soil(moisture, temperature));
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
