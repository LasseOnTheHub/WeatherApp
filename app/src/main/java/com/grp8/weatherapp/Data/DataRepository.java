package com.grp8.weatherapp.Data;

import com.grp8.weatherapp.Data.API.IDataProvider;
import com.grp8.weatherapp.Data.API.Requests.APIStationRequest;

import java.util.Date;

/**
 * Created by Thomas on 03-Jan-17.
 */
public class DataRepository
{
    private IDataProvider provider;

    public DataRepository(IDataProvider provider)
    {
        this.provider = provider;
    }

    public void setUser() //TODO: Add user entity as parameter
    {

    }

    public boolean authorize(int userID)
    {
        return true;
    }

    /**
     * Fetches all stations associated with the specified user
     *
     * @param userID CLAFIS user ID
     */
    public void getStations(int userID) //TODO: Replace return type with proper entity
    {
        String payload = this.provider.fetch(new APIStationRequest(userID));

        System.out.println(payload);
    }

    /**
     * Fetches a specific station associated with the specified user and station ID
     *
     * @param userID    CLAFIS user ID
     * @param stationID CLAFIS station ID
     */
    public void getStation(int userID, int stationID) //TODO: Replace return type with proper entity
    {
        String payload = this.provider.fetch(new APIStationRequest(userID, stationID));

        System.out.println(payload);
    }

    /**
     * Fetches the latest station data reading associated with the specified station ID
     *
     * @param stationID CLAFIS station ID
     */
    public void getStationData(int stationID) //TODO: Replace return type with proper entity
    {

    }


    /**
     * TODO: What is this used for?
     *
     * @param stationID CLAFIS
     * @param date
     */
    public void getStationData(int stationID, Date date) //TODO: Replace return type with proper entity
    {

    }

    /**
     * Fetches all data reading with the specified date range associated with the
     * specified station ID
     *
     * @param stationID CLAFIS station ID
     * @param start     Beginning of date range
     * @param end       End of date range
     */
    public void getStationData(int stationID, Date start, Date end) //TODO: Replace return type with proper entity
    {
        if(start.compareTo(end) > 0)
        {
            throw new IllegalStateException("Start of date range cannot be after end of the range.");
        }
    }
}
