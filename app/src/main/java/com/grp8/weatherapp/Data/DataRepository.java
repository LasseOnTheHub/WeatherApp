package com.grp8.weatherapp.Data;

import com.grp8.weatherapp.Data.API.Requests.APIDataReadingRequest;
import com.grp8.weatherapp.Data.API.Requests.APIStationRequest;
import com.grp8.weatherapp.Data.API.IDataProvider;

import com.grp8.weatherapp.Data.Database.DataReadingDatabaseHelper;
import com.grp8.weatherapp.Data.Database.Database;
import com.grp8.weatherapp.Data.Database.StationDatabaseHelper;
import com.grp8.weatherapp.Data.Mappers.IListableMapper;

import com.grp8.weatherapp.Entities.DataReading;
import com.grp8.weatherapp.Entities.Station;

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
    private int user;

    private IListableMapper<Station>     stationMapper;
    private IListableMapper<DataReading> readingMapper;

    private IDataProvider  provider;
    private Database       database;

    private Map<Integer, Station> stations = new HashMap<>();
    private Map<Integer, Map<Date, DataReading>> readings = new HashMap<>();

    /*
     * Constructor is made package-local
     */
    DataRepository(IDataProvider provider, Database database, IListableMapper<Station> stationMapper, IListableMapper<DataReading> readingMapper)
    {
        this.provider = provider;
        this.database = database;

        this.stationMapper = stationMapper;
        this.readingMapper = readingMapper;
    }

    public void setUser(int user)
    {
        this.user = user;
    }

    public boolean authorize(int id)
    {
        return id == 5;
    }

    public void test()
    {
        Station station = StationDatabaseHelper.fetch(this.database, 2);

        if(station == null)
        {
            System.out.println("No station with the specified id");
        }
        else
        {
            System.out.println(station.getId());
        }
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
            String payload;

            payload  = this.provider.fetch(new APIStationRequest(this.user));
            stations = this.stationMapper.map(this.split(payload));

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
            this.stations.get(id);
        }

        Station station = StationDatabaseHelper.fetch(this.database, id);

        if(station == null)
        {
            String payload;

            payload = this.provider.fetch(new APIStationRequest(this.user, id));
            station = this.stationMapper.map(payload);

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

        if(!this.readings.isEmpty() && this.readings.containsKey(station))
        {
            List<DataReading> collection = new ArrayList<>(this.readings.get(station).values());

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

        DataReading reading = DataReadingDatabaseHelper.latest(this.database, this.readingMapper, station);

        if(reading == null)
        {
            APIDataReadingRequest request = new APIDataReadingRequest(this.user, station);

            int ceiling = 3;
            int counter = 0;

            String payload = "[]";

            while(payload.equals("[]") && counter < ceiling)
            {
                payload = this.provider.fetch(request);

                if(!payload.equals("[]"))
                {
                    break;
                }

                request.increaseBackwardsReadingDateInterval();
                counter++;
            }

            reading = (DataReading) this.readingMapper.map(this.split(payload));
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
            Map<Date, DataReading> collection = this.readings.get(station);
            List<DataReading>      results    = new ArrayList<>();

            for(Map.Entry<Date, DataReading> entry : collection.entrySet())
            {
                if(entry.getKey().compareTo(start) > 0 || entry.getKey().compareTo(end) < 0)
                {
                    results.add(entry.getValue());
                }
            }

            return results;
        }

        List<DataReading> results = DataReadingDatabaseHelper.fetch(this.database, this.readingMapper, station, end, start);

        if(results.size() < 1)
        {
            String   payload;
            String[] items;

            payload = this.provider.fetch(new APIDataReadingRequest(this.user, station, start, end));
            items   = this.split(payload);
            results = this.readingMapper.map(items);

            if(items.length != results.size())
            {
                throw new RuntimeException("Cannot cache data readings due to mismatch in collection item count");
            }

            if(!this.readings.containsKey(station))
            {
                this.readings.put(station, new HashMap<Date, DataReading>());
            }

            for(int index = 0; index < results.size(); index++)
            {
                DataReading result = results.get(index);

                this.readings.get(station).put(result.getTimestamp(), result);
                DataReadingDatabaseHelper.add(this.database, result.getID(), result.getDeviceID(), result.getTimestamp(), items[index]);
            }
        }

        return results;
    }

    /**
     * Splits a JSON payload into separate array elements.
     *
     * @param payload A JSON payload
     */
    private String[] split(String payload)
    {
        String[] elements = payload.substring(1, payload.length() - 1).split(",\\{$");

        for(int index = 0; index < elements.length; index++)
        {
            if(!elements[index].substring(0, 1).equals("{"))
            {
                elements[index] = "{" + elements[index];
            }
        }

        return elements;
    }
}
