package com.blogspot.mowael.zgo.utilities;

import android.support.multidex.MultiDexApplication;

/**
 * Created by moham on 1/9/2017.
 */

public class BaseApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseHelper databaseHelper = DatabaseHelper.newInstance(this);
        VolleySingleton volley = VolleySingleton.getInstance(this);
        volley.getRequestQueue().start();
    }
}
