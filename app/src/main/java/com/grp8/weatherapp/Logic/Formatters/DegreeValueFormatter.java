package com.grp8.weatherapp.Logic.Formatters;

import android.content.Context;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.grp8.weatherapp.Logic.Converters.TemperatureConverter;
import com.grp8.weatherapp.Logic.SettingsManager;

import java.text.DecimalFormat;

class DegreeValueFormatter implements IValueFormatter
{

    private final DecimalFormat mFormat;
    private Context context;

    public DegreeValueFormatter(Context context) {
        mFormat = new DecimalFormat("###.#");
        this.context = context;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value) + SettingsManager.getTempUnit(context);
    }
}
