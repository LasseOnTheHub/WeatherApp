package com.grp8.weatherapp.Data;

import android.content.Context;

import com.grp8.weatherapp.Data.API.APIDataProvider;
import com.grp8.weatherapp.Data.Mappers.IListableMapper;
import com.grp8.weatherapp.Entities.Data.Air;
import com.grp8.weatherapp.Entities.Data.Soil;
import com.grp8.weatherapp.Entities.Data.Wind;
import com.grp8.weatherapp.Entities.DataReading;
import com.grp8.weatherapp.Entities.Station;
import com.grp8.weatherapp.Logic.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

/*
 * Created by Thomas on 14-Jan-17.
 */
public class FallbackDataRepository extends DataRepository
{
    private Context context;

    public FallbackDataRepository(Context context, APIDataProvider api, IListableMapper<Station> stations, IListableMapper<DataReading> readings)
    {
        super(api, stations, readings);

        this.context = context;
    }

    @Override
    public int getStationCount()
    {
        if(Utils.isNetworkAvailable(this.context))
        {
            return super.getStationCount();
        }

        return 3;
    }

    @Override
    public List<Station> getStations()
    {
        if(Utils.isNetworkAvailable(this.context))
        {
            return super.getStations();
        }

        Map<Station, DataReading> stations  = new Hashtable<>();
        Map<Integer, Double[]>    positions = new Hashtable<>();

        positions.put(0, new Double[]{ 60.426426, 24.377971 });
        positions.put(1, new Double[]{ 55.732282, 12.39504 });
        positions.put(2, new Double[]{ 55.732282, 12.39504 });

        for(int index = 0; index < this.getStationCount(); index++)
        {
            int      id  = index + 1;
            Double[] pos = positions.get(index);

            stations.put(new Station(id, 1, "Demo station " + (index + 1), pos[0], pos[1]), getStationData(id));
        }

        super.addToCache(stations);

        return new ArrayList<>(stations.keySet());
    }

    @Override
    public DataReading getStationData(int station)
    {
        if(Utils.isNetworkAvailable(this.context))
        {
            return super.getStationData(station);
        }

        return generate(1, station, new Date(System.currentTimeMillis()), new Random());
    }

    @Override
    public List<DataReading> getStationData(int station, Date start, Date end)
    {
        if(Utils.isNetworkAvailable(this.context))
        {
            return super.getStationData(station, start, end);
        }

        List<DataReading> readings = new ArrayList<>();

        Random rand = new Random();

        long time = System.currentTimeMillis();

        for(int index = 1; index <= 20; index++)
        {
            readings.add(generate(index, station, new Date(time), rand));

            time -= 12 * 60 * 60 * 1000;
        }

        return readings;
    }

    private DataReading generate(int id, int station, Date date, Random rand)
    {
        double rain = 10 - rand.nextInt(10);

        Air  air  = new Air(1100 - rand.nextInt(200), 60 - rand.nextInt(25), 10 - rand.nextInt(8));
        Wind wind = new Wind(25.0 - (double) rand.nextInt(25), 0);
        Soil soil = new Soil(new int[] {0, 0, 0, 0}, new int[] {0, 0, 0, 0});

        return new DataReading(id, station, date, rain, air, wind, soil);
    }
}
