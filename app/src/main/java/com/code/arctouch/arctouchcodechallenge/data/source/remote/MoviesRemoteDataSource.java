package com.code.arctouch.arctouchcodechallenge.data.source.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.code.arctouch.arctouchcodechallenge.data.source.MoviesDataSource;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.Movie;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.core.ResponsePage;
import com.code.arctouch.arctouchcodechallenge.util.AppExecutors;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of a data source as a remote data source.
 */
public class MoviesRemoteDataSource implements MoviesDataSource {

    private static MoviesRemoteDataSource INSTANCE;
    private AppExecutors mAppExecutors;

    // Private constructor to prevent direct instantiation.
    private MoviesRemoteDataSource(@NonNull AppExecutors appExecutors) {
        mAppExecutors = appExecutors;
    }

    public static MoviesRemoteDataSource getInstance(@NonNull AppExecutors appExecutors) {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRemoteDataSource(appExecutors);
        }
        return INSTANCE;
    }

    /**
     * Note: {@link LoadMoviesCallback#onDataNotAvailable()} is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    @Override
    public void getUpcomingMovies(final @NonNull LoadUpcomingMoviesCallback callback) {

        try {
            Movies movies = Api.getInstance().getMovies();
            final List<Movie> list = new ArrayList<>();
            ResponsePage responsePage = null;
            do {
                responsePage = movies.getUpcomingMovies("", responsePage == null ? 0 : responsePage.getPage() + 1);
                List<Movie> internalList = responsePage.getResults();
                for (Movie movie : internalList) {
                    for (int i = 0; i < movie.getGenre_ids().size(); i++) {
                        movie.getGenre_ids()
                                .set(i, Api.getInstance().getGenreName(Integer.valueOf(movie.getGenre_ids().get(i))));
                    }
                }
                list.addAll(internalList);
            } while (responsePage.getTotalPages() > responsePage.getPage());

            mAppExecutors.networkIO().execute(new Runnable() {
                @Override
                public void run() {
                    callback.onUpcomingMoviesLoaded(list);
                }
            });
        } catch (Exception e) {
            Log.e("SyncError", "getUpcomingMovies: ", e);
            mAppExecutors.networkIO().execute(new Runnable() {
                @Override
                public void run() {
                    callback.onDataNotAvailable();
                }
            });
        }
    }

    @Override
    public void getMovieDetail(@NonNull final LoadMovieCallback callback, @NonNull final String movieId) {
        try {
            Movies movies = Api.getInstance().getMovies();
            final Movie movie = movies.getMovie("", movieId);

            mAppExecutors.networkIO().execute(new Runnable() {
                @Override
                public void run() {
                    callback.onMovieLoaded(movie);
                }
            });
        } catch (Exception e) {
            Log.e("SyncError", "getUpcomingMovies: ", e);
            mAppExecutors.networkIO().execute(new Runnable() {
                @Override
                public void run() {
                    callback.onDataNotAvailable(movieId);
                }
            });
        }
    }

    @Override
    public void saveMovie(@NonNull Movie movie) {
        //Do nothing
    }

    @Override
    public void refreshUpcomingMovies() {
        //Do nothing
    }

    @Override
    public void deleteAllMovies() {
        //Do nothing
    }
}
