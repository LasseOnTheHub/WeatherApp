package com.grp8.weatherapp.Logic.Formatters;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

public class DegreeAxisValueFormatter implements IAxisValueFormatter
{
    private final DecimalFormat mFormat;

    public DegreeAxisValueFormatter() {
        mFormat = new DecimalFormat("###.#");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value) + (char) 0x00B0+"C";
    }
}
