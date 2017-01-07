package com.grp8.weatherapp.api;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.grp8.weatherapp.Data.API.Requests.APIDataReadingRequest;
import com.grp8.weatherapp.Data.API.Requests.APIStationRequest;
import com.grp8.weatherapp.Data.API.Requests.APIRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Thomas on 03-Jan-17.
 */
public class APIRequestTest
{
    private static final int    USER_ID      = 5;
    private static final String BASE_ADDRESS = "130.226.195.167";
    private static final int    BASE_PORT    = 8080;
    private static final String BASE_URL     = "http://" + BASE_ADDRESS + ":" + BASE_PORT + "/CLAFIS-REST-API/api/v1/" + USER_ID;

    @Test
    @Before
    public void canCreateStationRequestInstance() throws Exception
    {
        assertNotNull(new APIStationRequest(USER_ID, 1));
    }

    @Test
    @Before
    public void canCreateDataReadingRequestInstance() throws Exception
    {
        assertNotNull(new APIDataReadingRequest(USER_ID, 1));
        assertNotNull(new APIDataReadingRequest(USER_ID, 1, new Date(), new Date()));
    }

    @Test
    public void canCreateAllStationsRequest() throws Exception
    {
        APIRequest request = new APIStationRequest(USER_ID);

        assertEquals(BASE_URL + "/devices", request.build());
    }

    @Test
    public void canCreateSpecificStationRequest() throws Exception
    {
        APIRequest request = new APIStationRequest(USER_ID, 1);

        assertEquals(BASE_URL + "/device/1", request.build());
    }

    @Test
    public void canCreateLatestDataReadingsRequest() throws Exception
    {
        APIRequest request = new APIDataReadingRequest(USER_ID, 1);

        SimpleDateFormat df  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar         cal = Calendar.getInstance();

        String end = df.format(cal.getTime());

        cal.add(Calendar.HOUR_OF_DAY, -1);
        String start = df.format(cal.getTime());

        assertEquals(BASE_URL + "/weather-station/1?endDate=" + end.replaceAll(" ", "%20") + "&startDate=" + start.replaceAll(" ", "%20"), request.build());
    }

    @Test
    public void canCreateIncreaseIntervalForLatestDataReadingRequest() throws Exception
    {
        APIDataReadingRequest request = new APIDataReadingRequest(USER_ID, 1);

        SimpleDateFormat df  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar         cal = Calendar.getInstance();

        String end = df.format(cal.getTime()).replaceAll(" ", "%20");

        cal.add(Calendar.HOUR_OF_DAY, -1);
        Date minusOneHour = cal.getTime();

        cal.add(Calendar.HOUR_OF_DAY, -1);
        Date minusTwoHours = cal.getTime();

        cal.add(Calendar.HOUR_OF_DAY, -2);
        Date minusFourHours = cal.getTime();

        assertEquals("Interval: -1 hour", BASE_URL + "/weather-station/1?endDate=" + end + "&startDate=" + df.format(minusOneHour).replaceAll(" ", "%20"), request.build());

        request.increaseBackwardsReadingDateInterval();
        assertEquals("Interval: -2 hours", BASE_URL + "/weather-station/1?endDate=" + end + "&startDate=" + df.format(minusTwoHours).replaceAll(" ", "%20"), request.build());

        request.increaseBackwardsReadingDateInterval();
        assertEquals("Interval: -4 hours", BASE_URL + "/weather-station/1?endDate=" + end + "&startDate=" + df.format(minusFourHours).replaceAll(" ", "%20"), request.build());
    }

    @Test
    public void canCreateSpecificDataReadingRequestWithDateRanges() throws Exception
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date start = df.parse("2016-11-01 00:00:00");
        Date end   = df.parse("2016-11-14 00:00:00");

        APIRequest request = new APIDataReadingRequest(USER_ID, 1, start, end);

        assertEquals(BASE_URL + "/weather-station/1?endDate=2016-11-14%2000:00:00&startDate=2016-11-01%2000:00:00", request.build());
    }
}
