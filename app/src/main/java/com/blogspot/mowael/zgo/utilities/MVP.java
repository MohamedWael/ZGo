package com.blogspot.mowael.zgo.utilities;

import android.content.Context;

/**
 * Created by moham on 1/9/2017.
 */

public interface MVP {

    interface ModelToPresenter {

    }

    interface PresenterToModel {
        Context getAppContext();

        Context getActivityContext();
    }

    interface PresenterToView {

    }

    interface ViewToPresenter {
        Context getAppContext();

        Context getActivityContext();
    }

}
