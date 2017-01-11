package com.grp8.weatherapp.Data.API.Requests;

import android.util.Log;

import com.grp8.weatherapp.SupportingFiles.Environment;
import com.grp8.weatherapp.SupportingFiles.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

        /*
         * 2016-11-14 23:59:59 in UNIX time converted to milliseconds (in Denmark)
         */
        long validEndDate = 1479164399 * 1000L;

        Date forcedEndDate   = new Date(validEndDate);
        Date forcedStartDate = new Date(validEndDate - (end.getTime() - start.getTime()));

        Log.d(TAG, "Forced start date " + forcedStartDate.toString());
        Log.d(TAG, "Forced end date " + forcedEndDate.toString());

        this.addParameter("startDate", df.format(forcedStartDate));
        this.addParameter("endDate",   df.format(forcedEndDate));
    }

    public void increaseBackwardsReadingDateInterval()
    {
        /*
         * This assumes the starting interval was -1 hour when called for the first time.
         */
        this.backwardsReadingDateInterval = (this.backwardsReadingDateInterval * Environment.API_RETRY_MULTIPLICATION_FACTOR);

        if(Utils.isEmulator())
        {
            Log.d(TAG, "Increasing backwards reading interval to: -" + this.backwardsReadingDateInterval + " hours");
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
