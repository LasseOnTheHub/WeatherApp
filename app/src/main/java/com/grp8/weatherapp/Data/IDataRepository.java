package com.grp8.weatherapp.Data;

/*
 * Created by Thomas on 12-Jan-17.
 */
import com.grp8.weatherapp.Entities.DataReading;
import com.grp8.weatherapp.Entities.Station;

import java.util.Date;
import java.util.List;

public interface IDataRepository
{
    /**
     * Attempts to authorize the user.
     *
     * @param user A CLAFIS user ID.
     *
     * @return Returns TRUE if the authorization was successful, FALSE otherwise.
     */
    public boolean authorize(int user);

    /**
     * Sets the current user.
     *
     * @param user A CLAFIS user ID.
     */
    public void setUser(int user);

    /**
     * Refreshes the data source.
     */
    public void refresh();

    /**
     * Fetches a specific station associated with the current user and specified station ID
     *
     * @param id CLAFIS station ID
     */
    public Station getStation(int id);

    /**
     * Fetches all stations associated with the current user
     */
    public List<Station> getStations();

    /**
     * Returns the number of stations.
     *
     * @return Returns an integer.
     */
    public int getStationCount();

    /**
     * Fetches the latest station data reading associated with the specified station ID
     *
     * @param station CLAFIS station ID
     */
    public DataReading getStationData(int station);

    /**
     * Fetches all data reading with the specified date range associated with the
     * specified station ID
     *
     * @param station CLAFIS station ID
     * @param start   Beginning of date range
     * @param end     End of date range
     */
    public List<DataReading> getStationData(int station, Date start, Date end);
}
