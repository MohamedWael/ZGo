package com.blogspot.mowael.zgo.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.blogspot.mowael.zgo.R;
import com.blogspot.mowael.zgo.fragments.SignInFragment;
import com.blogspot.mowael.zgo.utilities.BackGroundService;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;

public class MainActivity extends AppCompatActivity {

    private SignInFragment signInFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, BackGroundService.class));
        signInFragment = SignInFragment.newInstance("","");
        getSupportFragmentManager().beginTransaction().replace(R.id.flMain, signInFragment).commit();
    }

    // Method to stop the service
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), BackGroundService.class));
    }
}
