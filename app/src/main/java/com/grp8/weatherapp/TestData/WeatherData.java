package com.grp8.weatherapp.TestData;


import java.security.Timestamp;
import java.util.Random;

/**
 * Created by lbirk on 21-10-2016.
 */

public class WeatherData {

    private long timeStamp = 0;
    private int rainFall = 0;
    private int airHum = 0;
    private  int windDir = 0;
    private int windSpeed = 0;
    private int lux = 0;
    private int airTemp = 0;
    private int airPressure = 0;


    public void generateRandomData()
    {
        timeStamp = System.currentTimeMillis();

        Random r = new Random();

        rainFall = r.nextInt(50-0)+0;
        airHum = r.nextInt(100-20)+20;
        windDir = r.nextInt(4-0)+4;
        windSpeed=r.nextInt(20-0)+0;
        lux = r.nextInt(1200-0)+0;
        airTemp = r.nextInt(50-0)+0;
        airPressure = r.nextInt(20-0)+0;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getRainFall() {
        return rainFall;
    }

    public int getAirHum() {
        return airHum;
    }

    public int getWindDir() {
        return windDir;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public int getLux() {
        return lux;
    }

    public int getAirTemp() {
        return airTemp;
    }

    public int getAirPressure() {
        return airPressure;
    }

}
