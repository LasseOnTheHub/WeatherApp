package com.grp8.weatherapp.Fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
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
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.grp8.weatherapp.Data.DataRepository;
import com.grp8.weatherapp.Entities.DataReading;
import com.grp8.weatherapp.Entities.Station;
import com.grp8.weatherapp.R;
import com.grp8.weatherapp.SupportingFiles.Formatters.DayAxisValueFormatter;
import com.grp8.weatherapp.SupportingFiles.Formatters.DegreeAxisValueFormatter;
import com.grp8.weatherapp.SupportingFiles.Formatters.DegreeValueFormatter;
import com.grp8.weatherapp.SupportingFiles.Formatters.MMAxisValueFormatter;
import com.grp8.weatherapp.SupportingFiles.Formatters.MMValueFormatter;
import com.grp8.weatherapp.Data.DataRepositoryFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by lbirk on 08-01-2017.
 */



public class GraphTempRainHumidityFragment extends Fragment {

    //Grafer
    private LineChart humidityChart;
    private CombinedChart tempRainChart;
    Typeface mTfLight;
    DataRepository dataRepository;

/*    String dtStart = "";
    String dtEnd = "";*/

    //Dato vælger
    private TextView dateInputFrom;
    private TextView dateInputTo;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_temp_rain_humidity, container, false);
        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");
        dataRepository = DataRepositoryFactory.build(getActivity().getApplicationContext());
        dataRepository.setUser(5);

        //Grafer
        humidityChart = (LineChart) view.findViewById(R.id.humiditychart);
        tempRainChart = (CombinedChart) view.findViewById(R.id.tempandrainchart);

        //Datovælger
        dateInputFrom = (TextView) view.findViewById(R.id.dateInputFrom);
        dateInputTo = (TextView) view.findViewById(R.id.dateInputTo);

        //******Definere graf for temperatur og nedbør START******

        tempRainChart.getDescription().setEnabled(false);
        tempRainChart.setBackgroundColor(Color.rgb(250,250,250));
        tempRainChart.setDrawGridBackground(false);
        tempRainChart.setDrawBarShadow(false);
        tempRainChart.setHighlightFullBarEnabled(false);
        tempRainChart.setViewPortOffsets(100f, 100f, 100f, 100f);

        //Tegner linjen oven på bars
        tempRainChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE});

        //Definere temperatur og nedbørs legend
        Legend tempRainLegend = tempRainChart.getLegend();
        tempRainLegend.setWordWrapEnabled(true);
        tempRainLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        tempRainLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        tempRainLegend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        tempRainLegend.setDrawInside(false);

        //Definere nedbørs højre Y-akse
        YAxis tempRainYRightAxis = tempRainChart.getAxisRight();
        tempRainYRightAxis.setDrawGridLines(false);
        tempRainYRightAxis.setAxisMinimum(100f); // this replaces setStartAtZero(true)
        //tempRainYRightAxis.setAxisMaximum(40f);
        tempRainYRightAxis.setValueFormatter(new MMAxisValueFormatter());

        //Definere temperatur og nedbørs venstre Y-akse
        YAxis leftAxis = tempRainChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setValueFormatter(new DegreeAxisValueFormatter());

        //Definere temperatur og nedbørs X-akse
        XAxis xAxis = tempRainChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(0f);
        //xAxis.setValueFormatter(new DayAxisValueFormatter(tempRainChart));

        CombinedData data = new CombinedData();

        data.setData(generateTemperatureLineData());
        //data.setData(generateRainBarData());
        data.setValueTypeface(mTfLight);

        //xAxis.setAxisMaximum(data.getXMax() + 0.25f);

        tempRainChart.setData(data);


        //******Definere graf for temperatur og nedbør SLUT******


        //******Definere graf for fugtighede START******

        humidityChart.getDescription().setEnabled(false);
        humidityChart.setBackgroundColor(Color.rgb(250,250,250));
        humidityChart.setDrawGridBackground(false);
        humidityChart.setViewPortOffsets(100f, 100f, 100f, 100f);

        //Sætter fugtigheds legend
        Legend humidityLegend = humidityChart.getLegend();
        humidityLegend.setWordWrapEnabled(true);
        humidityLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        humidityLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        humidityLegend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        humidityLegend.setDrawInside(false);

        //Definere fugtigheds X-aksens udseende
        XAxis humidityXAxis = humidityChart.getXAxis();
        humidityXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        humidityXAxis.setAxisMinimum(0f);
        humidityXAxis.setGranularity(1f);
        humidityXAxis.setValueFormatter(new DayAxisValueFormatter(humidityChart));

        //Definere fugtigheds venstre Y-akses udseende
        YAxis HumidityYleftAxis = humidityChart.getAxisLeft();
        HumidityYleftAxis.setTypeface(mTfLight);
        humidityChart.getAxisRight().setDrawLabels(false);
        humidityChart.getAxisRight().setDrawGridLines(false);
        HumidityYleftAxis.setTextColor(ColorTemplate.getHoloBlue());
        HumidityYleftAxis.setAxisMaximum(100);
        HumidityYleftAxis.setAxisMinimum(0f);
        HumidityYleftAxis.setDrawGridLines(true);
        HumidityYleftAxis.setGranularityEnabled(true);
        HumidityYleftAxis.setValueFormatter(new PercentFormatter());

        //Definere højre Y-akses udseende.
        YAxis humidityYRightAxis = humidityChart.getAxisRight();
        humidityYRightAxis.setTypeface(mTfLight);
        humidityYRightAxis.setTextColor(Color.RED);
        humidityYRightAxis.setAxisMaximum(100);
        humidityYRightAxis.setAxisMinimum(0);
        humidityYRightAxis.setDrawGridLines(false);
        humidityYRightAxis.setDrawZeroLine(false);
        humidityYRightAxis.setGranularityEnabled(false);
        humidityYRightAxis.setValueFormatter(new PercentFormatter());
        //******Definere graf for fugtighede SLUT******


        setHumidityData();
        return view;
    }

    private void setHumidityData() {

        //Dummy Data
        ArrayList<Entry> earthVals = new ArrayList<Entry>();
        earthVals.add(new Entry(1, 6));
        earthVals.add(new Entry(2, 8));
        earthVals.add(new Entry(2, 9));
        earthVals.add(new Entry(3, 10));
        earthVals.add(new Entry(4, 10));
        earthVals.add(new Entry(5, 5));
        earthVals.add(new Entry(6, -0));
        earthVals.add(new Entry(7, -5));
        earthVals.add(new Entry(8, -5));
        earthVals.add(new Entry(9, 0));

        //Dummy Data
        ArrayList<Entry> airVals = new ArrayList<Entry>();
        airVals.add(new Entry(1, 10));
        airVals.add(new Entry(2, 20));
        airVals.add(new Entry(2, 15));
        airVals.add(new Entry(3, 13));
        airVals.add(new Entry(4, 12));
        airVals.add(new Entry(5, 5));
        airVals.add(new Entry(6, 10));
        airVals.add(new Entry(7, 8));
        airVals.add(new Entry(8, 7));
        airVals.add(new Entry(9, 5));

        LineDataSet set1, set2;

        if (humidityChart.getData() != null &&
                humidityChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) humidityChart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) humidityChart.getData().getDataSetByIndex(1);
            set1.setValues(earthVals);
            set2.setValues(airVals);
            humidityChart.getData().notifyDataChanged();
            humidityChart.notifyDataSetChanged();
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
            set2 = new LineDataSet(airVals, "Luftfugtighed");
            set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
