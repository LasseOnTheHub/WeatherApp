package com.grp8.weatherapp.Data.Mappers;

import java.util.List;

/**
 * Created by Henrik on 02-01-2017.
 */
public interface IListableMapper<T> extends IMapper
{
    public List<T> map(String[] json);
}
