package com.grp8.weatherapp.mappers;

import com.grp8.weatherapp.Data.Mappers.IListableMapper;
import com.grp8.weatherapp.Data.Mappers.StationMapper;
import com.grp8.weatherapp.Entities.Station;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by t_bit on 05-01-2017.
 */
public class StationMapperTest
{
    @Test
    public void canMapSingleStation() throws Exception
    {
        IListableMapper<Station> mapper = new StationMapper();

        String  payload  = "{\"id\": 1,\"deviceType\": 1,\"notes\": \"notes\",\"latitude\": 10.0,\"longitude\": 20.0\n}";

        Station expected = new Station(1, 1, "notes", 10.0, 20.0);
        Station result   = mapper.map(payload);

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getLatitude(), result.getLatitude(), 0.01);
        assertEquals(expected.getLongitude(), result.getLongitude(), 0.01);
        assertEquals(expected.getNotes(), result.getNotes());
    }

    @Test
    public void canMapMultipleStations() throws Exception
    {
        IListableMapper<Station> mapper = new StationMapper();

        String[] payloads = new String[2];

        payloads[0] = "{\"id\": 1,\"deviceType\": 1,\"notes\": \"notes\",\"latitude\": 10.0,\"longitude\": 20.0\n}";
        payloads[1] = "{\"id\": 2,\"deviceType\": 1,\"notes\": \"notes\",\"latitude\": 20.0,\"longitude\": 30.0\n}";

        List<Station> results = mapper.map(payloads);

        assertEquals(results.size(), 2);

        for(Station result : results)
        {
            assertTrue(result != null);
        }
    }
}
