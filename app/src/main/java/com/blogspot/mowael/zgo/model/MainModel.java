package com.blogspot.mowael.zgo.model;

import android.content.Context;

import com.blogspot.mowael.zgo.utilities.MVP;

/**
 * Created by moham on 1/9/2017.
 */

public class MainModel implements MVP.ModelToPresenter {
    private MVP.PresenterToModel presenter;
    private Context appContext, ActivityContext;

    public MainModel(MVP.PresenterToModel presenter) {
        this.presenter = presenter;
        appContext = this.presenter.getAppContext();
        ActivityContext = this.presenter.getActivityContext();
    }
}
