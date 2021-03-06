package com.grp8.weatherapp.Data.Mappers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/*
 * Created by Henrik on 02-01-2017.
 */
public interface IListableMapper<T>
{
    /**
     * Maps multiple instances of the specified type into a list.
     *
     * @param collection An array of JSON strings to fetch instance field values from.
     *
     * @return A list of instances of the specified type.
     */
    List<T> map(JSONArray collection);

    /**
     * Maps a single instance of the specified type.
     *
     * @param json A JSON string to fetch instance field values from.
     *
     * @return An instance of the specified type.
     */
    T map(JSONObject json);
}
