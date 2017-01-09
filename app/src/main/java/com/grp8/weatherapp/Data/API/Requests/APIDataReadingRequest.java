package com.grp8.weatherapp.Data.API.Requests;

import com.grp8.weatherapp.SupportingFiles.Environment;
import com.grp8.weatherapp.SupportingFiles.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
 * Created by Thomas on 03-Jan-17.
 */
public class APIDataReadingRequest extends APIRequest
{
    private int backwardsReadingDateInterval = 1;

    /**
     * Fetches the latest data reading from the specified station
     *
     * @param userID    CLAFIS user ID
     * @param stationID CLAFIS station ID
     */
    public APIDataReadingRequest(int userID, int stationID)
    {
        this(userID, stationID, createInitialBackwardsReadingDateInterval(), new Date());
    }

    /**
     * Fetches all data readings from the specified station within the
     * specified date range.
     *
     * @param userID    CLAFIS user ID
     * @param stationID CLAFIS station ID
     * @param start     Start date of date range
     * @param end       End date of date range
     */
    public APIDataReadingRequest(int userID, int stationID, Date start, Date end)
    {
        super("/" + userID + "/weather-station/" + stationID);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        this.addParameter("startDate", df.format(start));
        this.addParameter("endDate",   df.format(end));
    }

    public void increaseBackwardsReadingDateInterval()
    {
        /*
         * This assumes the starting interval was -1 hour when called for the first time.
         */
        this.backwardsReadingDateInterval = (this.backwardsReadingDateInterval * Environment.API_RETRY_MULTIPLICATION_FACTOR);

        if(Utils.isEmulator())
        {
            System.out.println("[API DEBUG]: Increasing backwards reading interval to: -" + this.backwardsReadingDateInterval + " hours");
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -this.backwardsReadingDateInterval);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        this.addParameter("startDate", df.format(cal.getTime()));
    }

    private static Date createInitialBackwardsReadingDateInterval()
    {
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.HOUR_OF_DAY, -1);

        return cal.getTime();
    }
}
