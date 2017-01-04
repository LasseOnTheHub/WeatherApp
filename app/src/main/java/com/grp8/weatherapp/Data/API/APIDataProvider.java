package com.grp8.weatherapp.Data.API;

import com.grp8.weatherapp.Data.API.Requests.APIRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Thomas on 03-Jan-17.
 */
public class APIDataProvider implements IDataProvider
{
    public String fetch(APIRequest request)
    {
        HttpURLConnection connection;
        StringBuffer      payload = new StringBuffer();

        try
        {
            URL url = new URL(request.build());

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            System.out.println("[API DEBUG]: Sending GET request to: " + url.toString());

            int responseCode = connection.getResponseCode();

            System.out.println("[API DEBUG]: Response code: " + responseCode);

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
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return payload.toString();
    }
}
