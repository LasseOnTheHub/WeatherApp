package com.grp8.weatherapp.Data.API.Requests;

import com.grp8.weatherapp.Data.API.Exceptions.APIException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URI;
import java.net.URL;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thomas on 03-Jan-17.
 */
public abstract class APIRequest
{
    protected final static String TAG = "APIRequest";

    private static final String SERVER_ADDRESS = "130.226.195.167";
    private static final int    SERVER_PORT    = 8080;

    private String              path;
    private Map<String, String> parameters = new HashMap<>();

    protected APIRequest(String path)
    {
        this.path = path;
    }

    protected void addParameter(String key, String value)
    {
        this.parameters.put(key, value);
    }

    public String build()
    {
        String url = "http://" + SERVER_ADDRESS + ":" + SERVER_PORT + "/CLAFIS-REST-API/api/v1/" + this.path.replaceAll("^/", "");

        if(this.parameters.size() > 0)
        {
            Iterator iterator = this.parameters.entrySet().iterator();

            int index = 0;

            while(iterator.hasNext())
            {
                String    prefix = "&";
                Map.Entry pair   = (Map.Entry) iterator.next();

                if(index == 0)
                {
                    prefix = "?";
                }

                url += prefix + pair.getKey().toString() + "=" + pair.getValue().toString();

                index++;
            }
        }

        try
        {
            url = encode(url).toURL().toString();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();

            throw new APIException(ex);
        }

        return url;
    }

    private URI encode(String in) throws URISyntaxException, MalformedURLException
    {
        URL url = new URL(in);

        /*
         * This is a trick we can use in android to escape certain characters from URLs.
         */
        return new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
    }
}
