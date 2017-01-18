package com.grp8.weatherapp.Logic.Formatters;

import android.content.Context;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.grp8.weatherapp.Logic.Converters.TemperatureConverter;
import com.grp8.weatherapp.Logic.SettingsManager;

import java.text.DecimalFormat;

public class DegreeAxisValueFormatter implements IAxisValueFormatter
{
    private final DecimalFormat mFormat;
    private Context context;

    public DegreeAxisValueFormatter(Context context) {
        mFormat = new DecimalFormat("###.#");
        this.context = context;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value) + SettingsManager.getTempUnit(context);
    }
}
