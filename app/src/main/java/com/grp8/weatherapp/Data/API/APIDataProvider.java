package com.grp8.weatherapp.Data.API;

import android.util.Log;

import com.grp8.weatherapp.Data.API.Exceptions.APIException;
import com.grp8.weatherapp.Data.API.Exceptions.APINetworkException;
import com.grp8.weatherapp.Data.API.Requests.APIRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;

/*
 * Created by Thomas on 03-Jan-17.
 */
public class APIDataProvider implements IDataProvider
{
    private final static String TAG = "APIDataProvider";

    public String fetch(APIRequest request)
    {
        HttpURLConnection connection;
        StringBuffer      payload = new StringBuffer();

        try
        {
            URL url = new URL(request.build());

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            Log.d(TAG, "Sending GET request to: " + url.toString());

            int responseCode = connection.getResponseCode();

            Log.d(TAG, "Response code: " + responseCode);

            if(responseCode != 200)
            {
                throw new APIException("Cannot fetch information from API. Returned response code " + responseCode + " on url: " + url.toString());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;

            while((line = reader.readLine()) != null)
            {
                payload.append(line);
            }

            reader.close();
        }
        catch(SocketException ex)
        {
            throw new APINetworkException(ex);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return payload.toString();
    }
}
