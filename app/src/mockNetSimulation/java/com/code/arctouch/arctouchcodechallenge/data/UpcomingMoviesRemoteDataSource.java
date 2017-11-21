package com.code.arctouch.arctouchcodechallenge.data;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.code.arctouch.arctouchcodechallenge.data.source.UpcomingMoviesDataSource;
import com.google.common.collect.Lists;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of the data source that adds a latency simulating network.
 */
public class UpcomingMoviesRemoteDataSource implements UpcomingMoviesDataSource {

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;
    private static final Map<String, UpcomingMovie> TASKS_SERVICE_DATA;
    private static UpcomingMoviesRemoteDataSource INSTANCE;

    static {
        TASKS_SERVICE_DATA = new LinkedHashMap<>(2);
        addUpcomingMovie("Build tower in Pisa", "Ground looks good, no foundation work required.");
        addUpcomingMovie("Finish bridge in Tacoma", "Found awesome girders at half the cost!");
    }

    // Prevent direct instantiation.
    private UpcomingMoviesRemoteDataSource() {
    }

    public static UpcomingMoviesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UpcomingMoviesRemoteDataSource();
        }
        return INSTANCE;
    }

    private static void addUpcomingMovie(String title, String description) {
        UpcomingMovie newUpcomingMovie = new UpcomingMovie(title, description);
        TASKS_SERVICE_DATA.put(newUpcomingMovie.getId(), newUpcomingMovie);
    }

    /**
     * Note: {@link LoadUpcomingMoviesCallback#onDataNotAvailable()} is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    @Override
    public void getUpcomingMovies(final @NonNull LoadUpcomingMoviesCallback callback) {
        // Simulate network by delaying the execution.
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onUpcomingMoviesLoaded(Lists.newArrayList(TASKS_SERVICE_DATA.values()));
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    /**
     * Note: {@link GetUpcomingMovieCallback#onDataNotAvailable()} is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    @Override
    public void getUpcomingMovie(@NonNull String upcomingMovieId, final @NonNull GetUpcomingMovieCallback callback) {
        final UpcomingMovie upcomingMovie = TASKS_SERVICE_DATA.get(upcomingMovieId);

        // Simulate network by delaying the execution.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onUpcomingMovieLoaded(upcomingMovie);
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    @Override
    public void saveUpcomingMovie(@NonNull UpcomingMovie upcomingMovie) {
        TASKS_SERVICE_DATA.put(upcomingMovie.getId(), upcomingMovie);
    }

    @Override
    public void refreshUpcomingMovies() {
        // Not required because the {@link MoviesRepository} handles the logic of refreshing the
        // upcomingMovies from all the available data sources.
    }

    @Override
    public void deleteAllUpcomingMovies() {
        TASKS_SERVICE_DATA.clear();
    }

    @Override
    public void deleteUpcomingMovie(@NonNull String upcomingMovieId) {
        TASKS_SERVICE_DATA.remove(upcomingMovieId);
    }
}
