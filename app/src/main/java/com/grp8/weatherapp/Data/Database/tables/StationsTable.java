package com.grp8.weatherapp.Data.Database.tables;

/*
 * Created by Thomas on 07-Jan-17.
 */
public class StationsTable
{
    public final static String TABLE_NAME = "stations";

    public final static String COLUMN_ID = "id";
    public final static String COLUMN_TYPE = "type";
    public final static String COLUMN_NOTES = "notes";
    public final static String COLUMN_LATITUDE = "latitude";
    public final static String COLUMN_LONGITUDE = "longitude";

    public final static int COLUMN_POSITION_ID = 0;
    public final static int COLUMN_POSITION_TYPE = 1;
    public final static int COLUMN_POSITION_NOTES = 2;
    public final static int COLUMN_POSITION_LATITUDE = 3;
    public final static int COLUMN_POSITION_LONGITUDE = 4;

    public final static String COLUMN_SELECT_ALL_QUERY      = "SELECT * FROM " + TABLE_NAME;
    public final static String COLUMN_SELECT_SPECIFIC_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";

    public final static String CREATE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("+ COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_TYPE + " INTEGER, " + COLUMN_NOTES + " VARCHAR, " + COLUMN_LATITUDE + " VARCHAR, " + COLUMN_LONGITUDE + " VARCHAR)";
    public final static String RESET_QUERY  = "DELETE FROM " + TABLE_NAME;
    public final static String DROP_QUERY   = "DROP TABLE " + TABLE_NAME;
}
