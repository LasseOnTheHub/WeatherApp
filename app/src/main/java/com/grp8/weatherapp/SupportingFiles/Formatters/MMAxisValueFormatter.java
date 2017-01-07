package com.grp8.weatherapp.SupportingFiles.Formatters;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

public class MMAxisValueFormatter implements IAxisValueFormatter
{

    private DecimalFormat mFormat;

    public MMAxisValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,###.#");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value) + " mm";
    }
}
