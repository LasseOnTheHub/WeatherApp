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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/*
 * Created by Thomas on 03-Jan-17.
 */
public class DataRepositoryOLD
{
    private final static String TAG = "DataRepositoryOLD";

    private int user;

    private IListableMapper<Station>     stationMapper;
    private IListableMapper<DataReading> readingMapper;

    private IDataProvider  provider;
    private Database       database;

    private Map<Integer, Station>           stations = new HashMap<>();
    private Map<Integer, List<DataReading>> readings = new LinkedHashMap<Integer, List<DataReading>>(Environment.API_CACHE_DATA_READING_MAX_SIZE)
    {
        @Override
        protected boolean removeEldestEntry(Entry<Integer, List<DataReading>> entry)
        {
            return size() > Environment.API_CACHE_DATA_READING_MAX_SIZE;
        }
    };

    private Context context;

    /*
     * Constructor is made package-local
     */
    DataRepositoryOLD(IDataProvider provider, Database database, IListableMapper<Station> stationMapper, IListableMapper<DataReading> readingMapper, Context context)
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
     * Refreshed the data repository by clearing caches
     *
     * @param hard Whether the SQLite database should be reset.
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

            String    payload = "";
            JSONArray json    = null;

            if(!this.readings.containsKey(station))
            {
                this.readings.put(station, new ArrayList<DataReading>());
            }

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

            /*
             * Update timestamp to reflect manipulated data.
             */
            //updateTimestampOnManipulatedReading(reading);

            this.readings.get(station).add(reading);

            DataReadingDatabaseHelper.add(this.database, reading.getID(), reading.getDeviceID(), reading.getTimestamp(), payload);
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

        if(start.after(end))
        {
            throw new IllegalStateException("Start of date range cannot be after end of the range.");
        }

        boolean query = true;

        Map<Date, DataReading> results = extract(station, start, end);

        if(results.size() < 1)
        {
            results = sync(station, start, end, true);
            query   = false;
        }

        /*
         * Find newest and oldest readings
         */
        Date newest = null;
        Date oldest = null;

        for(Date date : results.keySet())
        {
            if(newest == null || date.after(newest))
            {
                newest = date;
            }

            if(oldest == null || date.before(oldest))
            {
                oldest = date;
            }
        }

        /*
         * Check if the result-set time range ends before the selection ends and more
         * data may exists.
         */
        if(newest != null && newest.before(end))
        {
            Map<Date, DataReading> subset = sync(station, newest, end, query);

            for(Date key : subset.keySet())
            {
                if(!results.containsKey(key))
                {
                    results.put(key, subset.get(key));
                }
            }
        }

        /*
         * Check if the result-set time range starts after the selection starts and more
         * data may exists
         */
        if(oldest != null && oldest.after(start))
        {
            Map<Date, DataReading> subset = sync(station, newest, end, query);

            for(Date key : subset.keySet())
            {
                if(!results.containsKey(key))
                {
                    results.put(key, subset.get(key));
                }
            }
        }

        /*
         * Sort the readings by the key set in natural order.
         */
        Map<Date, DataReading> sorted = new TreeMap<>(results);

        /*
         * Update data reading object cache
         */
        if(!this.readings.containsKey(station))
        {
            this.readings.put(station, new ArrayList<DataReading>());
        }

        for(DataReading reading : results.values())
        {
            if(!this.readings.get(station).contains(reading))
            {
                this.readings.get(station).add(reading);
            }
        }

        return new ArrayList<>(sorted.values());
    }

    private Map<Date, DataReading> extract(int station, Date start, Date end)
    {
        Map<Date, DataReading> result = new HashMap<>();

        if(!this.readings.containsKey(station))
        {
            return result;
        }

        for(DataReading reading : this.readings.get(station))
        {
            if(reading.getTimestamp().after(start) || reading.getTimestamp().before(end))
            {
                result.put(reading.getTimestamp(), reading);
            }
        }

        return result;
    }

    private Map<Date, DataReading> sync(int station, Date start, Date end, boolean query)
    {
        List<DataReading> rows = null;

        return null;
    }

    private Map<Date, DataReading> fetch(int station, Date start, Date end)
    {
        return null;
    }

    private List<DataReading> synchronize(int station, Date start, Date end)
    {
        List<DataReading> results;

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

        if(!this.readings.containsKey(station))
        {
            this.readings.put(station, new ArrayList<DataReading>());
        }

        for(int index = 0; index < results.size(); index++)
        {
            DataReading result = results.get(index);

            try
            {
                DataReadingDatabaseHelper.add(this.database, result.getID(), result.getDeviceID(), result.getTimestamp(), items.getString(index));
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
        }

        return results;
    }
}
