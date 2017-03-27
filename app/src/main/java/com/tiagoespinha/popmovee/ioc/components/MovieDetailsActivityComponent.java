package com.tiagoespinha.popmovee.ioc.components;

import com.tiagoespinha.popmovee.MovieDetailsActivity;
import com.tiagoespinha.popmovee.ioc.modules.MovieDetailsActivityModule;
import com.tiagoespinha.popmovee.ioc.modules.TMDBServiceModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules={TMDBServiceModule.class, MovieDetailsActivityModule.class})
public interface MovieDetailsActivityComponent {
    void inject(MovieDetailsActivity movieDetailsActivity);
}
