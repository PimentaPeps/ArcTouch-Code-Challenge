package com.code.arctouch.arctouchcodechallenge.data.source.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.code.arctouch.arctouchcodechallenge.data.source.UpcomingMoviesDataSource;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.UpcomingMovie;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.core.ResponsePage;
import com.code.arctouch.arctouchcodechallenge.util.AppExecutors;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of a data source as a remote data source.
 */
public class UpcomingMoviesRemoteDataSource implements UpcomingMoviesDataSource {

    private static UpcomingMoviesRemoteDataSource INSTANCE;
    private AppExecutors mAppExecutors;

    // Private constructor to prevent direct instantiation.
    private UpcomingMoviesRemoteDataSource(@NonNull AppExecutors appExecutors) {
        mAppExecutors = appExecutors;
    }

    public static UpcomingMoviesRemoteDataSource getInstance(@NonNull AppExecutors appExecutors) {
        if (INSTANCE == null) {
            INSTANCE = new UpcomingMoviesRemoteDataSource(appExecutors);
        }
        return INSTANCE;
    }

    /**
     * Note: {@link LoadUpcomingMoviesCallback#onDataNotAvailable()} is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    @Override
    public void getUpcomingMovies(final @NonNull LoadUpcomingMoviesCallback callback) {

        try {
            Movies movies = Api.getInstance().getMovies();
            final List<UpcomingMovie> list = new ArrayList<>();
            ResponsePage responsePage = null;
            do {
                responsePage = movies.getUpcomingMovies("en", responsePage == null ? 1 : responsePage.getPage() + 1);
                list.addAll(responsePage.getResults());
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
    public void saveUpcomingMovie(@NonNull UpcomingMovie upcomingMovie) {
        //Do nothing
    }

    @Override
    public void refreshUpcomingMovies() {
        //Do nothing
    }

    @Override
    public void deleteAllUpcomingMovies() {
        //Do nothing
    }
}
