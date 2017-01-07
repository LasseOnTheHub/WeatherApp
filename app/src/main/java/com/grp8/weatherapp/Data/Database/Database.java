package com.grp8.weatherapp.Data.Database;

import android.content.Context;
import android.content.ContentValues;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.grp8.weatherapp.Data.Database.tables.StationsTable;
import com.grp8.weatherapp.Data.Database.tables.ReadingsTable;

/*
 * Created by Thomas on 07-Jan-17.
 */
public class Database extends SQLiteOpenHelper
{
    public final static int    DATABASE_VERSION = 1;
    public final static String DATABASE_NAME    = "CLAFIS_DATABASE_CACHE";

    private Context applicationContext;

    public Database(Context appContext)
    {
        super(appContext, DATABASE_NAME, null, DATABASE_VERSION);

        this.applicationContext = appContext;
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        System.out.println("Creating SQLite database " + DATABASE_NAME + ", version: " + DATABASE_VERSION);

        database.execSQL(StationsTable.CREATE_QUERY);
        database.execSQL(ReadingsTable.CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int previousVersion, int nextVersion)
    {
        database.execSQL(StationsTable.DROP_QUERY);
        database.execSQL(StationsTable.CREATE_QUERY);

        database.execSQL(ReadingsTable.DROP_QUERY);
        database.execSQL(ReadingsTable.CREATE_QUERY);
    }

    public Cursor read(String sql)
    {
        return this.read(sql, null);
    }

    public Cursor read(String sql, String[] params)
    {
        return this.getReadableDatabase().rawQuery(sql, params);
    }

    public void insert(String table, ContentValues values)
    {
        this.getWritableDatabase().insert(table, null, values);
    }

    public void reset()
    {
        System.out.println("Resetting SQLite database " + DATABASE_NAME);

        this.getWritableDatabase().execSQL(StationsTable.RESET_QUERY);
        this.getWritableDatabase().execSQL(ReadingsTable.RESET_QUERY);
    }

    /**
     * Deletes the entire SQLite database.
     */
    public void destroy()
    {
        System.out.println("Removing SQLite database " + DATABASE_NAME);

        this.applicationContext.deleteDatabase(DATABASE_NAME);
    }
}
