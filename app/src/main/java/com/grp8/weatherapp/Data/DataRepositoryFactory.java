package com.grp8.weatherapp.Data;

import com.grp8.weatherapp.Data.API.APIDataProvider;
import com.grp8.weatherapp.Data.Mappers.DataReadingMapper;
import com.grp8.weatherapp.Data.Mappers.StationMapper;

/**
 * Created by t_bit on 05-01-2017.
 */
public class DataRepositoryFactory
{
    private static DataRepository instance;

    public static DataRepository build()
    {
        if(instance == null)
        {
            instance = new DataRepository(new APIDataProvider(), new StationMapper(), new DataReadingMapper());
        }

        return instance;
    }
}
