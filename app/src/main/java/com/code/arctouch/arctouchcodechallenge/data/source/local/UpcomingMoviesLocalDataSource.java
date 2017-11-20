package com.code.arctouch.arctouchcodechallenge.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.code.arctouch.arctouchcodechallenge.data.source.UpcomingMoviesDataSource;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.UpcomingMovie;
import com.code.arctouch.arctouchcodechallenge.util.AppExecutors;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Concrete implementation of a data source as a db.
 */
public class UpcomingMoviesLocalDataSource implements UpcomingMoviesDataSource {

    private static volatile UpcomingMoviesLocalDataSource instance;
    private AppExecutors mAppExecutors;
    private UpcomingMoviesDao mUpcomingMoviesDao;

    // Private constructor to prevent direct instantiation.
    private UpcomingMoviesLocalDataSource(@NonNull AppExecutors appExecutors,
                                          @NonNull UpcomingMoviesDao upcomingMoviesDao) {
        mAppExecutors = appExecutors;
        mUpcomingMoviesDao = upcomingMoviesDao;
    }

    public static UpcomingMoviesLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                            @NonNull UpcomingMoviesDao upcomingMoviesDao) {
        if (instance == null) {
            synchronized (UpcomingMoviesLocalDataSource.class) {
                if (instance == null) {
                    instance = new UpcomingMoviesLocalDataSource(appExecutors, upcomingMoviesDao);
                }
            }
        }
        return instance;
    }

    @VisibleForTesting
    static void clearInstance() {
        instance = null;
    }

    @Override
    public void getUpcomingMovies(@NonNull final LoadUpcomingMoviesCallback callback) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<UpcomingMovie> upcomingMovies = mUpcomingMoviesDao.getUpcomingMovies();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (upcomingMovies.isEmpty()) {
                            // Table new or empty
                            callback.onDataNotAvailable();
                        } else {
                            // Return movies
                            callback.onUpcomingMoviesLoaded(upcomingMovies);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void saveUpcomingMovie(@NonNull final UpcomingMovie upcomingMovie) {
        checkNotNull(upcomingMovie);
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Insert movie
                mUpcomingMoviesDao.insertUpcomingMovie(upcomingMovie);
            }
        });
    }

    @Override
    public void refreshUpcomingMovies() {
        // Do nothing
    }

    @Override
    public void deleteAllUpcomingMovies() {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Delete all movies from database
                mUpcomingMoviesDao.deleteUpcomingMovies();
            }
        });
    }
}
