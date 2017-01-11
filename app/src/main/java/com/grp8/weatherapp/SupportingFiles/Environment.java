package com.grp8.weatherapp.SupportingFiles;

/*
 * Created by Thomas on 09-Jan-17.
 */
public class Environment
{
    /*
     * 2016-11-14 23:59:59 in UNIX time converted to milliseconds (in Denmark)
     */
    public final static long  API_VALID_DATE_OFFSET = 1479164399 * 1000L;

    public final static int   API_CACHE_DATA_READING_MAX_SIZE    = 250;
    public final static float API_CACHE_DATA_READING_LOAD_FACTOR = 0.75F;

    public final static int API_MAXIMUM_NUMBER_OF_RETRIES = 5;
    public final static int API_RETRY_MULTIPLICATION_FACTOR = 3;
}
