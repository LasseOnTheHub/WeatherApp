package com.grp8.weatherapp.Data.API;

import com.grp8.weatherapp.Data.API.Requests.APIRequest;

/*
 * Created by Thomas on 03-Jan-17.
 */
interface IDataProvider
{
    String fetch(APIRequest request);
}
