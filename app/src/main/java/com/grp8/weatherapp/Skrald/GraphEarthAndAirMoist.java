package com.grp8.weatherapp.Skrald;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.grp8.weatherapp.SupportingFiles.Formatters.DayAxisValueFormatter;

import java.util.ArrayList;

/**
 * Created by lbirk on 07-01-2017.
 */

public class GraphEarthAndAirMoist extends Fragment{
    private LineChart mChart;
    Typeface mTfLight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_graph_temperature_and_rain, container, false);

        //Generere et layout og sætter grafen til at fylde hele RelativeLayout
        RelativeLayout view = new RelativeLayout(getActivity());
        ViewGroup.LayoutParams lpView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mChart = new LineChart(getActivity());
        mChart.setLayoutParams(lpView);
        view.addView(mChart);

        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setViewPortOffsets(100f, 100f, 100f, 100f);

        Legend l = mChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new DayAxisValueFormatter(mChart));

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setAxisMaximum(100);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setValueFormatter(new PercentFormatter());

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setTypeface(mTfLight);
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaximum(100);
        rightAxis.setAxisMinimum(0);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);
        rightAxis.setValueFormatter(new PercentFormatter());

        setData();
        return view;
    }

    private void setData() {

        ArrayList<Entry> earthVals = new ArrayList<Entry>();
        earthVals.add(new Entry(1,6));
        earthVals.add(new Entry(2,8));
        earthVals.add(new Entry(2,9));
        earthVals.add(new Entry(3,10));
        earthVals.add(new Entry(4,10));
        earthVals.add(new Entry(5,5));
        earthVals.add(new Entry(6,-0));
        earthVals.add(new Entry(7,-5));
        earthVals.add(new Entry(8,-5));
        earthVals.add(new Entry(9,0));

        ArrayList<Entry> airVals = new ArrayList<Entry>();
        airVals.add(new Entry(1,10));
        airVals.add(new Entry(2,20));
        airVals.add(new Entry(2,15));
        airVals.add(new Entry(3,13));
        airVals.add(new Entry(4,12));
        airVals.add(new Entry(5,5));
        airVals.add(new Entry(6,10));
        airVals.add(new Entry(7,8));
        airVals.add(new Entry(8,7));
        airVals.add(new Entry(9,5));

        LineDataSet set1, set2;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) mChart.getData().getDataSetByIndex(1);
            set1.setValues(earthVals);
            set2.setValues(airVals);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(earthVals, "Jordfugtighed");
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
            set1.setValueFormatter(new PercentFormatter());





            // create a dataset and give it a type
            set2 = new LineDataSet(airVals, "Luftfugtighed 2");
            set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
            set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set2.setCubicIntensity(0.1f);
            set2.setColor(Color.RED);
            set2.setCircleColor(Color.BLACK);
            set2.setLineWidth(2f);
            set2.setCircleRadius(3f);
            set2.setFillAlpha(65);
            set2.setFillColor(Color.RED);
            //set2.setDrawCircleHole(false);
            set2.setHighLightColor(Color.rgb(244, 117, 117));
            set2.setValueFormatter(new PercentFormatter());


            // create a data object with the datasets
            LineData data = new LineData(set1, set2);
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(9f);

            // set data
            mChart.setData(data);
        }
    }
}
