package com.grp8.weatherapp.Data.API.Exceptions;

/**
 * Created by Thomas on 03-Jan-17.
 */

public class APIException extends RuntimeException
{
    public APIException(String message)
    {
        this("An API exception has occurred", null);
    }

    public APIException(Throwable previous)
    {
       this("API exception: " + previous.getMessage(), previous);
    }

    APIException(String message, Throwable previous)
    {
        super(message, previous);
    }
}
