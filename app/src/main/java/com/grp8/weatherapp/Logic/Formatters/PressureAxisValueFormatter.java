package com.grp8.weatherapp.Logic.Formatters;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by lbirk on 17-01-2017.
 */

public class PressureAxisValueFormatter implements IAxisValueFormatter
{
    private DecimalFormat mFormat;

    public PressureAxisValueFormatter() {
        mFormat = new DecimalFormat("####");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value) + " hPa";
    }
}
