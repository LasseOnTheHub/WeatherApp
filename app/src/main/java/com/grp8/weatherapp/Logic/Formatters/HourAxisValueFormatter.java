package com.grp8.weatherapp.Logic.Formatters;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/**
 * Created by lbirk on 12-01-2017.
 */

public class HourAxisValueFormatter implements IAxisValueFormatter
{

    private final long referenceTimestamp; // minimum timestamp in your data set
    private final DateFormat mDataFormat;
    private final Date mDate;

    public HourAxisValueFormatter(long referenceTimestamp) {
        this.referenceTimestamp = referenceTimestamp;
        this.mDataFormat = new SimpleDateFormat(" dd/MM ", Locale.ENGLISH);
        this.mDate = new Date();
        Log.d("ReferenceTid","Tiden sat i ValueFormatteren er: "+mDataFormat.format(referenceTimestamp*1000));
    }


    /**
     * Called when a value from an axis is to be formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * @param value the value to be formatted
     * @param axis  the axis the value belongs to
     * @return
     */
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // convertedTimestamp = originalTimestamp - referenceTimestamp
        long convertedTimestamp = (long) value;

        // Retrieve original timestamp
        long originalTimestamp = referenceTimestamp + convertedTimestamp;

        // Convert timestamp to hour:minute
        return getHour(originalTimestamp);
    }


    private String getHour(long timestamp){
        try{
            mDate.setTime(timestamp*1000);
            return mDataFormat.format(mDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }
}