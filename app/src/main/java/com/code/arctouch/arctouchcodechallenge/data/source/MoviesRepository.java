package com.code.arctouch.arctouchcodechallenge.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.Movie;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation of dataSource, uses a simple local cache
 */
public class MoviesRepository implements MoviesDataSource {

    private static MoviesRepository instance = null;
    private final MoviesDataSource mMoviesRemoteDataSource;
    private final MoviesDataSource mMoviesLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, Movie> mCachedUpcomingMovies;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private MoviesRepository(@NonNull MoviesDataSource moviesRemoteDataSource,
                             @NonNull MoviesDataSource moviesLocalDataSource) {
        mMoviesRemoteDataSource = checkNotNull(moviesRemoteDataSource);
        mMoviesLocalDataSource = checkNotNull(moviesLocalDataSource);
    }

    public static MoviesRepository getInstance(MoviesDataSource upcomingMoviesRemoteDataSource,
                                               MoviesDataSource upcomingMoviesLocalDataSource) {
        if (instance == null) {
            instance = new MoviesRepository(upcomingMoviesRemoteDataSource, upcomingMoviesLocalDataSource);
        }
        return instance;
    }

    @Override
    public void getUpcomingMovies(@NonNull final LoadUpcomingMoviesCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedUpcomingMovies != null && !mCacheIsDirty) {
            callback.onUpcomingMoviesLoaded(new ArrayList<>(mCachedUpcomingMovies.values()));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getUpcomingMoviesFromRemoteDataSource(callback);
        } else {
            getUpcomingMoviesFromLocalDataSource(callback);
        }
    }

    @Override
    public void getMovieDetail(@NonNull final LoadMovieCallback callback, String movieId) {
        if (mCachedUpcomingMovies.containsKey(mCachedUpcomingMovies)) {
            callback.onMovieLoaded(mCachedUpcomingMovies.get(mCachedUpcomingMovies));
            return;
        }
        getMovieFromRemoteDataSource(callback, movieId);
    }

    @Override
    public void saveMovie(@NonNull Movie movie) {
        checkNotNull(movie);
        mMoviesRemoteDataSource.saveMovie(movie);
        mMoviesLocalDataSource.saveMovie(movie);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedUpcomingMovies == null) {
            mCachedUpcomingMovies = new LinkedHashMap<>();
        }
        mCachedUpcomingMovies.put(String.valueOf(movie.getId()), movie);
    }

    @Override
    public void refreshUpcomingMovies() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllMovies() {
        mMoviesLocalDataSource.deleteAllMovies();

        if (mCachedUpcomingMovies == null) {
            mCachedUpcomingMovies = new LinkedHashMap<>();
        }
        mCachedUpcomingMovies.clear();
    }


    //UPCOMING MOVIES
    private void getUpcomingMoviesFromRemoteDataSource(@NonNull final LoadUpcomingMoviesCallback callback) {
        // Try remote first if available. If not, go local storage.
        mMoviesRemoteDataSource.getUpcomingMovies(new LoadUpcomingMoviesCallback() {

            @Override
            public void onUpcomingMoviesLoaded(List<Movie> movies) {
                refreshCache(movies);
                refreshLocalDataSource(movies);
                callback.onUpcomingMoviesLoaded(new ArrayList<>(mCachedUpcomingMovies.values()));
            }

            @Override
            public void onDataNotAvailable() {
                getUpcomingMoviesFromLocalDataSource(callback);
            }
        });
    }

    private void getUpcomingMoviesFromLocalDataSource(@NonNull final LoadUpcomingMoviesCallback callback) {
        // Query the local storage if available. If not, displays error.
        mMoviesLocalDataSource.getUpcomingMovies(new LoadUpcomingMoviesCallback() {

            @Override
            public void onUpcomingMoviesLoaded(List<Movie> movies) {
                refreshCache(movies);
                callback.onUpcomingMoviesLoaded(new ArrayList<>(mCachedUpcomingMovies.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    //MOVIE DETAILS
    private void getMovieFromRemoteDataSource(@NonNull final LoadMovieCallback callback, String movieId) {
        // Try remote first if available. If not, go local storage.
        checkNotNull(callback);
        mMoviesRemoteDataSource.getMovieDetail(new LoadMovieCallback() {

            @Override
            public void onMovieLoaded(Movie movie) {
                addToCache(movie);
                addToLocalDataSource(movie);
                callback.onMovieLoaded(movie);
            }

            @Override
            public void onDataNotAvailable(String movieId) {
                getMovieFromLocalDataSource(callback, movieId);
            }
        }, movieId);
    }

    private void getMovieFromLocalDataSource(@NonNull final LoadMovieCallback callback, String movieId) {
        // Query the local storage if available. If not, displays error.
        mMoviesLocalDataSource.getMovieDetail(new LoadMovieCallback() {

            @Override
            public void onMovieLoaded(Movie movie) {
                addToCache(movie);
                callback.onMovieLoaded(movie);
            }

            @Override
            public void onDataNotAvailable(String movieId) {
                callback.onDataNotAvailable(movieId);
            }
        }, movieId);
    }

    private void refreshCache(List<Movie> movies) {
        if (mCachedUpcomingMovies == null) {
            mCachedUpcomingMovies = new LinkedHashMap<>();
        }
        mCachedUpcomingMovies.clear();
        for (Movie movie : movies) {
            mCachedUpcomingMovies.put(String.valueOf(movie.getId()), movie);
        }
        mCacheIsDirty = false;
    }

    private void addToCache(Movie movie) {
        if (mCachedUpcomingMovies == null) {
            mCachedUpcomingMovies = new LinkedHashMap<>();
        }
        mCachedUpcomingMovies.put(String.valueOf(movie.getId()), movie);
    }

    private void refreshLocalDataSource(List<Movie> movies) {
        mMoviesLocalDataSource.deleteAllMovies();
        for (Movie movie : movies) {
            mMoviesLocalDataSource.saveMovie(movie);
        }
    }

    private void addToLocalDataSource(Movie movie) {
        mMoviesLocalDataSource.saveMovie(movie);
    }

    @Nullable
    private Movie getUpcomingMovieWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedUpcomingMovies == null || mCachedUpcomingMovies.isEmpty()) {
            return null;
        } else {
            return mCachedUpcomingMovies.get(id);
        }
    }
}
