package com.tiagoespinha.popmovee.ioc.modules;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tiagoespinha.popmovee.R;
import com.tiagoespinha.popmovee.model.MovieMetadata;

import java.util.function.Function;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.functions.Consumer;

@Module
public class MovieDetailsActivityModule {
    @Provides
    @Singleton
    public Function<Activity, Consumer<Throwable>> provideMovieDetailsFetchErrorHandler() {
        return activity -> new Consumer<Throwable>() {
            Toast toast = null;

            @Override
            public void accept(Throwable throwable) throws Exception {
                Context context = activity.getApplicationContext();
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(context,
                        context.getString(R.string.error_loading_movies_showing_favorites),
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        };
    }

    @Provides
    @Singleton
    @Named("SavePosterToFileTarget")
    public Function<MovieMetadata, Target> provideSavePosterToFileTarget() {
        return (movieMetadata) -> new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
    }
}
