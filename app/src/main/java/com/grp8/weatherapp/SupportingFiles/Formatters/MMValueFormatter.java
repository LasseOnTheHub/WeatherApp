package com.grp8.weatherapp.SupportingFiles.Formatters;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class MMValueFormatter implements IValueFormatter
{

    private DecimalFormat mFormat;
    
    public MMValueFormatter() {
        mFormat = new DecimalFormat("###.0");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value) + " mm";
    }
}
