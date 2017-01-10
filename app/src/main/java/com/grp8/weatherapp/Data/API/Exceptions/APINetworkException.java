package com.grp8.weatherapp.Data.API.Exceptions;

/*
 * Created by Thomas on 09-Jan-17.
 */
public class APINetworkException extends APIException
{
    public APINetworkException()
    {
        super("Network is unreachable");
    }

    public APINetworkException(Exception exception)
    {
        super("Network is unreachable", exception);
    }
}
