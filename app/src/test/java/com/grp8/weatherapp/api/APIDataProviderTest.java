package com.grp8.weatherapp.api;

import com.grp8.weatherapp.Data.API.APIDataProvider;
import com.grp8.weatherapp.Data.API.Requests.APIStationRequest;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/*
 * Created by Thomas on 03-Jan-17.
 */
public class APIDataProviderTest
{
    @Test
    @Before
    public void canCreateInstance() throws Exception
    {
        APIDataProvider provider = new APIDataProvider();

        assertNotNull(provider);
    }

    @Test
    public void canFetchSingleStation() throws Exception
    {
        String expected = "{\"id\":2,\"deviceType\":1,\"notes\":\"Weather station located at DTU (5)\",\"latitude\":55.732282,\"longitude\":12.39504}";

        APIDataProvider provider = new APIDataProvider();

        String payload = provider.fetch(new APIStationRequest(5, 2));

        assertNotNull(payload);
        assertEquals(expected, payload);
    }
}

