package com.tiagoespinha.popmovee.services;

import android.content.Context;

import com.tiagoespinha.popmovee.MainActivity;

import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by tiago on 19/01/2017.
 */
@Singleton
@Component(modules={TMDBServiceModule.class, MainActivityModule.class})
public interface MainActivityComponent {
    Context context();
    void inject(MainActivity mainActivity);
}
