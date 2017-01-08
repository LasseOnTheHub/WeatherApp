package com.grp8.weatherapp.mappers;

import com.grp8.weatherapp.Data.Mappers.DataReadingMapper;
import com.grp8.weatherapp.Data.Mappers.IListableMapper;
import com.grp8.weatherapp.Entities.Data.Air;
import com.grp8.weatherapp.Entities.Data.Soil;
import com.grp8.weatherapp.Entities.Data.Wind;
import com.grp8.weatherapp.Entities.DataReading;
import com.grp8.weatherapp.Entities.Station;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by t_bit on 05-01-2017.
 */
public class DataReadingMapperTest
{
    @Test
    public void canMapSingleDataReading() throws Exception
    {
        IListableMapper<DataReading> mapper = new DataReadingMapper();

        String payload = "{\"id\":1,\"timestamp\":946684800,\"rfaddress\":1,\"rainfall\":0.0,\"relativeAirHumidity\":10,\"windDirection\":0,\"windSpeed\":0.0,\"lux\":0.0,\"airTemp\":10,\"deviceID\":1,\"airPressure\":1000,\"soilMoistureOne\":0,\"soilMoistureTwo\":0,\"soilMoistureThree\":0,\"soilMoistureFour\":0,\"soilTemperatureOne\":0,\"soilTemperatureTwo\":0,\"soilTemperatureThree\":0,\"soilTemperatureFour\":0,\"leafWetness\":0}";

        DataReading expected = new DataReading(1, 1, 946684800, 0.0, new Air(1000, 10, 10), new Wind(0.0, 0), new Soil(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}));
        DataReading result   = mapper.map(payload);

        assertEquals(expected.getID(),        result.getID());
        assertEquals(expected.getTimestamp(), result.getTimestamp());

        assertEquals(expected.getRainfall(), result.getRainfall(), 0.01);

        assertEquals(expected.getAirReadings().getPressure(),    result.getAirReadings().getPressure());
        assertEquals(expected.getAirReadings().getHumidity(),    result.getAirReadings().getHumidity());
        assertEquals(expected.getAirReadings().getTemperature(), result.getAirReadings().getTemperature(), 0.01);

        assertEquals(expected.getWindReadings().getSpeed(),     result.getWindReadings().getSpeed(), 0.01);
        assertEquals(expected.getWindReadings().getDirection(), result.getWindReadings().getDirection());

        assertArrayEquals(expected.getSoilReadings().getMoisture(),    result.getSoilReadings().getMoisture());
        assertArrayEquals(expected.getSoilReadings().getTemperature(), result.getSoilReadings().getTemperature());
    }

    @Test
    public void canMapMultipleDataReading() throws Exception
    {
        IListableMapper<DataReading> mapper = new DataReadingMapper();

        String[] payloads = new String[2];

        payloads[0] = "{\"id\":1,\"timestamp\":946684800,\"rfaddress\":1,\"rainfall\":0.0,\"relativeAirHumidity\":10,\"windDirection\":0,\"windSpeed\":0.0,\"lux\":0.0,\"airTemp\":10,\"deviceID\":1,\"airPressure\":1000,\"soilMoistureOne\":0,\"soilMoistureTwo\":0,\"soilMoistureThree\":0,\"soilMoistureFour\":0,\"soilTemperatureOne\":0,\"soilTemperatureTwo\":0,\"soilTemperatureThree\":0,\"soilTemperatureFour\":0,\"leafWetness\":0}";
        payloads[1] = "{\"id\":2,\"timestamp\":946684800,\"rfaddress\":2,\"rainfall\":0.0,\"relativeAirHumidity\":20,\"windDirection\":0,\"windSpeed\":0.0,\"lux\":0.0,\"airTemp\":20,\"deviceID\":2,\"airPressure\":1000,\"soilMoistureOne\":0,\"soilMoistureTwo\":0,\"soilMoistureThree\":0,\"soilMoistureFour\":0,\"soilTemperatureOne\":0,\"soilTemperatureTwo\":0,\"soilTemperatureThree\":0,\"soilTemperatureFour\":0,\"leafWetness\":0}";

        List<DataReading> results = mapper.map(payloads);

        assertEquals(results.size(), 2);

        for(DataReading result : results)
        {
            assertTrue(result != null);
        }

    }
}
