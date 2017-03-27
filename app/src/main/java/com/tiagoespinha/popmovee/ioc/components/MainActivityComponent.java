package com.tiagoespinha.popmovee.ioc.components;

import android.content.Context;

import com.tiagoespinha.popmovee.MainActivity;
import com.tiagoespinha.popmovee.MovieDetailsActivity;
import com.tiagoespinha.popmovee.ioc.modules.MainActivityModule;
import com.tiagoespinha.popmovee.ioc.modules.TMDBServiceModule;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules={TMDBServiceModule.class, MainActivityModule.class})
public interface MainActivityComponent {
    Context context();
    void inject(MainActivity mainActivity);
    //void inject(MovieDetailsActivity movieDetailsActivity);
}
