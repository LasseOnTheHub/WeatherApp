package com.grp8.weatherapp;

import com.grp8.weatherapp.Data.DataRepository;
import com.grp8.weatherapp.Data.DataRepositoryFactory;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by t_bit on 05-01-2017.
 */
public class DataRepositoryFactoryTest
{

    @Test
    public void canCreateAndRetainInstance() throws Exception
    {
        DataRepository first  = DataRepositoryFactory.build();
        DataRepository second = DataRepositoryFactory.build();

        assertEquals(first, second);
    }
}
