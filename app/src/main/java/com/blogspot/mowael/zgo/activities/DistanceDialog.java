package com.blogspot.mowael.zgo.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.blogspot.mowael.zgo.R;
import com.blogspot.mowael.zgo.dataModel.MeasureData;
import com.blogspot.mowael.zgo.fragments.MapFragment;
import com.blogspot.mowael.zgo.utilities.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by moham on 1/12/2017.
 */

public class DistanceDialog extends Dialog implements MapFragment.DistanceDialogResult, MapFragment.MapResponseArriveListener {


    private final FragmentActivity activity;
    private Context context;
    private DatabaseHelper dbHelper;
    private ProgressBar progressBar;
    private TextView tvFrom, tvTo, tvResultOfDirectDistance, tvDurationValue, tvDurationInTrafficValue;
    private ViewSwitcher viewSwitcher;
    private boolean isNextView = false;

    public DistanceDialog(FragmentActivity activity, Context context) {
        super(context);
        dbHelper = DatabaseHelper.newInstance(context);
        setTitle("Search Result");
        this.activity = activity;
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distance_dialog_layout);
        initComponents();
        if (isNextView) {
            viewSwitcher.showPrevious();
            isNextView = false;
        }
        progressBar.setVisibility(View.VISIBLE);
    }

    private void initComponents() {
        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvFrom = (TextView) findViewById(R.id.tvFrom);
        tvTo = (TextView) findViewById(R.id.tvTo);
        tvResultOfDirectDistance = (TextView) findViewById(R.id.tvResultOfDirectDistance);
        tvDurationValue = (TextView) findViewById(R.id.tvDurationValue);
        tvDurationInTrafficValue = (TextView) findViewById(R.id.tvDurationInTrafficValue);
    }

    @Override
    public void OnDialogResult(int requestCode, int resultCode, Intent data) {
//values from OnActivityResult
    }

    @Override
    public void onMapResponseArriveListener(JSONObject response, MeasureData data) {
        try {
            JSONArray routes = response.getJSONArray("routes");
            JSONArray legs = routes.getJSONObject(0).getJSONArray("legs");
            JSONObject distance = legs.getJSONObject(0).getJSONObject("distance");
            JSONObject duration = legs.getJSONObject(0).getJSONObject("duration");
            JSONObject durationInTraffic = legs.getJSONObject(0).getJSONObject("duration_in_traffic");
            String to = legs.getJSONObject(0).getString("end_address");
            String from = legs.getJSONObject(0).getString("start_address");
            String summary = routes.getJSONObject(0).getString("summary");
            setTitle(summary);
            data.setSummary(summary);
            tvFrom.setText(from);
            data.setOrigin(from);
            tvTo.setText(to);
            data.setDestination(to);
            String distanceStr = distance.getString("text");
            tvResultOfDirectDistance.setText(distanceStr);
            data.setTravelDistance(distanceStr);
            String durationStr = duration.getString("text");
            tvDurationValue.setText(durationStr);
            data.setDuration(durationStr);
            String duarationInTrafficStr = durationInTraffic.getString("text");
            tvDurationInTrafficValue.setText(duarationInTrafficStr);
            data.setDurationInTraffic(duarationInTrafficStr);
            viewSwitcher.showNext();
            isNextView = true;
            dbHelper.addMeasureData(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
