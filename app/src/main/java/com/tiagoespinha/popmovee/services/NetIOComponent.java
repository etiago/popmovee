package com.tiagoespinha.popmovee.services;

import com.tiagoespinha.popmovee.MainActivity;

import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by tiago on 19/01/2017.
 */
@Singleton
@Component(modules={TMDBServiceModule.class})
public interface NetIOComponent {
    void inject(MainActivity mainActivity);
}
