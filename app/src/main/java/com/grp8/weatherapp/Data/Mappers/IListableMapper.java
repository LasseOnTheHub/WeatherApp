package com.grp8.weatherapp.Data.Mappers;

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
    public List<T> map(String[] collection);

    /**
     * Maps a single instance of the specified type.
     *
     * @param json A JSON string to fetch instance field values from.
     *
     * @return An instance of the specified type.
     */
    public T map(String json);
}
