package com.grp8.weatherapp.Data;

import android.content.Context;

import com.grp8.weatherapp.Data.API.APIDataProvider;
import com.grp8.weatherapp.Data.Database.Database;
import com.grp8.weatherapp.Data.Mappers.DataReadingMapper;
import com.grp8.weatherapp.Data.Mappers.StationMapper;

/*
 * Created by t_bit on 05-01-2017.
 */
public class DataRepositoryFactory
{
    private static DataRepository instance;

    public static DataRepository build(Context appContext)
    {
        if(instance == null)
        {
            instance = new DataRepository(new APIDataProvider(), new Database(appContext), new StationMapper(), new DataReadingMapper(), appContext);
        }

        return instance;
    }
}
