package com.grp8.weatherapp.Fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.grp8.weatherapp.Activities.WeatherStationTab;
import com.grp8.weatherapp.Adapters.WeatherStationsAdapter;
import com.grp8.weatherapp.R;

import static android.view.View.VISIBLE;

/**
 * Created by Frederik on 14/11/2016.
 */

public class MainFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView list;
    private View mainFrag;
    private ProgressBar spinner;
    private TextView spinnerText;

    private FrameLayout searchFrame;
    private RelativeLayout spinnerFrame;

    private boolean searchIsVisible;

    private long delay = 5000;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainFrag = inflater.inflate(R.layout.fragment_stationlist, container, false);

        spinnerFrame = (RelativeLayout) mainFrag.findViewById(R.id.spinner_layout);
        spinner = (ProgressBar) mainFrag.findViewById(R.id.spinner);
        spinnerText = (TextView) mainFrag.findViewById(R.id.spinner_text);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                updateList();
            }
        }, delay);

        updateLoadingText();

        searchFrame = (FrameLayout) mainFrag.findViewById(R.id.searchFrame);
        searchFrame.setVisibility(FrameLayout.GONE);
        searchIsVisible = false;

        return mainFrag;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (searchIsVisible) {
            toggleSearch(true);
        }
    }

    public void updateList() {
        spinnerFrame.setVisibility(RelativeLayout.GONE);

        if (list == null) {
            list = (ListView) mainFrag.findViewById(R.id.stationlist);
            list.setOnItemClickListener(this);
        }
        list.setVisibility(View.VISIBLE);
        list.setAdapter(new WeatherStationsAdapter(getActivity()));
    }

    private void updateLoadingText() {
        Handler handler = new Handler();

        for (long i = 0; i < delay; i += 1000) {
            final long timer = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (timer == 0) {
                        spinnerText.setText(R.string.loadingText);
                    } else if (timer % 3000 == 1000) {
                        spinnerText.setText(R.string.loadingText1);
                    } else if (timer % 3000 == 2000) {
                        spinnerText.setText(R.string.loadingText2);
                    } else if (timer % 3000 == 0) {
                        spinnerText.setText(R.string.loadingText3);
                    }
                }
            }, i);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("Clicked on item",String.valueOf(position));
        startActivity(new Intent(getActivity(), WeatherStationTab.class));
    }

    public void toggleSearch(boolean shouldChange) {
        if (searchIsVisible) {
            searchFrame.animate()
                    .translationY(-1/2*searchFrame.getHeight())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            EditText searchField = (EditText) getView().findViewById(R.id.searchField);
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
                         }
                    });
            searchFrame.setVisibility(FrameLayout.GONE);
        } else {
            searchFrame.setVisibility(VISIBLE);
            searchFrame.animate()
                    .translationY(1/2*searchFrame.getHeight())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            EditText searchField = (EditText) getView().findViewById(R.id.searchField);
                            searchField.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(searchField, InputMethodManager.SHOW_IMPLICIT);
                        }
                    });
        }

        if (shouldChange) {
            searchIsVisible = !searchIsVisible;
        }
    }

    public boolean isSearchVisible() {
        return searchIsVisible;
    }

}
