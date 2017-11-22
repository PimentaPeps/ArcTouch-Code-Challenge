package com.code.arctouch.arctouchcodechallenge.data;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.code.arctouch.arctouchcodechallenge.data.source.MoviesDataSource;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.Movie;
import com.google.common.collect.Lists;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of the data source that adds a latency simulating network.
 */
public class MoviesRemoteDataSource implements MoviesDataSource {

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;
    private static final Map<String, Movie> TASKS_SERVICE_DATA;
    private static MoviesRemoteDataSource INSTANCE;

    static {
        TASKS_SERVICE_DATA = new LinkedHashMap<>(2);
        addUpcomingMovie("Build tower in Pisa", "Ground looks good, no foundation work required.");
        addUpcomingMovie("Finish bridge in Tacoma", "Found awesome girders at half the cost!");
    }

    // Prevent direct instantiation.
    private MoviesRemoteDataSource() {
    }

    public static MoviesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRemoteDataSource();
        }
        return INSTANCE;
    }

    private static void addUpcomingMovie(String title, String description) {
        Movie newMovie = new Movie();
        newMovie.setId(1);
        TASKS_SERVICE_DATA.put(String.valueOf(newMovie.getId()), newMovie);
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

    @Override
    public void getMovieDetail(@NonNull LoadMovieCallback callback, String movieId) {

    }

    @Override
    public void saveMovie(@NonNull Movie movie) {

    }

    @Override
    public void refreshUpcomingMovies() {
        // Not required because the {@link MoviesRepository} handles the logic of refreshing the
        // upcomingMovies from all the available data sources.
    }

    @Override
    public void deleteAllMovies() {

    }
}
