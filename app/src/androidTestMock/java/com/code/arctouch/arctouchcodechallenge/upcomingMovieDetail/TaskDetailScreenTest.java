package com.code.arctouch.arctouchcodechallenge.upcomingMovieDetail;

import android.app.Activity;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.code.arctouch.arctouchcodechallenge.data.source.UpcomingMoviesRepository;
import com.code.arctouch.arctouchcodechallenge.util.EspressoIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

/**
 * Tests for the upcomingMovies screen, the main screen which contains a list of all upcomingMovies.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class UpcomingMovieDetailScreenTest {

    private static String UPCOMING_MOVIE_TITLE = "ATSL";

    private static String UPCOMING_MOVIE_DESCRIPTION = "Rocks";

    /**
     * {@link UpcomingMovie} stub that is added to the fake service API layer.
     */
    private static UpcomingMovie ACTIVE_UPCOMING_MOVIE = new UpcomingMovie(UPCOMING_MOVIE_TITLE, UPCOMING_MOVIE_DESCRIPTION, false);

    /**
     * {@link UpcomingMovie} stub that is added to the fake service API layer.
     */
    private static UpcomingMovie COMPLETED_UPCOMING_MOVIE = new UpcomingMovie(UPCOMING_MOVIE_TITLE, UPCOMING_MOVIE_DESCRIPTION, true);

    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     * <p>
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     * <p>
     * <p>
     * Sometimes an {@link Activity} requires a custom start {@link Intent} to receive data
     * from the source Activity. ActivityTestRule has a feature which let's you lazily start the
     * Activity under test, so you can control the Intent that is used to start the target Activity.
     */
    @Rule
    public ActivityTestRule<UpcomingMovieDetailActivity> mUpcomingMovieDetailActivityTestRule =
            new ActivityTestRule<>(UpcomingMovieDetailActivity.class, true /* Initial touch mode  */,
                    false /* Lazily launch activity */);


    /**
     * Prepare your test fixture for this test. In this case we register an IdlingResources with
     * Espresso. IdlingResource resource is a great way to tell Espresso when your app is in an
     * idle state. This helps Espresso to synchronize your test actions, which makes tests
     * significantly more reliable.
     */
    @Before
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(EspressoIdlingResource.getIdlingResource());
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(EspressoIdlingResource.getIdlingResource());
    }

    private void loadActiveUpcomingMovie() {
        startActivityWithWithStubbedUpcomingMovie(ACTIVE_UPCOMING_MOVIE);
    }

    private void loadCompletedUpcomingMovie() {
        startActivityWithWithStubbedUpcomingMovie(COMPLETED_UPCOMING_MOVIE);
    }

    /**
     * Setup your test fixture with a fake upcomingMovie id. The {@link UpcomingMovieDetailActivity} is started with
     * a particular upcomingMovie id, which is then loaded from the service API.
     * <p>
     * <p>
     * Note that this test runs hermetically and is fully isolated using a fake implementation of
     * the service API. This is a great way to make your tests more reliable and faster at the same
     * time, since they are isolated from any outside dependencies.
     */
    private void startActivityWithWithStubbedUpcomingMovie(UpcomingMovie upcomingMovie) {
        // Add a upcomingMovie stub to the fake service api layer.
        UpcomingMoviesRepository.destroyInstance();
        FakeUpcomingMoviesRemoteDataSource.getInstance().addUpcomingMovies(upcomingMovie);

        // Lazily start the Activity from the ActivityTestRule this time to inject the start Intent
        Intent startIntent = new Intent();
        startIntent.putExtra(UpcomingMovieDetailActivity.EXTRA_UPCOMING_MOVIE_ID, upcomingMovie.getId());
        mUpcomingMovieDetailActivityTestRule.launchActivity(startIntent);
    }

    @Test
    public void activeUpcomingMovieDetails_DisplayedInUi() throws Exception {
        loadActiveUpcomingMovie();

        // Check that the upcomingMovie title and description are displayed
        onView(withId(R.id.upcomingMovie_detail_title)).check(matches(withText(UPCOMING_MOVIE_TITLE)));
        onView(withId(R.id.upcomingMovie_detail_description)).check(matches(withText(UPCOMING_MOVIE_DESCRIPTION)));
        onView(withId(R.id.upcomingMovie_detail_complete)).check(matches(not(isChecked())));
    }

    @Test
    public void completedUpcomingMovieDetails_DisplayedInUi() throws Exception {
        loadCompletedUpcomingMovie();

        // Check that the upcomingMovie title and description are displayed
        onView(withId(R.id.upcomingMovie_detail_title)).check(matches(withText(UPCOMING_MOVIE_TITLE)));
        onView(withId(R.id.upcomingMovie_detail_description)).check(matches(withText(UPCOMING_MOVIE_DESCRIPTION)));
        onView(withId(R.id.upcomingMovie_detail_complete)).check(matches(isChecked()));
    }

    @Test
    public void orientationChange_menuAndUpcomingMoviePersist() {
        loadActiveUpcomingMovie();

        // Check delete menu item is displayed and is unique
        onView(withId(R.id.menu_delete)).check(matches(isDisplayed()));

        TestUtils.rotateOrientation(mUpcomingMovieDetailActivityTestRule.getActivity());

        // Check that the upcomingMovie is shown
        onView(withId(R.id.upcomingMovie_detail_title)).check(matches(withText(UPCOMING_MOVIE_TITLE)));
        onView(withId(R.id.upcomingMovie_detail_description)).check(matches(withText(UPCOMING_MOVIE_DESCRIPTION)));

        // Check delete menu item is displayed and is unique
        onView(withId(R.id.menu_delete)).check(matches(isDisplayed()));
    }

}
