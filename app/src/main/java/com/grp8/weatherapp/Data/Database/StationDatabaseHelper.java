package com.grp8.weatherapp.Data.Database;

import android.content.ContentValues;
import android.database.Cursor;

import com.grp8.weatherapp.Data.Database.tables.StationsTable;
import com.grp8.weatherapp.Entities.Station;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Thomas on 07-Jan-17.
 */

/**
 * Provides utility methods for working with stations in the local
 * SQLite database.
 */
public class StationDatabaseHelper
{
    public static int count(Database database)
    {
        Cursor cursor = database.read(StationsTable.QUERY_SELECT_COUNT);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }

    /**
     * Fetches all stations.
     *
     * @param database A database manager instance
     *
     * @return A list of station entities or an empty list if none exists.
     */
    public static List<Station> all(Database database)
    {
        Cursor        cursor = database.read(StationsTable.QUERY_SELECT_ALL);
        List<Station> result = new ArrayList<>();

        if(cursor.getCount() == 0)
        {
            return result;
        }

        while(!cursor.isLast())
        {
            cursor.moveToNext();

            result.add(new Station(
                cursor.getInt(StationsTable.COLUMN_POSITION_ID),
                cursor.getInt(StationsTable.COLUMN_POSITION_TYPE),
                cursor.getString(StationsTable.COLUMN_POSITION_NOTES),

                Double.valueOf(cursor.getString(StationsTable.COLUMN_POSITION_LATITUDE)),
                Double.valueOf(cursor.getString(StationsTable.COLUMN_POSITION_LONGITUDE))
            ));
        }

        cursor.close();

        return result;
    }

    /**
     * Fetches a station associated with the specified id
     *
     * @param database A database manager instance.
     * @param id       A station id
     *
     * @return A station entity or null of none exists.
     */
    public static Station fetch(Database database, int id)
    {
        Cursor cursor = database.read(StationsTable.QUERY_SELECT_SPECIFIC, new String[] { Integer.toString(id) });

        if(cursor.getCount() == 0)
        {
            return null;
        }

        cursor.moveToFirst();

        Station result = new Station(
            cursor.getInt(0),
            cursor.getInt(1),
            cursor.getString(2),

            Double.valueOf(cursor.getString(3)),
            Double.valueOf(cursor.getString(4))
        );

        cursor.close();

        return result;
    }

    /**
     * Adds a station to the SQLite database.
     *
     * @param database A database manager instance.
     * @param station  A station entity.
     */
    public static void add(Database database, Station station)
    {
        ContentValues values = new ContentValues();

        values.put(StationsTable.COLUMN_ID,    station.getId());
        values.put(StationsTable.COLUMN_TYPE,  station.getType());
        values.put(StationsTable.COLUMN_NOTES, station.getNotes());

        values.put(StationsTable.COLUMN_LATITUDE,  station.getLatitude());
        values.put(StationsTable.COLUMN_LONGITUDE, station.getLongitude());

        database.insert(StationsTable.TABLE_NAME, values);
    }
}
