package com.grp8.weatherapp.Fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.grp8.weatherapp.Activities.WeatherStationTab;
import com.grp8.weatherapp.Data.DataRepositoryFactory;
import com.grp8.weatherapp.Data.IDataRepository;
import com.grp8.weatherapp.Entities.DataReading;
import com.grp8.weatherapp.Logic.Constants;
import com.grp8.weatherapp.R;
import com.grp8.weatherapp.Logic.Formatters.HourAxisValueFormatter;
import com.grp8.weatherapp.Logic.Formatters.MyMarkerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by lbirk on 09-01-2017.
 */
public class GraphLuxPressure extends Fragment implements DatePickerFragment {

    private LineChart pressureChart;
    private LineChart luxChart;
    Typeface        mTfLight;
    IDataRepository dataRepository;
    long            referenceTimestamp;
    MyMarkerView    myMarkerView;
    WeatherStationTab weatherStationTab;
    int stationId;

    Calendar cal = Calendar.getInstance();
    SimpleDateFormat formatter;


    private TextView dateInputFrom;
    private TextView dateInputTo;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lux_pressure, container, false);
        weatherStationTab = (WeatherStationTab)getActivity();
        stationId = weatherStationTab.stationId;

        formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateInputTo = (TextView) view.findViewById(R.id.dateInputTo);
        setToDate(((WeatherStationTab) getActivity()).getEndDate());
        dateInputTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.setTime(((WeatherStationTab) getActivity()).getEndDate());
                DatePickerDialog dialog = new DatePickerDialog(getContext(), dateToListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(((WeatherStationTab) getActivity()).getStartDate().getTime());
                dialog.getDatePicker().setMaxDate(((WeatherStationTab) getActivity()).getStartDate().getTime()+604800000);
                dialog.setTitle("Choose end date");
                dialog.show();
            }
        });
        dateInputFrom = (TextView) view.findViewById(R.id.dateInputFrom);
        setFromDate(((WeatherStationTab) getActivity()).getStartDate());
        dateInputFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.setTime(((WeatherStationTab) getActivity()).getStartDate());
                DatePickerDialog dialog = new DatePickerDialog(getContext(), dateFromListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
                dialog.setTitle("Choose start date");
                dialog.show();
            }
        });

        dataRepository = DataRepositoryFactory.build(getActivity().getApplicationContext());
        //TODO: Gør denne generisk
        dataRepository.setUser(5);

        //Opretter grafer
        pressureChart = (LineChart) view.findViewById(R.id.pressurechart);
        luxChart = (LineChart) view.findViewById(R.id.luxchart);
        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        drawGraphs();
        getData();
        return view;
    }

    public void drawGraphs()
    {
        definePressureGraph();
        defineLuxGraph();
    }

    public void defineLuxGraph()
    {
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
        luxXAxis.setGranularity(1f);

        //Definere lux's Y-akse
        YAxis luxLeftAxis = luxChart.getAxisLeft();
        luxChart.getAxisRight().setDrawLabels(false);
        luxChart.getAxisRight().setDrawGridLines(false);
        luxLeftAxis.setTypeface(mTfLight);
        luxLeftAxis.setTextColor(ColorTemplate.getHoloBlue());
        luxLeftAxis.setAxisMaximum(1100f);
        luxLeftAxis.setAxisMinimum(900f);
        luxLeftAxis.setDrawGridLines(true);
        luxLeftAxis.setGranularityEnabled(true);
        //******Definere graf for lux SLUT******
    }

    public void definePressureGraph()
    {
        //******Definere graf for tryk START******
        pressureChart.setBackgroundColor(Color.rgb(250,250,250));
        pressureChart.setDrawGridBackground(false);
        pressureChart.setViewPortOffsets(100f, 100f, 100f, 100f);
        pressureChart.getDescription().setEnabled(false);
        //pressureChart.setMarker(myMarkerView);

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

        //Definere tryk's Y-akse
        YAxis PressureYLeftAxis = pressureChart.getAxisLeft();
        PressureYLeftAxis.setTypeface(mTfLight);
        pressureChart.getAxisRight().setDrawLabels(false);
        pressureChart.getAxisRight().setDrawGridLines(false);
        PressureYLeftAxis.setTextColor(ColorTemplate.getHoloBlue());
        PressureYLeftAxis.setAxisMaximum(1100);
        PressureYLeftAxis.setAxisMinimum(900);
        PressureYLeftAxis.setDrawGridLines(true);
        PressureYLeftAxis.setGranularityEnabled(true);
        //******Definere graf for tryk SLUT******
    }

    public void getData()
    {
        try{
        new AsyncTask<Void, List<DataReading>, List<DataReading>>() {
            @Override
            protected List<DataReading> doInBackground(Void... args) {
               /* List<DataReading> data = dataRepository.getStationData(stationId, convertDateFromStringToDate(dtStart), convertDateFromStringToDate(dtEnd));*/
                List<DataReading> data = dataRepository.getStationData(stationId, (((WeatherStationTab) getActivity()).getStartDate()), (((WeatherStationTab) getActivity()).getEndDate()));
                return data;
            }

            @Override
            protected void onPostExecute(List<DataReading> data) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                referenceTimestamp = data.get(0).getTimestamp().getTime()/1000;
                String string = sdf.format(referenceTimestamp);
                Log.d("Reference","Reference tidspunktet er: " + string);
                setPressureData(data);
                setLuxData(data);
            }
        }.execute();
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    }


    private void setPressureData(List<DataReading> data) {
        final ArrayList<Entry> pressureVals = new ArrayList<Entry>();
        for (int i=0;i<data.size();i++)
        {
            DataReading d = data.get(i);
            long xNew = (d.getTimestamp().getTime()/1000)-referenceTimestamp;
            //float x = d.getTimestamp().ge;
            float x = d.getTimestamp().getTime();
            float y = (float) d.getAirReadings().getPressure();
            pressureVals.add(new Entry(xNew, y));
        }
        LineDataSet set1;

        if (pressureChart.getData() != null && pressureChart.getData().getDataSetCount() > 0) {
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
            set1.setDrawCircles(false);
            set1.setFillAlpha(100);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            pressureChart.getXAxis().setValueFormatter(new HourAxisValueFormatter(referenceTimestamp));
            myMarkerView = new MyMarkerView(getActivity().getApplicationContext(), R.layout.custom_marker_view, referenceTimestamp);
            pressureChart.setMarker(myMarkerView);

            // create a data object with the datasets
            LineData lineData = new LineData(set1);
            lineData.setValueTextColor(Color.BLACK);
            lineData.setValueTextSize(9f);
            // set data
            pressureChart.setData(lineData);
            pressureChart.notifyDataSetChanged();
            pressureChart.invalidate();
        }
    }
    private void setLuxData(List<DataReading> data) {
        ArrayList<Entry> luxVals = new ArrayList<Entry>();
        for (int i=0;i<data.size();i++)
        {
            DataReading d = data.get(i);
            long xNew = (d.getTimestamp().getTime()/1000)-referenceTimestamp;
            float x = d.getTimestamp().getTime();
            float y = (float) d.getAirReadings().getTemperature();
            luxVals.add(new Entry(xNew, y));
        }

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

            set1.setColor(ColorTemplate.getHoloBlue());
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(100);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            luxChart.getXAxis().setValueFormatter(new HourAxisValueFormatter(referenceTimestamp));

            // create a data object with the datasets
            LineData lineData = new LineData(set1);
            lineData.setValueTextColor(Color.BLACK);
            lineData.setValueTextSize(9f);

            // set data
            luxChart.setData(lineData);
            luxChart.notifyDataSetChanged();
            luxChart.invalidate();
    }
    }
    private Date convertDateFromStringToDate(String date)
    {
        Calendar cal = Calendar.getInstance();
        try {
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            cal.setTime(sdf.parse(date));// all done
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

    private DatePickerDialog.OnDateSetListener dateFromListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            ((WeatherStationTab) getActivity()).setFromDate(cal.getTime());
            cal.set(Calendar.DATE, 7);
            if (((WeatherStationTab) getActivity()).getEndDate().after(cal.getTime())) {
                dateInputTo.callOnClick();
            }
        }
    };

    private DatePickerDialog.OnDateSetListener dateToListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            ((WeatherStationTab) getActivity()).setToDate(cal.getTime());
            drawGraphs();
            getData();
        }
    };

    public static float toNumber(int hour, int minute) {
        return hour + minute / 60f;
    }

    @Override
    public void setToDate(Date date) {
        dateInputTo.setText(formatter.format(date));
    }

    @Override
    public void setFromDate(Date date) {
        dateInputFrom.setText(formatter.format(date));
    }
}