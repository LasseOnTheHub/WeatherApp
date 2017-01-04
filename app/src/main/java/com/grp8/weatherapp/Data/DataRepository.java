package com.grp8.weatherapp.Data;

import com.grp8.weatherapp.Data.API.Requests.APIDataReadingRequest;
import com.grp8.weatherapp.Data.API.Requests.APIStationRequest;
import com.grp8.weatherapp.Data.API.IDataProvider;

import com.grp8.weatherapp.Data.Mappers.IListableMapper;

import com.grp8.weatherapp.Entities.DataReading;
import com.grp8.weatherapp.Entities.Station;
import com.grp8.weatherapp.Entities.User;

import java.util.Date;
import java.util.List;

/**
 * Created by Thomas on 03-Jan-17.
 */
public class DataRepository
{
    private User user;

    private IListableMapper<Station>     stationMapper;
    private IListableMapper<DataReading> readingMapper;

    private IDataProvider provider;

    public DataRepository(IDataProvider provider, IListableMapper<Station> stationMapper, IListableMapper<DataReading> readingMapper)
    {
        this.provider = provider;

        this.stationMapper = stationMapper;
        this.readingMapper = readingMapper;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public boolean authorize(int userID)
    {
        return true;
    }

    /**
     * Fetches all stations associated with the current user
     */
    public List<Station> getStations()
    {
        String payload = this.provider.fetch(new APIStationRequest(this.user.getID()));

        return this.stationMapper.map(this.split(payload)); // TODO: Add caching
    }

    /**
     * Fetches a specific station associated with the current user and specified station ID
     *
     * @param stationID CLAFIS station ID
     */
    public Station getStation(int stationID)
    {
        String payload = this.provider.fetch(new APIStationRequest(this.user.getID(), stationID));

        return (Station) this.stationMapper.map(this.split(payload)); // TODO: Add caching
    }

    /**
     * Fetches the latest station data reading associated with the specified station ID
     *
     * @param stationID CLAFIS station ID
     */
    public DataReading getStationData(int stationID)
    {
        APIDataReadingRequest request = new APIDataReadingRequest(this.user.getID(), stationID);

        int celling = 3;
        int counter = 1;

        String payload = "[]";

        while(payload.equals("[]") && counter <= celling)
        {
            payload = this.provider.fetch(request);

            if(!payload.equals("[]"))
            {
                break;
            }

            request.increaseBackwardsReadingDateInterval();
            counter++;
        }

        return (DataReading) this.readingMapper.map(this.split(payload)); // TODO: Add caching
    }

    /**
     * @param stationID CLAFIS
     * @param date
     */
    //public void getStationData(int stationID, Date date) {}

    /**
     * Fetches all data reading with the specified date range associated with the
     * specified station ID
     *
     * @param stationID CLAFIS station ID
     * @param start     Beginning of date range
     * @param end       End of date range
     */
    public List<DataReading> getStationData(int stationID, Date start, Date end)
    {
        if(start.compareTo(end) > 0)
        {
            throw new IllegalStateException("Start of date range cannot be after end of the range.");
        }

        String payload = this.provider.fetch(new APIDataReadingRequest(this.user.getID(), stationID, start, end));

        return this.readingMapper.map(this.split(payload));
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
