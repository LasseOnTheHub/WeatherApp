package com.grp8.weatherapp.Logic;

/**
 * Created by lbirk on 02-10-2016.
 */
public class Authorizer {

    public boolean Authorize(int userid)
    {
        if (userid == 5)
        {
            return true;
        }
        return false;
    }

}