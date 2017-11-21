package com.code.arctouch.arctouchcodechallenge.data;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.code.arctouch.arctouchcodechallenge.data.source.UpcomingMoviesDataSource;
import com.google.common.collect.Lists;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
public class FakeUpcomingMoviesRemoteDataSource implements UpcomingMoviesDataSource {

    private static final Map<String, UpcomingMovie> TASKS_SERVICE_DATA = new LinkedHashMap<>();
    private static FakeUpcomingMoviesRemoteDataSource INSTANCE;

    // Prevent direct instantiation.
    private FakeUpcomingMoviesRemoteDataSource() {
    }

    public static FakeUpcomingMoviesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeUpcomingMoviesRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getUpcomingMovies(@NonNull LoadUpcomingMoviesCallback callback) {
        callback.onUpcomingMoviesLoaded(Lists.newArrayList(TASKS_SERVICE_DATA.values()));
    }

    @Override
    public void getUpcomingMovie(@NonNull String upcomingMovieId, @NonNull GetUpcomingMovieCallback callback) {
        UpcomingMovie upcomingMovie = TASKS_SERVICE_DATA.get(upcomingMovieId);
        callback.onUpcomingMovieLoaded(upcomingMovie);
    }

    @Override
    public void saveUpcomingMovie(@NonNull UpcomingMovie upcomingMovie) {
        TASKS_SERVICE_DATA.put(upcomingMovie.getId(), upcomingMovie);
    }

    public void refreshUpcomingMovies() {
        // Not required because the {@link MoviesRepository} handles the logic of refreshing the
        // upcomingMovies from all the available data sources.
    }

    @Override
    public void deleteUpcomingMovie(@NonNull String upcomingMovieId) {
        TASKS_SERVICE_DATA.remove(upcomingMovieId);
    }

    @Override
    public void deleteAllUpcomingMovies() {
        TASKS_SERVICE_DATA.clear();
    }

    @VisibleForTesting
    public void addUpcomingMovies(UpcomingMovie... upcomingMovies) {
        for (UpcomingMovie upcomingMovie : upcomingMovies) {
            TASKS_SERVICE_DATA.put(upcomingMovie.getId(), upcomingMovie);
        }
    }
}