/*            set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set2.setCubicIntensity(0.1f);
            set2.setColor(Color.RED);
            set2.setCircleColor(Color.BLACK);*/
            set2.setLineWidth(2f);
            //set2.setCircleRadius(3f);
            //set2.setFillAlpha(65);
            //set2.setFillColor(Color.RED);
            //set2.setDrawCircleHole(false);
            set2.setHighLightColor(Color.rgb(244, 117, 117));
            set2.setValueFormatter(new PercentFormatter());

            // create a data object with the datasets
            LineData data = new LineData(set1, set2);
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(9f);

            // set data
            humidityChart.setData(data);
        }
    }

    private Date convertDateFromStringToDate(String date)
    {
        Calendar cal = Calendar.getInstance();
        try {
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            cal.setTime(sdf.parse(date));// all done
            Log.d("Tid", "Tiden sat fra grafen er:" + cal.getTime().toString());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //return null;
        }
        return cal.getTime();
    }

    public static float toNumber(Date now) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(now);
        int hour = c.get(Calendar.HOUR_OF_DAY);// 0-23
        int minute = c.get(Calendar.MINUTE);// 0-59

        return toNumber(hour, minute);
    }

    public static float toNumber(int hour, int minute) {
        return hour + minute / 60f;
    }

    private LineData generateTemperatureLineData() {

        LineData d = new LineData();
        final ArrayList<Entry> tempVals = new ArrayList<Entry>();

        final String dtStart = "2015-01-01 00:00";
        final String dtEnd = "2017-01-01 00:00";

        try {
            new AsyncTask<Void, List<DataReading>, List<DataReading>>() {
                @Override
                protected List<DataReading> doInBackground(Void... args) {
                    List<DataReading> data = dataRepository.getStationData(2, convertDateFromStringToDate(dtStart), convertDateFromStringToDate(dtEnd));
                    return data;
                }

                @Override
                protected void onPostExecute(List<DataReading> data) {
                    for (DataReading d : data) {
                        float x = toNumber(d.getTimestamp());
                        float y = (float) d.getAirReadings().getTemperature();
                        tempVals.add(new Entry(x, y));
                    }
                }
            }.execute();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
            tempVals.add(new Entry(1, 20));
            LineDataSet set = new LineDataSet(tempVals, "Temperatur");
            set.setColor(Color.rgb(242, 72, 0));
            set.setLineWidth(2.5f);
            set.setCircleColor(Color.rgb(242, 72, 0));
            set.setCircleRadius(0f);
            set.setFillColor(Color.rgb(240, 238, 70));
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setCubicIntensity(0.0f);
            set.setDrawValues(false);
            set.setValueTextSize(10f);
            set.setValueTextColor(Color.rgb(242, 72, 0));
            set.setValueFormatter(new DegreeValueFormatter());

            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            d.addDataSet(set);
            return d;
        }


    private BarData generateRainBarData() {

        final ArrayList<BarEntry> rainVals = new ArrayList<BarEntry>();
        final String dtStart = "2015-01-01 00:00";
        final String dtEnd = "2017-01-01 00:00";

        try {
            new AsyncTask<Void, List<DataReading>, List<DataReading>>() {
                @Override
                protected List<DataReading> doInBackground(Void... args) {
                    List<DataReading> data = dataRepository.getStationData(2, convertDateFromStringToDate(dtStart), convertDateFromStringToDate(dtEnd));
                    return data;
                }

                @Override
                protected void onPostExecute(List<DataReading> data) {
                    for (DataReading d : data) {
                        float x = toNumber(d.getTimestamp());
                        float y = (float) d.getAirReadings().getPressure();
                        rainVals.add(new BarEntry(x, y));
                    }
                }
            }.execute();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        rainVals.add(new BarEntry(1f,1.2f));
/*        rainVals.add(new BarEntry(1f,1.2f));
        rainVals.add(new BarEntry(2f,1.0f));
        rainVals.add(new BarEntry(2f,2.0f));
        rainVals.add(new BarEntry(2f,3.0f));
        rainVals.add(new BarEntry(3f,0.5f));
        rainVals.add(new BarEntry(4f,1.0f));
        rainVals.add(new BarEntry(5f,5.9f));
        rainVals.add(new BarEntry(6f,2.0f));
        rainVals.add(new BarEntry(7f,1.7f));
        rainVals.add(new BarEntry(8f,1.5f));
        rainVals.add(new BarEntry(9f,1.2f));*/


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
