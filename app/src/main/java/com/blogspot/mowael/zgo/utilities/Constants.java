package com.blogspot.mowael.zgo.utilities;

/**
 * Created by moham on 1/9/2017.
 */

public interface Constants {
    int SPLASH_TIME_OUT = 3000;
    int GALLERY_INTENT_CALLED = 1;
    int GALLERY_KITKAT_INTENT_CALLED = 2;

    String SHARED_PREFERENCES_NAME = "LoginCridentials";
    String USER_NAME = "userName";
    String PASSWORD = "password";
    String EMAIL = "email";
    String LOGIN_AUTOMATICALLY = "loginAutomatically";
    String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    String REGEX_Mail = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

    String IMAGE_URI = "profilePicPath";


    String GOOGLE_MAPS_API = "https://maps.googleapis.com/maps/api/directions/json";
    String ORIGIN = "?origin=";
    String DESTINATION = "&destination=";
    String SENSOR = "&sensor=";
    String KEY = "&key=";
    String DEPARTURE_TIME = "&departure_time=";
    String TRAFFIC_MODE = "&traffic_model=best_guess";
    String WAY_POINT = "&waypoints=optimize:true|Providence";

}
