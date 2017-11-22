package com.code.arctouch.arctouchcodechallenge.data;

import android.support.annotation.NonNull;

import com.code.arctouch.arctouchcodechallenge.data.source.MoviesDataSource;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.Movie;
import com.google.common.collect.Lists;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
public class FakeMoviesRemoteDataSource implements MoviesDataSource {

    private static final Map<String, Movie> TASKS_SERVICE_DATA = new LinkedHashMap<>();
    private static FakeMoviesRemoteDataSource INSTANCE;

    // Prevent direct instantiation.
    private FakeMoviesRemoteDataSource() {
    }

    public static FakeMoviesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeMoviesRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getUpcomingMovies(@NonNull MoviesDataSource.LoadUpcomingMoviesCallback callback) {
        callback.onUpcomingMoviesLoaded(Lists.newArrayList(TASKS_SERVICE_DATA.values()));
    }

    @Override
    public void getMovieDetail(@NonNull LoadMovieCallback callback, String movieId) {

    }

    @Override
    public void saveMovie(@NonNull Movie movie) {
        TASKS_SERVICE_DATA.put(String.valueOf(movie.getId()), movie);
    }

    public void refreshUpcomingMovies() {
        // Not required because the {@link MoviesRepository} handles the logic of refreshing the
        // upcomingMovies from all the available data sources.
    }

    @Override
    public void deleteAllMovies() {
        TASKS_SERVICE_DATA.clear();
    }

}
