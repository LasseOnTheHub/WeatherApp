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
import com.grp8.weatherapp.Activities.WeatherStationTab;
import com.grp8.weatherapp.Data.IDataRepository;
import com.grp8.weatherapp.Entities.DataReading;
import com.grp8.weatherapp.Logic.Formatters.HourAxisValueFormatter;
import com.grp8.weatherapp.Logic.Formatters.MyMarkerView;
import com.grp8.weatherapp.R;
import com.grp8.weatherapp.Logic.Formatters.DayAxisValueFormatter;
import com.grp8.weatherapp.Logic.Formatters.DegreeAxisValueFormatter;
import com.grp8.weatherapp.Logic.Formatters.DegreeValueFormatter;
import com.grp8.weatherapp.Logic.Formatters.MMAxisValueFormatter;
import com.grp8.weatherapp.Logic.Formatters.MMValueFormatter;
import com.grp8.weatherapp.Data.DataRepositoryFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by lbirk on 08-01-2017.
 */



public class GraphTempRainHumidityFragment extends Fragment implements DatePickerFragment {

    //Grafer
    private LineChart humidityChart;
    private CombinedChart tempRainChart;
    Typeface        mTfLight;
    IDataRepository dataRepository;
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat formatter;
    long            referenceTimestamp;
    MyMarkerView    myMarkerView;
    int             stationId;
    WeatherStationTab weatherStationTab;

