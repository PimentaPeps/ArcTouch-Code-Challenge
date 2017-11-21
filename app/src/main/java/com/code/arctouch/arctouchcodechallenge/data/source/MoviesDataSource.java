package com.code.arctouch.arctouchcodechallenge.data.source;

import android.support.annotation.NonNull;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.Movie;

import java.util.List;

public interface MoviesDataSource {


    void getUpcomingMovies(@NonNull LoadUpcomingMoviesCallback callback);

    void getMovieDetail(@NonNull LoadMovieCallback callback, String movieId);

    void saveMovie(@NonNull Movie movie);

    void refreshUpcomingMovies();

    void deleteAllMovies();

    interface LoadUpcomingMoviesCallback {

        void onUpcomingMoviesLoaded(List<Movie> movies);

        void onDataNotAvailable();
    }

    interface LoadMovieCallback {

        void onMovieLoaded(Movie movie);

        void onDataNotAvailable(String movieId);
    }
}
