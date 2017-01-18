package com.grp8.weatherapp.Logic.Formatters;

import android.content.Context;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.grp8.weatherapp.Logic.Converters.PressureConverter;
import com.grp8.weatherapp.Logic.SettingsManager;

import java.text.DecimalFormat;

/**
 * Created by lbirk on 17-01-2017.
 */

public class PressureAxisValueFormatter implements IAxisValueFormatter
{
    private final DecimalFormat mFormat;
    private Context context;

    public PressureAxisValueFormatter(Context context) {
        mFormat = new DecimalFormat("####.#");
        this.context = context;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value) + " " + SettingsManager.getPressureUnit(context);
    }
}
