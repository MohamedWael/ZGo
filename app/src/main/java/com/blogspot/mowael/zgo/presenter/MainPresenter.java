package com.blogspot.mowael.zgo.presenter;

import android.content.Context;

import com.blogspot.mowael.zgo.utilities.MVP;

import java.lang.ref.WeakReference;

/**
 * Created by moham on 1/9/2017.
 */

public class MainPresenter implements MVP.PresenterToModel, MVP.PresenterToView {

    private MVP.ViewToPresenter view;
    private WeakReference<MVP.ViewToPresenter> weakReferenceView;
    private MVP.ModelToPresenter model;

    public MainPresenter(MVP.ViewToPresenter view) {
        weakReferenceView = new WeakReference<>(view);
        this.view = getView();
    }

    MVP.ViewToPresenter getView() {
        return weakReferenceView.get();
    }

    @Override
    public Context getAppContext() {
        return view.getAppContext();
    }

    @Override
    public Context getActivityContext() {
        return view.getActivityContext();
    }
}
