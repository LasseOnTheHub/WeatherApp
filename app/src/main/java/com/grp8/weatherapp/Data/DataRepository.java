package com.grp8.weatherapp.Data;

import android.content.Context;
import android.util.Log;

import com.grp8.weatherapp.Data.API.Exceptions.APINetworkException;
import com.grp8.weatherapp.Data.API.Requests.APIDataReadingRequest;
import com.grp8.weatherapp.Data.API.Requests.APIStationRequest;
import com.grp8.weatherapp.Data.API.IDataProvider;

import com.grp8.weatherapp.Data.Database.DataReadingDatabaseHelper;
import com.grp8.weatherapp.Data.Database.Database;
import com.grp8.weatherapp.Data.Database.StationDatabaseHelper;
import com.grp8.weatherapp.Data.Mappers.IListableMapper;

import com.grp8.weatherapp.Entities.DataReading;
import com.grp8.weatherapp.Entities.Station;
import com.grp8.weatherapp.SupportingFiles.Environment;
import com.grp8.weatherapp.SupportingFiles.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Created by Thomas on 03-Jan-17.
 */
public class DataRepository
{
    private final static String TAG = "DataRepository";

    private int user;

    private IListableMapper<Station>     stationMapper;
    private IListableMapper<DataReading> readingMapper;

    private IDataProvider  provider;
    private Database       database;

    private Map<Integer, Station>           stations = new HashMap<>();
    private Map<Integer, List<DataReading>> readings = new HashMap<>();

    private Context context;

    /*
     * Constructor is made package-local
     */
    DataRepository(IDataProvider provider, Database database, IListableMapper<Station> stationMapper, IListableMapper<DataReading> readingMapper, Context context)
    {
        this.provider = provider;
        this.database = database;

        this.stationMapper = stationMapper;
        this.readingMapper = readingMapper;

        this.context = context;
    }

    public void setUser(int user)
    {
        this.user = user;
    }

    public boolean authorize(int id)
    {
        if(id == 5)
        {
            this.setUser(id);
            return true;
        }
        return false;
    }

    /**
     * Refreshes the data repository by clearing memory caches.
     */
    public void refresh()
    {
        this.refresh(false);
    }

    /**
     * Refreshed the data repository by clearing caches
     *
     * @param hard Whether the SQlite database should be reset.
     */
    public void refresh(boolean hard)
    {
        this.stations.clear();
        this.readings.clear();

        if(hard)
        {
            this.database.reset();
        }
    }

    /**
     * Returns the number of stations in the object cache.
     *
     * @return Returns an integer.
     */
    public int getStationCount()
    {
        int count = this.stations.size();

        if(count < 1)
        {
            count = StationDatabaseHelper.count(this.database);
        }

        return count;
    }

    /**
     * Fetches all stations associated with the current user
     */
    public List<Station> getStations()
    {
        if(this.user == 0)
        {
            throw new RuntimeException("Missing user ID.");
        }

        if(this.stations.size() > 0)
        {
            Log.d(TAG, "Returning stations from object cache.");

            return new ArrayList<>(this.stations.values());
        }

        List<Station> stations = StationDatabaseHelper.all(this.database);

        if(stations.size() < 1)
        {
            Log.d(TAG, "Fetching weather stations from remote API");

            if(!Utils.isNetworkAvailable(this.context))
            {
                if(Utils.isEmulator())
                {
                    Log.e(TAG, "Cannot access remote API", new APINetworkException());
                }

                return null;
            }

            String payload;

            payload = this.provider.fetch(new APIStationRequest(this.user));

            try
            {
                stations = this.stationMapper.map(new JSONArray(payload));
            }
            catch(JSONException e)
            {
                e.printStackTrace();
                return null;
            }

            for(Station station : stations)
            {
                this.stations.put(station.getId(), station);

                StationDatabaseHelper.add(this.database, station);
            }
        }

        return stations;
    }

    /**
     * Fetches a specific station associated with the current user and specified station ID
     *
     * @param id CLAFIS station ID
     */
    public Station getStation(int id)
    {
        if(this.user == 0)
        {
            throw new RuntimeException("Missing user ID.");
        }

        if(this.stations.containsKey(id))
        {
            return this.stations.get(id);
        }

        Station station = StationDatabaseHelper.fetch(this.database, id);

        if(station == null)
        {
            String payload;

            payload = this.provider.fetch(new APIStationRequest(this.user, id));

            try
            {
                station = this.stationMapper.map(new JSONObject(payload));
            }
            catch(JSONException e)
            {
                e.printStackTrace();
                return null;
            }

            this.stations.put(station.getId(), station);
            StationDatabaseHelper.add(this.database, station);
        }

        return station;
    }

