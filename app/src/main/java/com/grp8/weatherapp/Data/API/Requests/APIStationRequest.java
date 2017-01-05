package com.grp8.weatherapp.Data.API.Requests;

/**
 * Created by Thomas on 03-Jan-17.
 */

public class APIStationRequest extends APIRequest
{
    /**
     * Creates a request for all stations associated with the user.
     *
     * @param userID ClAFIS user ID
     */
    public APIStationRequest(int userID)
    {
        super("/" + userID + "/devices");
    }

    /**
     * Creates a request for a specific station associated with the user.
     *
     * @param userID    ClAFIS user ID
     * @param stationID CLAFIS station ID
     */
    public APIStationRequest(int userID, int stationID)
    {
        super("/" + userID + "/device/" + stationID);
    }
}
