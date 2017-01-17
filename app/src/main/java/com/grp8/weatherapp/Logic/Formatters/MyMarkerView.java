package com.grp8.weatherapp.Logic.Formatters;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.grp8.weatherapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by lbirk on 12-01-2017.
 */

public class MyMarkerView extends MarkerView {

    private final TextView tvContent;
    private final long referenceTimestamp;  // minimum timestamp in your data set
    private final DateFormat mDataFormat;
    private final Date mDate;

    public MyMarkerView(Context context, long referenceTimestamp) {
        super(context, R.layout.custom_marker_view);
        // this markerview only displays a textview
        tvContent = (TextView) findViewById(R.id.tvContent);
        this.referenceTimestamp = referenceTimestamp;
        this.mDataFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);
        this.mDate = new Date();
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        long currentTimestamp = (int)e.getX() + referenceTimestamp;

        tvContent.setText(e.getY() +" - d."+ getTimedate(currentTimestamp)); // set the entry-value as the display text
        super.refreshContent(e, highlight);
    }
    @Override
    public MPPointF getOffset() {
        return new MPPointF((float)-(getWidth() / 2), (float)-getHeight());
    }


    private String getTimedate(long timestamp){

        try{
            mDate.setTime(timestamp*1000);
            return mDataFormat.format(mDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }
}