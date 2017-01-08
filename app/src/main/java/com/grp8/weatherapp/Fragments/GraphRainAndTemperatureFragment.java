package com.grp8.weatherapp.Fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.grp8.weatherapp.R;
import com.grp8.weatherapp.SupportingFiles.Formatters.*;

import java.util.ArrayList;

/**
 * Created by lbirk on 06-01-2017.
 */

public class GraphRainAndTemperatureFragment extends Fragment {

    private CombinedChart mChart;
    Typeface mTfLight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_graph_temperature_and_rain, container, false);

        //Generere et layout og sætter grafen til at fylde hele RelativeLayout
        RelativeLayout view = new RelativeLayout(getActivity());
        ViewGroup.LayoutParams lpView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mChart = new CombinedChart(getActivity());
        mChart.setLayoutParams(lpView);
        view.addView(mChart);

        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.setViewPortOffsets(100f, 100f, 100f, 100f);

        // draw bars behind lines
        mChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE});

        Legend l = mChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setAxisMaximum(10f);
        rightAxis.setValueFormatter(new MMAxisValueFormatter());

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setValueFormatter(new DegreeAxisValueFormatter());

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(0f);
        xAxis.setValueFormatter(new DayAxisValueFormatter(mChart));

        CombinedData data = new CombinedData();

        data.setData(generateLineData());
        data.setData(generateBarData());
        data.setValueTypeface(mTfLight);

        xAxis.setAxisMaximum(data.getXMax() + 0.25f);

        mChart.setData(data);
        //mChart.invalidate();

        return view;
    }

    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> tempVals = new ArrayList<Entry>();

        tempVals.add(new Entry(1,6));
        tempVals.add(new Entry(2,8));
        tempVals.add(new Entry(2,9));
        tempVals.add(new Entry(3,10));
        tempVals.add(new Entry(4,10));
        tempVals.add(new Entry(5,5));
        tempVals.add(new Entry(6,-0));
        tempVals.add(new Entry(7,-5));
        tempVals.add(new Entry(8,-5));
        tempVals.add(new Entry(9,0));

/*        tempVals.add(new Entry(1451660400,6));
        tempVals.add(new Entry(1451685600,8));
        tempVals.add(new Entry(1451721600,10));
        tempVals.add(new Entry(1451743200,10));
        tempVals.add(new Entry(1451761200,5));*/


        LineDataSet set = new LineDataSet(tempVals, "Temperatur");
        set.setColor(Color.rgb(242, 72, 0));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(242, 72, 0));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.0f);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(242, 72, 0));
        set.setValueFormatter(new DegreeValueFormatter());

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarData() {

        ArrayList<BarEntry> rainVals = new ArrayList<BarEntry>();

        rainVals.add(new BarEntry(1,1.2f));
        rainVals.add(new BarEntry(2,1.0f));
        rainVals.add(new BarEntry(2f,2.0f));
        rainVals.add(new BarEntry(2f,3.0f));
        rainVals.add(new BarEntry(3f,0.5f));
        rainVals.add(new BarEntry(4f,1.0f));
        rainVals.add(new BarEntry(5f,5.9f));
        rainVals.add(new BarEntry(6f,2.0f));
        rainVals.add(new BarEntry(7f,1.7f));
        rainVals.add(new BarEntry(8f,1.5f));
        rainVals.add(new BarEntry(9f,1.2f));


/*        rainVals.add(new BarEntry(1451660400,1,2));
        rainVals.add(new BarEntry(1451685600,1,6));
        rainVals.add(new BarEntry(1451721600,2));
        rainVals.add(new BarEntry(1451743200,2,5));
        rainVals.add(new BarEntry(1451761200,3));*/

        BarDataSet set1 = new BarDataSet(rainVals, "Nedbør");
        set1.setColor(Color.rgb(60, 220, 78));
        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set1.setValueFormatter(new MMValueFormatter());

        float barWidth = 0.90f; //
        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);
        return d;
    }
}
