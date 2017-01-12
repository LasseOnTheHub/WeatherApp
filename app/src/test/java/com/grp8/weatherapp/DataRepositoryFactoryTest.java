package com.grp8.weatherapp;

import com.grp8.weatherapp.Data.DataRepositoryFactory;
import com.grp8.weatherapp.Data.IDataRepository;

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
        IDataRepository first  = DataRepositoryFactory.build(null);
        IDataRepository second = DataRepositoryFactory.build(null);

        assertEquals(first, second);
    }
}
