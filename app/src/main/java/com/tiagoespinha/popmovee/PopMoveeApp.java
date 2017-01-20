package com.tiagoespinha.popmovee;

import android.app.Application;

import com.tiagoespinha.popmovee.services.DaggerNetIOComponent;
import com.tiagoespinha.popmovee.services.NetIOComponent;
import com.tiagoespinha.popmovee.services.TMDBService;
import com.tiagoespinha.popmovee.services.TMDBServiceModule;

/**
 * Created by tiago on 19/01/2017.
 */

public class PopMoveeApp extends Application {
    private NetIOComponent mNetIOComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mNetIOComponent = DaggerNetIOComponent.create();
    }

    public NetIOComponent getNetIOComponent() {
        return mNetIOComponent;
    }
}
