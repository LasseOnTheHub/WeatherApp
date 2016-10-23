package com.grp8.weatherapp.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.grp8.weatherapp.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;
import java.util.Random;

public class StationDetailsActivity extends AppCompatActivity {

    private TextView[] inputs = new TextView[2];
    private int[][]    values = {{0, 0, 0}, {0, 0, 0}};

    private boolean drawValues = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_details);

        ActionBar bar = getSupportActionBar();

        if(bar != null)
        {
            bar.setTitle("Station Details");
        }

        this.inputs[0] = (TextView) findViewById(R.id.dateInputFrom);
        this.inputs[1] = (TextView) findViewById(R.id.dateInputTo);

        this.inputs[0].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) { showDialog(0); }
        });

        this.inputs[1].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) { showDialog(1); }
        });

        /*
         * Set the current date as default on the text-edit fields and the date-pickers.
         */
        Calendar calendar = Calendar.getInstance();

        int year  = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day   = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        this.values[0][0] = year;
        this.values[1][0] = year;
        this.values[0][1] = month;
        this.values[1][1] = month;
        this.values[0][2] = calendar.get(Calendar.DAY_OF_MONTH);
        this.values[1][2] = day;

        this.inputs[0].setText(this.values[0][2] + "-" + month + "-" + year);
        this.inputs[1].setText(this.values[1][2] + "-" + month + "-" + year);

        createGraphs();
    }

    @Override
    public Dialog onCreateDialog(int id)
    {
        if(this.values[id] == null)
        {
            return null;
        }

        return new DatePickerDialog(this, this.createDatePickerListener(id), this.values[id][0], this.values[id][1], this.values[id][2]);
    }

    private DatePickerDialog.OnDateSetListener createDatePickerListener(final int id)
    {
        return new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day)
            {
                if(id == 1)
                {
                    if(values[0][0] > year || (values[0][0] == year && values[0][1] > month))
                    {
                        Toast.makeText(StationDetailsActivity.this, "Invalid date", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                values[id][0] = year;
                values[id][1] = month + 1;
                values[id][2] = day;

                inputs[id].setText(day + "-" + month + "-" + year);

                createGraphs();
            }
        };
    }

    private void createGraphs()
    {
        createGraph((GraphView) findViewById(R.id.graphTemperature), 7, 25, 5);
        createGraph((GraphView) findViewById(R.id.graphHumidity), 7, 80, 40);
        createGraph((GraphView) findViewById(R.id.graphRain), 7, 50, 20);
    }

    private void createGraph(GraphView graph, int recordings, int max, int min)
    {
        graph.removeAllSeries();

        DataPoint[] points = new DataPoint[recordings];

        Random   random   = new Random();
        Calendar calendar = Calendar.getInstance();

        calendar.set(this.values[0][0], this.values[0][1], this.values[0][2]);

        String[] labels = new String[recordings];

        for(int i = 0; i < recordings; i++)
        {
            if(i % 2 == 1)
            {
                labels[i] = calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + 1;
            }

            points[i] = new DataPoint(calendar.getTime(), random.nextInt((max - min)) + min);

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);

        series.setThickness(5);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(8);

        graph.addSeries(series);

        graph.getGridLabelRenderer().setLabelFormatter(new StaticLabelsFormatter(graph, labels, null));
        graph.getGridLabelRenderer().setNumHorizontalLabels(recordings);

        graph.getGridLabelRenderer().setHumanRounding(false);

        graph.getViewport().setMaxX(max + 10);
        graph.getViewport().setMinY(0);
    }
}
