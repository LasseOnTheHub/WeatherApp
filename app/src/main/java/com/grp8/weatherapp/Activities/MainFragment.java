package com.grp8.weatherapp.Activities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;


import com.grp8.weatherapp.R;

/**
 * Created by Frederik on 14/11/2016.
 */

public class MainFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView list;
    private FrameLayout searchFrame;
    private boolean searchIsVisible;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainFrag = inflater.inflate(R.layout.fragment_stationlist, container, false);

        list = (ListView) mainFrag.findViewById(R.id.stationlist);
        list.setAdapter(new WeatherStationsAdapter(getActivity()));
        list.setOnItemClickListener(this);

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("Clicked on item",String.valueOf(position));
        startActivity(new Intent(getActivity(), StationOverviewActivity.class));
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
            searchFrame.setVisibility(FrameLayout.VISIBLE);
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
