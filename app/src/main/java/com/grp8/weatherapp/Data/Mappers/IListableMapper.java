package com.grp8.weatherapp.Data.Mappers;

import java.util.List;

/**
 * Created by Henrik on 02-01-2017.
 */

public interface IListableMapper<T>{
    public List<T> map(List<String> json);
}
