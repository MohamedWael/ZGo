package com.blogspot.mowael.zgo.activities;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.blogspot.mowael.zgo.R;
import com.blogspot.mowael.zgo.fragments.LeftViewFragment;
import com.blogspot.mowael.zgo.fragments.MapFragment;

public class MapActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawer;
    private ImageButton ibNavDrawer;
    private FrameLayout flMapView;
    private LeftViewFragment leftViewFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initComponent();
        leftViewFragment = LeftViewFragment.newInstance("", "");
        getSupportFragmentManager().beginTransaction().replace(R.id.flLeftView, leftViewFragment).commit();

    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initComponent() {
        drawer = (DrawerLayout) findViewById(R.id.activity_map);
        ibNavDrawer = (ImageButton) findViewById(R.id.ibNavDrawer);
        ibNavDrawer.setOnClickListener(this);
        flMapView = (FrameLayout) findViewById(R.id.flMapView);

        mapFragment = MapFragment.newInstance("", "");
        getSupportFragmentManager().beginTransaction().replace(R.id.flMapView, mapFragment).commit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibNavDrawer:
                drawer.openDrawer(GravityCompat.START, true);
                break;
        }
    }
}
