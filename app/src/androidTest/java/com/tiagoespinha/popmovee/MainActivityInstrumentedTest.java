package com.tiagoespinha.popmovee;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Spinner;

import com.tiagoespinha.popmovee.adapters.MovieThumbnailAdapter;
import com.tiagoespinha.popmovee.model.TMDBMovieResultSet;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.functions.Consumer;
import io.reactivex.subscribers.TestSubscriber;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void selectedMovieListingIsPopularMovies() throws Exception {
        final String popularMovies = mActivityRule.getActivity().getString(R.string.most_popular_movies);

        onView(withId(R.id.spinner_nav)).check(matches(withSpinnerText(popularMovies)));
    }

    @Test
    public void initialMoviesAreInView() throws Exception {
        onView(withId(R.id.rv_movie_grid)).check((view, noViewFoundException) -> {
            //mActivityRule.getActivity().mNavigationSpinnerOnItemSelectedListener.getMovieResultSetObservable().test().assertSubscribed().onComplete();
            assertEquals("Did not find 20 items!",20,((RecyclerView)view).getLayoutManager().getItemCount());
        });
    }

    @Test
    public void scrollingOnViewLoadsMoreMovies() throws Exception {
        onView(withId(R.id.rv_movie_grid))
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp());

        onView(withId(R.id.rv_movie_grid)).check((view, noViewFoundException) -> {
            assertTrue(((RecyclerView)view).getLayoutManager().getItemCount() > 20);});
    }
}
