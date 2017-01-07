package com.grp8.weatherapp.Data.Database.tables;

/*
 * Created by Thomas on 07-Jan-17.
 */
public class ReadingsTable
{
    public final static String TABLE_NAME = "readings";

    public final static String COLUMN_ID = "id";
    public final static String COLUMN_STATION_ID = "stationID";
    public final static String COLUMN_TIMESTAMP = "timestamp";
    public final static String COLUMN_JSON = "json";

    public final static int COLUMN_POSITION_ID = 0;
    public final static int COLUMN_POSITION_STATION_ID = 1;
    public final static int COLUMN_POSITION_TIMESTAMP = 2;
    public final static int COLUMN_POSITION_JSON = 3;

    public final static String SELECT_ALL_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STATION_ID + " = ?";
    public final static String SELECT_LATEST_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_TIMESTAMP + " = (SELECT MAX(" + COLUMN_TIMESTAMP + ") FROM " + TABLE_NAME + " WHERE " + COLUMN_STATION_ID + " = ?) LIMIT 1";
    public final static String SELECT_SPECIFIC_FROM_ID_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    public final static String SELECT_BETWEEN_TIMESTAMPS_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STATION_ID + " = ? AND " + COLUMN_TIMESTAMP + " BETWEEN ? AND ?";

    public final static String CREATE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_STATION_ID + " INTEGER, " + COLUMN_TIMESTAMP + " TIMESTAMP, " + COLUMN_JSON + " VARCHAR)";
    public final static String RESET_QUERY  = "DELETE FROM " + TABLE_NAME;
    public final static String DROP_QUERY   = "DROP TABLE " + TABLE_NAME;
}
