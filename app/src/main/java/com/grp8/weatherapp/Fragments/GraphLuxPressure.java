package com.grp8.weatherapp.Fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.grp8.weatherapp.R;
import com.grp8.weatherapp.SupportingFiles.Formatters.DayAxisValueFormatter;

import java.util.ArrayList;

/**
 * Created by lbirk on 09-01-2017.
 */

public class GraphLuxPressure extends Fragment {

    private LineChart pressureChart;
    private LineChart luxChart;
    Typeface mTfLight;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lux_pressure, container, false);

        //Opretter grafer
        pressureChart = (LineChart) view.findViewById(R.id.pressurechart);
        luxChart = (LineChart) view.findViewById(R.id.luxchart);
        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        //******Definere graf for tryk START******
        pressureChart.setBackgroundColor(Color.rgb(250,250,250));
        pressureChart.setDrawGridBackground(false);
        pressureChart.setViewPortOffsets(100f, 100f, 100f, 100f);
        pressureChart.getDescription().setEnabled(false);

        //Legend
        Legend pressureLegend = pressureChart.getLegend();
        pressureLegend.setWordWrapEnabled(true);
        pressureLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        pressureLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        pressureLegend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        pressureLegend.setDrawInside(false);

        //Definere tryk's X-akse
        XAxis PressyreXAxis = pressureChart.getXAxis();
        PressyreXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        PressyreXAxis.setAxisMinimum(0f);
        PressyreXAxis.setGranularity(1f);
        PressyreXAxis.setValueFormatter(new DayAxisValueFormatter(pressureChart));

        //Definere tryk's Y-akse
        YAxis PressureYLeftAxis = pressureChart.getAxisLeft();
        PressureYLeftAxis.setTypeface(mTfLight);
        pressureChart.getAxisRight().setDrawLabels(false);
        pressureChart.getAxisRight().setDrawGridLines(false);
        PressureYLeftAxis.setTextColor(ColorTemplate.getHoloBlue());
        PressureYLeftAxis.setAxisMaximum(100);
        PressureYLeftAxis.setAxisMinimum(0f);
        PressureYLeftAxis.setDrawGridLines(true);
        PressureYLeftAxis.setGranularityEnabled(true);
        //******Definere graf for tryk SLUT******



        //******Definere graf for lux START******
        luxChart.getDescription().setEnabled(false);
        luxChart.setBackgroundColor(Color.rgb(250,250,250));
        luxChart.setDrawGridBackground(false);
        luxChart.setViewPortOffsets(100f, 100f, 100f, 100f);

        //Definere lux's legend
        Legend luxLegend = luxChart.getLegend();
        luxLegend.setWordWrapEnabled(true);
        luxLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        luxLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        luxLegend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        luxLegend.setDrawInside(false);

        //Definere lux's X-akse
        XAxis luxXAxis = luxChart.getXAxis();
        luxXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        luxXAxis.setAxisMinimum(0f);
        luxXAxis.setGranularity(1f);
        luxXAxis.setValueFormatter(new DayAxisValueFormatter(luxChart));


        //Definere lux's Y-akse
        YAxis luxLeftAxis = luxChart.getAxisLeft();
        luxChart.getAxisRight().setDrawLabels(false);
        luxChart.getAxisRight().setDrawGridLines(false);
        luxLeftAxis.setTypeface(mTfLight);
        luxLeftAxis.setTextColor(ColorTemplate.getHoloBlue());
        luxLeftAxis.setAxisMaximum(100);
        luxLeftAxis.setAxisMinimum(0f);
        luxLeftAxis.setDrawGridLines(true);
        luxLeftAxis.setGranularityEnabled(true);
        //******Definere graf for lux SLUT******

        setPressyreData();
        setLuxData();
        return view;
    }

    private void setPressyreData() {

        ArrayList<Entry> pressureVals = new ArrayList<Entry>();
        pressureVals.add(new Entry(1,6));
        pressureVals.add(new Entry(2,8));
        pressureVals.add(new Entry(2,9));
        pressureVals.add(new Entry(3,10));
        pressureVals.add(new Entry(4,10));
        pressureVals.add(new Entry(5,5));
        pressureVals.add(new Entry(6,-0));
        pressureVals.add(new Entry(7,-5));
        pressureVals.add(new Entry(8,-5));
        pressureVals.add(new Entry(9,0));

        LineDataSet set1;

        if (pressureChart.getData() != null &&
                pressureChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) pressureChart.getData().getDataSetByIndex(0);
            set1.setValues(pressureVals);
            pressureChart.getData().notifyDataChanged();
            pressureChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(pressureVals, "Tryk");
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.1f);
            set1.setDrawCircles(true);

            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(ColorTemplate.getHoloBlue());
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(100);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));

            // create a data object with the datasets
            LineData data = new LineData(set1);
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(9f);

            // set data
            pressureChart.setData(data);
        }
    }
    private void setLuxData() {

        ArrayList<Entry> luxVals = new ArrayList<Entry>();
        luxVals.add(new Entry(1,10));
        luxVals.add(new Entry(2,20));
        luxVals.add(new Entry(2,30));
        luxVals.add(new Entry(3,40));
        luxVals.add(new Entry(4,0));
        luxVals.add(new Entry(5,-10));
        luxVals.add(new Entry(6,20));
        luxVals.add(new Entry(7,5));
        luxVals.add(new Entry(8,10));
        luxVals.add(new Entry(9,20));

        LineDataSet set1;

        if (luxChart.getData() != null &&
                luxChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) luxChart.getData().getDataSetByIndex(0);
            set1.setValues(luxVals);
            luxChart.getData().notifyDataChanged();
            luxChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(luxVals, "Lux");
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.1f);
            set1.setDrawCircles(true);

            //set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(ColorTemplate.getHoloBlue());
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(100);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));

            // create a data object with the datasets
            LineData data = new LineData(set1);
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(9f);

            // set data
            luxChart.setData(data);
        }
    }
}