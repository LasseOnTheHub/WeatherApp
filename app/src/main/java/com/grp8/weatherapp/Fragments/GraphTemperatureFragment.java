package com.grp8.weatherapp.Fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.grp8.weatherapp.R;
import com.grp8.weatherapp.SupportingFiles.DayAxisValueFormatter;

import java.util.ArrayList;

/**
 * Created by lbirk on 06-01-2017.
 */

public class GraphTemperatureFragment extends Fragment {

    private LineChart mChart;
    private TextView tvXMax;
    private TextView tvyMax;
    private SeekBar seekBar1;
    private SeekBar seekBar2;
    Typeface mTfLight;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_temperature, container, false);

      getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Bold.ttf");

        tvXMax = (TextView) view.findViewById(R.id.tvXMax);
        tvyMax = (TextView) view.findViewById(R.id.tvYMax);
        seekBar1 = (SeekBar) view.findViewById(R.id.seekBar1);
        seekBar2 = (SeekBar) view.findViewById(R.id.seekBar2);


        mChart = (LineChart) view.findViewById(R.id.chart1);
        mChart.setBackgroundColor(Color.rgb(66, 212, 244));
        // no description text
        mChart.getDescription().setEnabled(false);
        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setViewPortOffsets(60f, 0f, 0f, 100f);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        mChart.setMaxHighlightDistance(300);

        //Formater som dage istedet for tal
        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);


        XAxis x = mChart.getXAxis();

        x.setEnabled(true);
        x.setDrawLabels(true);
        x.setTypeface(mTfLight);
        x.setLabelCount(6, false);
        x.setTextColor(Color.BLACK);
        x.setTextSize(14f);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(false);
        x.setAxisLineColor(Color.WHITE);
        x.setValueFormatter(xAxisFormatter);

        YAxis y = mChart.getAxisLeft();
        y.setTypeface(mTfLight);
        y.setLabelCount(6, false);
        y.setTextColor(Color.BLACK);
        y.setTextSize(14f);
        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.WHITE);

        // add data
        setData();

        mChart.getLegend().setEnabled(false);

        mChart.animateXY(2000, 2000);
        // dont forget to refresh the drawing
        mChart.invalidate();


        return view;
    }

    private void setData() {

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        yVals.add(new Entry(5, -6));
        yVals.add(new Entry(6, -10));
        yVals.add(new Entry(7, -5));
        yVals.add(new Entry(8, 0));
        yVals.add(new Entry(9, 5));

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals, "DataSet 1");

            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.2f);
            set1.setDrawCircles(false);
            set1.setLineWidth(1.8f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(Color.WHITE);
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setColor(Color.WHITE);
            set1.setFillColor(Color.WHITE);
            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return -10;
                }
            });

            // create a data object with the datasets
            LineData data = new LineData(set1);
            data.setValueTypeface(mTfLight);
            data.setValueTextSize(12f);
            data.setDrawValues(true);

            // set data
            mChart.setData(data);
        }
    }
}
