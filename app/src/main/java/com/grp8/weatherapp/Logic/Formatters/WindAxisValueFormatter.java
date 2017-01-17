package com.grp8.weatherapp.Logic.Formatters;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by lbirk on 17-01-2017.
 */

public class WindAxisValueFormatter implements IAxisValueFormatter
{
    private final DecimalFormat mFormat;

    public WindAxisValueFormatter() {
        mFormat = new DecimalFormat("####");
    }
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value) + " m/s";
    }
}
