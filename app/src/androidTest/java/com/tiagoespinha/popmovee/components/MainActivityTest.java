package com.tiagoespinha.popmovee.components;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.tiagoespinha.popmovee.MainActivity;
import com.tiagoespinha.popmovee.PopMoveeApp;
import com.tiagoespinha.popmovee.services.DaggerMainActivityComponent;
import com.tiagoespinha.popmovee.services.MainActivityComponent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by tiago on 24/01/2017.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {


//    @Rule
//    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
//            MainActivity.class,
//            true,     // initialTouchMode
//            false);   // launchActivity.

    @Before
    public void setUp() {
//        PopMoveeApp popMoveeApp = (PopMoveeApp) InstrumentationRegistry
//                .getInstrumentation()
//                .getTargetContext();



//        DaggerTestMainActivityComponent
//                .builder()
//                .testMainActivityModule(new TestMainActivityModule(popMoveeApp.getApplicationContext()))
//                .build();



//        DaggerMainActivityComponent
//                .builder()
//                .mainActivityModule(new TestMainActivityModule(popMoveeApp.getApplicationContext()))
//                .build();
    }
}