    //Dato vælger
    private TextView dateInputFrom;
    private TextView dateInputTo;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_temp_rain_humidity, container, false);
        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");
        dataRepository = DataRepositoryFactory.build(getActivity().getApplicationContext());
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

        //Grafer
        humidityChart = (LineChart) view.findViewById(R.id.humiditychart);
        tempRainChart = (CombinedChart) view.findViewById(R.id.tempandrainchart);

        getData();
        drawGraphs();

        return view;
    }

    public void drawGraphs()
    {
        defineTemperatureAndRainGraph();
        defineHumidityGraph();
    }


    private void defineHumidityGraph()
    {
        humidityChart.getDescription().setEnabled(false);
        humidityChart.setBackgroundColor(Color.rgb(250,250,250));
        humidityChart.setDrawGridBackground(false);
        humidityChart.setViewPortOffsets(120f, 100f, 120f, 100f);

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
        humidityXAxis.setValueFormatter(new HourAxisValueFormatter(referenceTimestamp));

        //Definere fugtigheds venstre Y-akses udseende
        YAxis HumidityYleftAxis = humidityChart.getAxisLeft();
        HumidityYleftAxis.setTypeface(mTfLight);
        HumidityYleftAxis.setTextColor(ColorTemplate.getHoloBlue());
        HumidityYleftAxis.setAxisMaximum(100);
        HumidityYleftAxis.setAxisMinimum(0f);
        HumidityYleftAxis.setDrawGridLines(true);
        HumidityYleftAxis.setGranularityEnabled(true);
        HumidityYleftAxis.setSpaceTop(5);
        HumidityYleftAxis.setSpaceBottom(5);
        HumidityYleftAxis.setValueFormatter(new PercentFormatter());

        //Definere fugtigheds højre Y-akses udseende.
        YAxis humidityYRightAxis = humidityChart.getAxisRight();
        humidityYRightAxis.setTypeface(mTfLight);
        humidityYRightAxis.setTextColor(Color.RED);
        humidityYRightAxis.setAxisMaximum(100);
        humidityYRightAxis.setAxisMinimum(0);
        humidityYRightAxis.setDrawGridLines(false);
        humidityYRightAxis.setDrawZeroLine(false);
        humidityYRightAxis.setGranularityEnabled(false);
        humidityYRightAxis.setSpaceTop(5);
        humidityYRightAxis.setSpaceBottom(5);
        humidityYRightAxis.setValueFormatter(new PercentFormatter());
    }

    private void defineTemperatureAndRainGraph()
    {
        tempRainChart.getDescription().setEnabled(false);
        tempRainChart.setBackgroundColor(Color.rgb(250,250,250));
        tempRainChart.setDrawGridBackground(false);
        tempRainChart.setDrawBarShadow(false);
        tempRainChart.setHighlightFullBarEnabled(false);
        tempRainChart.setViewPortOffsets(100f, 100f, 120f, 100f);

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
        tempRainYRightAxis.setSpaceTop(5);
        tempRainYRightAxis.setAxisMinimum(0);
        tempRainYRightAxis.setValueFormatter(new MMAxisValueFormatter());

        //Definere temperatur og nedbørs venstre Y-akse
        YAxis tempRainYLeftAxis = tempRainChart.getAxisLeft();
        tempRainYLeftAxis.setDrawGridLines(false);
        tempRainYLeftAxis.setSpaceTop(5);
        tempRainYLeftAxis.setSpaceBottom(5);
        tempRainYLeftAxis.setValueFormatter(new DegreeAxisValueFormatter());

        //Definere temperatur og nedbørs X-akse
        XAxis xAxis = tempRainChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(0f);

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
                        Log.d("Reference", "Reference tidspunktet sat i temprain er: " + string);
                        setTempRainChart(data);
                        setHumidityData(data);
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Intet data i det angivet tidsrum", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setHumidityData(List<DataReading> data) {
        ArrayList<Entry> earthVals = new ArrayList<Entry>();
        ArrayList<Entry> airVals = new ArrayList<Entry>();

        for (int i=0;i<data.size();i++)
        {
            DataReading d = data.get(i);
            long xNew = (d.getTimestamp().getTime()/1000)-referenceTimestamp;
            float earthY = (float) d.getAirReadings().getHumidity();
            float airY = (float) d.getSoilReadings().getMoisture()[1];
            earthVals.add(new Entry(xNew, earthY));
            airVals.add(new Entry(xNew, airY));
        }
        LineDataSet set1, set2;
        if (humidityChart.getData() != null &&
                humidityChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) humidityChart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) humidityChart.getData().getDataSetByIndex(1);
            set1.setValues(earthVals);
            set2.setValues(airVals);
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(earthVals, "Jordfugtighed");
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.1f);
            set1.setDrawCircles(false);
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
            set2.setLineWidth(2f);
            set2.setHighLightColor(Color.rgb(244, 117, 117));
            set2.setDrawCircles(false);
            set2.setValueFormatter(new PercentFormatter());
            humidityChart.getXAxis().setValueFormatter(new HourAxisValueFormatter(referenceTimestamp));
            myMarkerView = new MyMarkerView(getActivity().getApplicationContext(), R.layout.custom_marker_view, referenceTimestamp);
            humidityChart.setMarker(myMarkerView);

            // create a data object with the datasets
            LineData d = new LineData(set1, set2);
            d.setValueTextColor(Color.BLACK);
            d.setValueTextSize(9f);

            // set data
            humidityChart.setData(d);
            d.notifyDataChanged();
            humidityChart.notifyDataSetChanged();
            humidityChart.invalidate();
        }
    }

    public static float toNumber(int hour, int minute) {
        return hour + minute / 60f;
    }

    private LineData setTemperatureData(List<DataReading> data) {
        LineData lineData = new LineData();
        final ArrayList<Entry> tempVals = new ArrayList<Entry>();

        for (int i=0;i<data.size();i++)
        {
            DataReading d = data.get(i);
            long xNew = (d.getTimestamp().getTime()/1000)-referenceTimestamp;
            float x = d.getTimestamp().getTime();
            float y = (float) d.getAirReadings().getTemperature();
            tempVals.add(new Entry(xNew, y));
        }
        LineDataSet set1;

        if (tempRainChart.getData() != null && tempRainChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) tempRainChart.getData().getDataSetByIndex(0);
            set1.setValues(tempVals);
            set1.notifyDataSetChanged();
            tempRainChart.notifyDataSetChanged();
            tempRainChart.invalidate();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(tempVals, "Temperatur");
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
            tempRainChart.getXAxis().setValueFormatter(new HourAxisValueFormatter(referenceTimestamp));
            myMarkerView = new MyMarkerView(getActivity().getApplicationContext(), R.layout.custom_marker_view, referenceTimestamp);
            tempRainChart.setMarker(myMarkerView);
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);

            // create a data object with the datasets
            lineData.addDataSet(set1);
            lineData.notifyDataChanged();
            tempRainChart.notifyDataSetChanged();
            tempRainChart.invalidate();
        }
        return lineData;
    }


    private BarData setRainBarData(List<DataReading> data) {

        final ArrayList<BarEntry> rainVals = new ArrayList<BarEntry>();

        for (int i=0;i<data.size();i++)
        {
            DataReading d = data.get(i);
            long xNew = (d.getTimestamp().getTime()/1000)-referenceTimestamp;
            float y = (float) d.getRainfall();
            rainVals.add(new BarEntry(xNew, y));
        }
        BarDataSet set1 = new BarDataSet(rainVals, "Nedbør");
        set1.setColor(Color.rgb(60, 220, 78));
        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set1.setValueFormatter(new MMValueFormatter());
        BarData d = new BarData(set1);
        d.setBarWidth(0.90f);

        d.notifyDataChanged();
/*        tempRainChart.notifyDataSetChanged();
        tempRainChart.invalidate();*/

        return d;
    }

    private void setTempRainChart(List<DataReading> d)
    {
        CombinedData data = new CombinedData();
        data.setData(setTemperatureData(d));
        data.setData(setRainBarData(d));
        data.setValueTypeface(mTfLight);
        tempRainChart.setData(data);
        data.notifyDataChanged();
        tempRainChart.notifyDataSetChanged();
        tempRainChart.invalidate();
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
            getData();
            drawGraphs();
        }
    };

    private void removeDataSet() {
        humidityChart.clear();
        tempRainChart.clear();
        tempRainChart.notifyDataSetChanged();
        tempRainChart.invalidate();
        humidityChart.notifyDataSetChanged();
        humidityChart.invalidate();
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
