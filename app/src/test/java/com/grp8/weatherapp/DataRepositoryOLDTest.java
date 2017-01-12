package com.grp8.weatherapp;

import com.grp8.weatherapp.Data.API.APIDataProvider;
import com.grp8.weatherapp.Data.API.Requests.APIStationRequest;

import org.junit.Test;

/**
 * Created by Thomas on 03-Jan-17.
 */

public class DataRepositoryOLDTest
{
    @Test
    public void canSplitPayload() throws Exception
    {
        APIDataProvider provider = new APIDataProvider();

        String payload = provider.fetch(new APIStationRequest(5));

        String[] elements = payload.substring(1, payload.length() - 1).split(",\\{$");

        for(String element : elements)
        {
            if(!element.substring(0, 1).equals("{"))
            {
                element = '{' + element;
            }

            System.out.println(element);
        }

    }
}
