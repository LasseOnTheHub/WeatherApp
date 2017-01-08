package com.grp8.weatherapp.Data.Database;

import android.content.ContentValues;
import android.database.Cursor;

import com.grp8.weatherapp.Data.Database.tables.ReadingsTable;
import com.grp8.weatherapp.Data.Mappers.IListableMapper;
import com.grp8.weatherapp.Entities.DataReading;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * Created by Thomas on 07-Jan-17.
 */

/**
 * Provides utility methods for working with data readings in the local
 * SQLite database.
 */
public class DataReadingDatabaseHelper
{
    private static final String DATE_FORMAT = "YYYY-MM-DD HH:mm";

    /**
     * Fetches all data readings associated with the specified station
     *
     * @param database  A database manager instance
     * @param mapper    A data reading mapper instance
     * @param station   A station id
     *
     * @return A list of data reading entities or an empty list of none exists.
     */
    public static List<DataReading> all(Database database, IListableMapper<DataReading> mapper, int station)
    {
        Cursor cursor = database.read(ReadingsTable.SELECT_ALL_QUERY, new String[] { Integer.toString(station) });

        return Convert(cursor, mapper);
    }

    public static DataReading latest(Database database, IListableMapper<DataReading> mapper, int station)
    {
        Cursor cursor = database.read(ReadingsTable.SELECT_LATEST_QUERY, new String[] { Integer.toString(station) });

        if(cursor.getCount() < 1)
        {
            cursor.close();
            return null;
        }

        String json = cursor.getString(ReadingsTable.COLUMN_POSITION_JSON);

        return mapper.map(json);
    }

    /**
     * Fetches a specific data reading corresponding the specified id.
     *
     * @param database A database manager instance
     * @param mapper   A data mapper instance
     * @param id       A data reading id
     *
     * @return A data reading entity or null of none exists.
     */
    public static DataReading fetch(Database database, IListableMapper<DataReading> mapper, int id)
    {
        Cursor cursor = database.read(ReadingsTable.SELECT_SPECIFIC_FROM_ID_QUERY, new String[] { Integer.toString(id) });

        if(cursor.getCount() < 1)
        {
            cursor.close();
            return null;
        }

        String json = cursor.getString(ReadingsTable.COLUMN_POSITION_JSON);

        return mapper.map(json);
    }

    /**
     * Fetches all data readings in the specified time range.
     *
     * @param database A database manager instance
     * @param mapper   A data reading mapper instance
     * @param station  A station id to select data readings from
     * @param from     The start of the date range
     * @param to       The end of the date range
     *
     * @return A list of data reading entities within the time range or an empty list if none exists.
     */
    public static List<DataReading> fetch(Database database, IListableMapper<DataReading> mapper, int station, Date from, Date to)
    {
        String[] params = new String[3];

        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);

        params[0] = Integer.toString(station);
        params[1] = df.format(from);
        params[2] = df.format(to);

        Cursor cursor = database.read(ReadingsTable.SELECT_BETWEEN_TIMESTAMPS_QUERY, params);

        return Convert(cursor, mapper);
    }

    /**
     * Adds a JSON payload of a data reading to the SQlite database.
     *
     * @param database  A database manager instance
     * @param id        A reading id
     * @param device    A station id corresponding to the station the data reading was taken
     * @param timestamp A timestamp of the data reading
     * @param json      The actual JSON payload.
     */
    public static void add(Database database, int id, int device, Date timestamp, String json)
    {
        ContentValues values = new ContentValues();

        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);

        values.put(ReadingsTable.COLUMN_ID, id);
        values.put(ReadingsTable.COLUMN_STATION_ID, device);
        values.put(ReadingsTable.COLUMN_TIMESTAMP, df.format(timestamp));
        values.put(ReadingsTable.COLUMN_JSON, json);

        database.insert(ReadingsTable.TABLE_NAME, values);
    }

    /**
     * Converts multiple SQLite cursor objects into DataReading instances and
     * closes the cursor.
     *
     * @param cursor A SQLite cursor instance.
     * @param mapper A data reading mapper instance.
     *
     * @return A list of data readings or an empty list if the cursor is empty.
     */
    private static List<DataReading> Convert(Cursor cursor, IListableMapper<DataReading> mapper)
    {
        int size = cursor.getCount();

        if(size < 1)
        {
            cursor.close();
            return new ArrayList<>();
        }

        String[] results = new String[size];

        for(int index = 0; index < size; index++)
        {
            cursor.moveToNext();
            results[index] = cursor.getString(ReadingsTable.COLUMN_POSITION_JSON);
        }

        cursor.close();

        return mapper.map(results);
    }
}
