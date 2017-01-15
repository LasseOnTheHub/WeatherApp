package com.grp8.weatherapp.Data;
/*
 * Created by Thomas on 12-Jan-17.
 */

import android.os.Looper;
import android.util.Log;

import com.grp8.weatherapp.Data.API.APIDataProvider;
import com.grp8.weatherapp.Data.API.Requests.APIDataReadingRequest;
import com.grp8.weatherapp.Data.API.Requests.APIStationRequest;
import com.grp8.weatherapp.Data.Mappers.IListableMapper;
import com.grp8.weatherapp.Entities.DataReading;
import com.grp8.weatherapp.Entities.Station;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataRepository implements IDataRepository
{
    private final static String TAG = "DataRepository";

    private int user;

    private APIDataProvider api;

    private IListableMapper<Station>     stations;
    private IListableMapper<DataReading> readings;

    private Map<Integer, Station> cache = new HashMap<>();

    public DataRepository(APIDataProvider api, IListableMapper<Station> stations, IListableMapper<DataReading> readings)
    {
        this.api = api;

        this.stations = stations;
        this.readings = readings;
    }

    @Override
    public boolean authorize(int user)
    {
        return user == 5; // TODO: Add proper auth
    }

    @Override
    public void setUser(int user)
    {
        this.user = user;
    }

    @Override
    public void refresh()
    {
        this.cache.clear();
    }

    @Override
    public Station getStation(int id)
    {
        if(this.cache.containsKey(id))
        {
            return this.cache.get(id);
        }

        String payload = this.api.fetch(new APIStationRequest(this.user, id));

        try
        {
            return this.stations.map(new JSONObject(payload));
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Station> getStations()
    {
        if(this.user == 0)
        {
            throw new RuntimeException("Missing user ID");
        }

        if(Looper.getMainLooper().getThread() == Thread.currentThread())
        {
            return new ArrayList<>(this.cache.values());
        }

        String payload = this.api.fetch(new APIStationRequest(this.user));

        try
        {
            List<Station> stations = this.stations.map(new JSONArray(payload));

            this.addToCache(stations);

            return stations;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public int getStationCount()
    {
        return this.cache.size();
    }

    @Override
    public DataReading getStationData(int station)
    {
        if(this.user == 0)
        {
            throw new RuntimeException("Missing user ID");
        }

        APIDataReadingRequest request = new APIDataReadingRequest(this.user, station);

        int ceiling = 5;
        int counter = 0;

        JSONArray json = null;

        while(counter < ceiling)
        {
            String payload = this.api.fetch(request);

            try
            {
                json = new JSONArray(payload);

                if(json.length() > 0)
                {
                    break;
                }

                Log.d(TAG, "No data returned");
            }
            catch(JSONException e)
            {
                e.printStackTrace();
                return null;
            }

            request.increaseBackwardsReadingDateInterval();
            counter++;
        }

        if(counter == ceiling || json == null)
        {
            Log.w(TAG, "Reached maximum number of incremental retries. Returning null");
            return null;
        }

        try
        {
            return this.readings.map(json.getJSONObject(0));
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<DataReading> getStationData(int station, Date start, Date end)
    {
        if(this.user == 0)
        {
            throw new RuntimeException("Missing user ID");
        }

        String payload = this.api.fetch(new APIDataReadingRequest(this.user, station, start, end));

        try
        {
            return this.readings.map(new JSONArray(payload));
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    protected void addToCache(List<Station> stations)
    {
        for(Station station : stations)
        {
            if(!this.cache.containsKey(station.getId()))
            {
                this.cache.put(station.getId(), station);
            }
        }
    }
}
