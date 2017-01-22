package com.tiagoespinha.popmovee.consumers;

import android.util.Log;

import io.reactivex.functions.Consumer;

/**
 * Created by tiago on 22/01/2017.
 */

public class ThrowableConsumerMainActivity implements Consumer<Throwable> {
    @Override
    public void accept(Throwable throwable) throws Exception {
        Log.e(getClass().getSimpleName(), "Failed to consume observer in main activity!", throwable);
    }
}
