package com.code.arctouch.arctouchcodechallenge;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.code.arctouch.arctouchcodechallenge.upcomingmovies.UpcomingMoviesFilterType;
import com.code.arctouch.arctouchcodechallenge.upcomingmovies.UpcomingMoviesFragment;
import com.code.arctouch.arctouchcodechallenge.upcomingmovies.UpcomingMoviesPresenter;
import com.code.arctouch.arctouchcodechallenge.util.ActivityUtils;
import com.code.arctouch.arctouchcodechallenge.util.EspressoIdlingResource;

public class MainActivity extends AppCompatActivity {

    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";
    private UpcomingMoviesPresenter mUpcomingMoviesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        UpcomingMoviesFragment upcomingMoviesFragment =
                (UpcomingMoviesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (upcomingMoviesFragment == null) {
            // Create the fragment
            upcomingMoviesFragment = UpcomingMoviesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), upcomingMoviesFragment, R.id.contentFrame);
        }

        // Create the presenter
        mUpcomingMoviesPresenter = new UpcomingMoviesPresenter(
                Injection.provideUseCaseHandler(),
                upcomingMoviesFragment,
                Injection.provideGetUpcomingMovies(getApplicationContext())
        );

        // Load previously saved state, if available.
        if (savedInstanceState != null) {
            UpcomingMoviesFilterType currentFiltering =
                    (UpcomingMoviesFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
            mUpcomingMoviesPresenter.setFiltering(currentFiltering);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_FILTERING_KEY, mUpcomingMoviesPresenter.getFiltering());

        super.onSaveInstanceState(outState);
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }


}