    /**
     * Fetches the latest station data reading associated with the specified station ID
     *
     * @param station CLAFIS station ID
     */
    public DataReading getStationData(int station)
    {
        if(this.user == 0)
        {
            throw new RuntimeException("Missing user ID.");
        }

        if(Utils.isEmulator())
        {
            Log.d(TAG, "Attempting to fetch latest data reading from station: " + station);
        }

        if(!this.readings.isEmpty() && this.readings.containsKey(station))
        {
            List<DataReading> collection = this.readings.get(station);

            if(collection.size() == 1)
            {
                return collection.get(0);
            }

            DataReading current = null;

            for(DataReading item : collection)
            {
                if(current == null)
                {
                    current = item;
                    continue;
                }

                if(current.getTimestamp().compareTo(item.getTimestamp()) < 0)
                {
                    current = item;
                }
            }

            return current;
        }

        if(Utils.isEmulator())
        {
            Log.d(TAG, "Couldn't find matching data reading in object cache. Trying local database");
        }

        DataReading reading = DataReadingDatabaseHelper.latest(this.database, this.readingMapper, station);

        if(reading == null)
        {
            if(Utils.isEmulator())
            {
                Log.d(TAG, "Couldn't find matching data reading in local database. Trying remote API");
            }

            if(!Utils.isNetworkAvailable(this.context))
            {
                if(Utils.isEmulator())
                {
                    Log.e(TAG, "Cannot access remote API", new APINetworkException());
                }

                return null;
            }

            APIDataReadingRequest request = new APIDataReadingRequest(this.user, station);

            int ceiling = Environment.API_MAXIMUM_NUMBER_OF_RETRIES;
            int counter = 0;

            String    payload;
            JSONArray json = null;

            while(counter < ceiling)
            {
                payload = this.provider.fetch(request);

                try
                {
                    json = new JSONArray(payload);
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    continue;
                }

                if(json.length() > 0)
                {
                    break;
                }

                Log.d(TAG, "No data returned");

                request.increaseBackwardsReadingDateInterval();
                counter++;
            }

            if(counter == ceiling || json == null)
            {
                if(Utils.isEmulator())
                {
                    Log.w(TAG, "Reached maximum number of incremental retries. Returning null");
                }

                return null;
            }

            try
            {
                reading = this.readingMapper.map(json.getJSONObject(0));
            }
            catch(JSONException e)
            {
                e.printStackTrace();
                return null;
            }
        }

        return reading;
    }

    /**
     * Fetches all data reading with the specified date range associated with the
     * specified station ID
     *
     * @param station CLAFIS station ID
     * @param start   Beginning of date range
     * @param end     End of date range
     */
    public List<DataReading> getStationData(int station, Date start, Date end)
    {
        if(this.user == 0)
        {
            throw new RuntimeException("Missing user ID.");
        }

        if(start.compareTo(end) > 0)
        {
            throw new IllegalStateException("Start of date range cannot be after end of the range.");
        }

        if(!this.readings.isEmpty() && this.readings.containsKey(station))
        {
            List<DataReading> collection = this.readings.get(station);
            List<DataReading> results    = new ArrayList<>();

            for(DataReading entry : collection)
            {
                if(entry.getTimestamp().compareTo(start) > 0 || entry.getTimestamp().compareTo(end) < 0)
                {
                    results.add(entry);
                }
            }

            return results;
        }

        List<DataReading> results = DataReadingDatabaseHelper.fetch(this.database, this.readingMapper, station, end, start);

        if(results.size() < 1)
        {
            String    payload;
            JSONArray items;

            payload = this.provider.fetch(new APIDataReadingRequest(this.user, station, start, end));

            try
            {
                items   = new JSONArray(payload);
                results = this.readingMapper.map(items);
            }
            catch(JSONException e)
            {
                e.printStackTrace();

                return new ArrayList<>();
            }

            if(items.length() != results.size())
            {
                throw new RuntimeException("Cannot cache data readings due to mismatch in collection item count");
            }

            if(!this.readings.containsKey(station))
            {
                this.readings.put(station, new ArrayList<DataReading>());
            }

            for(int index = 0; index < results.size(); index++)
            {
                DataReading result = results.get(index);

                this.readings.get(station).add(result);

                try
                {
                    DataReadingDatabaseHelper.add(this.database, result.getID(), result.getDeviceID(), result.getTimestamp(), items.getString(index));
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return results;
    }
}
