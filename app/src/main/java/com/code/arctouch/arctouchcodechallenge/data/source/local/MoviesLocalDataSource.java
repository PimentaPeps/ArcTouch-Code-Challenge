package com.code.arctouch.arctouchcodechallenge.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.code.arctouch.arctouchcodechallenge.data.source.MoviesDataSource;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.Movie;
import com.code.arctouch.arctouchcodechallenge.util.AppExecutors;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Concrete implementation of a data source as a db.
 */
public class MoviesLocalDataSource implements MoviesDataSource {

    private static volatile MoviesLocalDataSource instance;
    private AppExecutors mAppExecutors;
    private UpcomingMoviesDao mUpcomingMoviesDao;

    // Private constructor to prevent direct instantiation.
    private MoviesLocalDataSource(@NonNull AppExecutors appExecutors,
                                  @NonNull UpcomingMoviesDao upcomingMoviesDao) {
        mAppExecutors = appExecutors;
        mUpcomingMoviesDao = upcomingMoviesDao;
    }

    public static MoviesLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                    @NonNull UpcomingMoviesDao upcomingMoviesDao) {
        if (instance == null) {
            synchronized (MoviesLocalDataSource.class) {
                if (instance == null) {
                    instance = new MoviesLocalDataSource(appExecutors, upcomingMoviesDao);
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
                final List<Movie> movies = mUpcomingMoviesDao.getUpcomingMovies();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (movies.isEmpty()) {
                            // Table new or empty
                            callback.onDataNotAvailable();
                        } else {
                            // Return movies
                            callback.onUpcomingMoviesLoaded(movies);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void getMovieDetail(@NonNull final LoadMovieCallback callback, @NonNull final String movieId) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final Movie movies = mUpcomingMoviesDao.getUpcomingMovieById(movieId);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (movies == null) {
                            // Table new or empty
                            callback.onDataNotAvailable(movieId);
                        } else {
                            // Return movies
                            callback.onMovieLoaded(movies);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void saveMovie(@NonNull final Movie movie) {
        checkNotNull(movie);
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Insert movie
                mUpcomingMoviesDao.insertUpcomingMovie(movie);
            }
        });
    }

    @Override
    public void refreshUpcomingMovies() {
        // Do nothing
    }

    @Override
    public void deleteAllMovies() {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Delete all movies from database
                mUpcomingMoviesDao.deleteUpcomingMovies();
            }
        });
    }
}
