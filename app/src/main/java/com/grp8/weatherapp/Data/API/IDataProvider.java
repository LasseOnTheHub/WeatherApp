package com.grp8.weatherapp.Data.API;

import com.grp8.weatherapp.Data.API.Requests.APIRequest;

/*
 * Created by Thomas on 03-Jan-17.
 */
public interface IDataProvider
{
    public String fetch(APIRequest request);
}
