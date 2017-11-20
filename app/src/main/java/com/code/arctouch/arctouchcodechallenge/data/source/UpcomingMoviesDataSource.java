package com.code.arctouch.arctouchcodechallenge.data.source;

import android.support.annotation.NonNull;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.UpcomingMovie;

import java.util.List;

public interface UpcomingMoviesDataSource {


    void getUpcomingMovies(@NonNull LoadUpcomingMoviesCallback callback);

    void saveUpcomingMovie(@NonNull UpcomingMovie upcomingMovie);

    void refreshUpcomingMovies();

    void deleteAllUpcomingMovies();

    interface LoadUpcomingMoviesCallback {

        void onUpcomingMoviesLoaded(List<UpcomingMovie> upcomingMovies);

        void onDataNotAvailable();
    }
}
