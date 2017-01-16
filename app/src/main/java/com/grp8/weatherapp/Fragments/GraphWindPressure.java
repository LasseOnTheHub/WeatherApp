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
import android.widget.Toast;

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
import com.grp8.weatherapp.R;
import com.grp8.weatherapp.Logic.Formatters.HourAxisValueFormatter;
import com.grp8.weatherapp.Logic.Formatters.MyMarkerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by lbirk on 09-01-2017.
 */
public class GraphWindPressure extends Fragment implements DatePickerFragment {

    private LineChart pressureChart;
    private LineChart windChart;
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
        View view = inflater.inflate(R.layout.fragment_wind_pressure, container, false);
        weatherStationTab = (WeatherStationTab)getActivity();
        stationId = weatherStationTab.getCurrentStationID();

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


        //Opretter grafer
        pressureChart = (LineChart) view.findViewById(R.id.pressurechart);
        windChart = (LineChart) view.findViewById(R.id.windchart);

        //Skrifttype
        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        getData();
        drawGraphs();

        return view;
    }

    public void drawGraphs()
    {
        definePressureGraph();
        defineWindGraph();
    }

    public void defineWindGraph()
    {
        //******Definere graf for wind START******
        windChart.getDescription().setEnabled(false);
        windChart.setBackgroundColor(Color.rgb(250,250,250));
        windChart.setDrawGridBackground(false);
        windChart.setViewPortOffsets(100f, 100f, 100f, 100f);

        //Definere wind's legend
        Legend windLegend = windChart.getLegend();
        windLegend.setWordWrapEnabled(true);
        windLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        windLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        windLegend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        windLegend.setDrawInside(false);

        //Definere wind's X-akse
        XAxis windXAxis = windChart.getXAxis();
        windXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        windXAxis.setGranularity(1f);

        //Definere wind's Y-akse
        YAxis windLeftAxis= windChart.getAxisLeft();
        windChart.getAxisRight().setDrawLabels(false);
        windChart.getAxisRight().setDrawGridLines(false);
        windLeftAxis.setTypeface(mTfLight);
        windLeftAxis.setTextColor(ColorTemplate.getHoloBlue());
        windLeftAxis.setSpaceTop(5);
        windLeftAxis.setSpaceBottom(5);
        windLeftAxis.setDrawGridLines(true);
        windLeftAxis.setGranularityEnabled(true);
        //******Definere graf for wind SLUT******
    }

    public void definePressureGraph()
    {
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
        pressureChart.getDescription().setEnabled(false);
        PressureYLeftAxis.setTextColor(ColorTemplate.getHoloBlue());
        PressureYLeftAxis.setSpaceTop(5);
        PressureYLeftAxis.setSpaceBottom(5);
        PressureYLeftAxis.setDrawGridLines(true);
        PressureYLeftAxis.setGranularityEnabled(true);

        pressureChart.setNoDataText("Der er desværre ingen data i denne periode");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && ((WeatherStationTab) getActivity()).shouldShowNoDataToastOnGraphAppearance()) {
            Log.d("Showing toast",String.valueOf(isVisibleToUser));
            Log.d("Showing toast",String.valueOf(((WeatherStationTab) getActivity()).shouldShowNoDataToastOnGraphAppearance()));
            Toast.makeText(getActivity(), getString(R.string.no_data), Toast.LENGTH_LONG).show();
            ((WeatherStationTab) getActivity()).setShouldShowNoDataToastOnGraphAppearance();
            Log.d("Showing toast next time",String.valueOf(((WeatherStationTab) getActivity()).shouldShowNoDataToastOnGraphAppearance()));
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    public void getData()
    {
        try{
        new AsyncTask<Void, List<DataReading>, List<DataReading>>() {
            @Override
            protected List<DataReading> doInBackground(Void... args) {
                List<DataReading> data = dataRepository.getStationData(stationId, (((WeatherStationTab) getActivity()).getStartDate()), (((WeatherStationTab) getActivity()).getEndDate()));
                return data;
            }

            @Override
            protected void onPostExecute(List<DataReading> data) {
                if(!data.isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    referenceTimestamp = data.get(0).getTimestamp().getTime() / 1000;
                    String string = sdf.format(referenceTimestamp * 1000);
                    setPressureData(data);
                    setWindData(data);
                }
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

            float x = d.getTimestamp().getTime();
            float y = (float) d.getAirReadings().getPressure();
            pressureVals.add(new Entry(xNew, y));
        }
        LineDataSet set1;

        if (pressureChart.getData() != null && pressureChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) pressureChart.getData().getDataSetByIndex(0);
            set1.setValues(pressureVals);
            set1.notifyDataSetChanged();

            pressureChart.notifyDataSetChanged();
            pressureChart.invalidate();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(pressureVals, "Tryk");
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.1f);
            set1.setDrawCircles(false);
            set1.setDrawValues(false);
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(ColorTemplate.getHoloBlue());
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(2f);
            set1.setFillAlpha(100);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            pressureChart.getXAxis().setValueFormatter(new HourAxisValueFormatter(referenceTimestamp));
            myMarkerView = new MyMarkerView(getActivity().getApplicationContext(), R.layout.custom_marker_view, referenceTimestamp);
            pressureChart.setMarker(myMarkerView);

            // create a data object with the datasets
            LineData lineData = new LineData(set1);
            pressureChart.setData(lineData);
            lineData.notifyDataChanged();
            pressureChart.notifyDataSetChanged();
            pressureChart.invalidate();
        }
    }

    private void setWindData(List<DataReading> data) {
        ArrayList<Entry> vals = new ArrayList<Entry>();
        for (int i=0;i<data.size();i++)
        {
            DataReading d = data.get(i);
            long xNew = (d.getTimestamp().getTime()/1000)-referenceTimestamp;
            float x = d.getTimestamp().getTime();
            float y = (float) d.getWindReadings().getSpeed();vals.add(new Entry(xNew, y));
        }

        LineDataSet set1;

        if (windChart.getData() != null &&
                windChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) windChart.getData().getDataSetByIndex(0);
            set1.setValues(vals);
            windChart.getData().notifyDataChanged();
            windChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(vals, "Vind hastighed");
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.1f);
            set1.setDrawCircles(false);
            set1.setDrawValues(false);
            set1.setColor(ColorTemplate.getHoloBlue());
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(100);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            myMarkerView = new MyMarkerView(getActivity().getApplicationContext(), R.layout.custom_marker_view, referenceTimestamp);
            pressureChart.setMarker(myMarkerView);
            windChart.getXAxis().setValueFormatter(new HourAxisValueFormatter(referenceTimestamp));

            // create a data object with the datasets
            LineData lineData = new LineData(set1);
            lineData.setValueTextColor(Color.BLACK);
            lineData.setValueTextSize(9f);

            // set data
            windChart.setData(lineData);
            lineData.notifyDataChanged();
            windChart.notifyDataSetChanged();
            windChart.invalidate();
    }
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
            removeDataSet();
            drawGraphs();
            getData();
        }
    };

    private void removeDataSet() {
        pressureChart.clear();
        windChart.clear();
        pressureChart.notifyDataSetChanged();
        pressureChart.invalidate();
        windChart.notifyDataSetChanged();
        windChart.invalidate();
    }

    //TODO: Denne skal kaldes fra det andet graf-fragment for at synkronisere datoer.
    public void synchronizeDate()
    {
        removeDataSet();
        drawGraphs();
        getData();
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