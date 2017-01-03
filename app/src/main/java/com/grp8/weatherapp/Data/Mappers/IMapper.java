package com.grp8.weatherapp.Data.Mappers;

/**
 * Created by Henrik on 02-01-2017.
 */
public interface IMapper<T> {

    public T map (String json);

}
