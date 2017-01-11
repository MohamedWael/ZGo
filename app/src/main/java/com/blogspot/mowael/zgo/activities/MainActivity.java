package com.blogspot.mowael.zgo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.blogspot.mowael.zgo.R;
import com.blogspot.mowael.zgo.fragments.SignInFragment;

public class MainActivity extends AppCompatActivity {

    private SignInFragment signInFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signInFragment = SignInFragment.newInstance("","");
        getSupportFragmentManager().beginTransaction().replace(R.id.flMain, signInFragment).commit();
    }
}
