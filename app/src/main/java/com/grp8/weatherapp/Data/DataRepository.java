package com.grp8.weatherapp.Data;

import com.grp8.weatherapp.Data.API.IDataProvider;
import com.grp8.weatherapp.Data.API.Requests.APIStationRequest;

import com.grp8.weatherapp.Data.Mappers.IListableMapper;
import com.grp8.weatherapp.Data.Mappers.IMapper;

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

    private IMapper<User> userMapper;
    private IListableMapper<Station>     stationMapper;
    private IListableMapper<DataReading> readingMapper;

    private IDataProvider provider;

    public DataRepository(IDataProvider provider, IMapper<User> userMapper, IListableMapper<Station> stationMapper, IListableMapper<DataReading> readingMapper)
    {
        this.provider = provider;

        this.userMapper    = userMapper;
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

        System.out.println(payload);

        return null; // TODO: Add return value
    }

    /**
     * Fetches a specific station associated with the current user and specified station ID
     *
     * @param stationID CLAFIS station ID
     */
    public Station getStation(int stationID)
    {
        String payload = this.provider.fetch(new APIStationRequest(this.user.getID(), stationID));

        System.out.println(payload);

        return null; // TODO: Add return value
    }

    /**
     * Fetches the latest station data reading associated with the specified station ID
     *
     * @param stationID CLAFIS station ID
     */
    public List<DataReading> getStationData(int stationID)
    {
        return null; // TODO: Add return value
    }

    /**
     * TODO: What is this used for?
     *
     * @param stationID CLAFIS
     * @param date
     */
    public void getStationData(int stationID, Date date)
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
    public List<DataReading> getStationData(int stationID, Date start, Date end)
    {
        if(start.compareTo(end) > 0)
        {
            throw new IllegalStateException("Start of date range cannot be after end of the range.");
        }

        return null; // TODO: Add return value
    }
}
