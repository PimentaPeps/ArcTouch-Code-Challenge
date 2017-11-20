package com.code.arctouch.arctouchcodechallenge.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.UpcomingMovie;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation of dataSource, uses a simple local cache
 */
public class UpcomingMoviesRepository implements UpcomingMoviesDataSource {

    private static UpcomingMoviesRepository instance = null;
    private final UpcomingMoviesDataSource mUpcomingMoviesRemoteDataSource;
    private final UpcomingMoviesDataSource mUpcomingMoviesLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, UpcomingMovie> mCachedUpcomingMovies;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private UpcomingMoviesRepository(@NonNull UpcomingMoviesDataSource upcomingMoviesRemoteDataSource,
                                     @NonNull UpcomingMoviesDataSource upcomingMoviesLocalDataSource) {
        mUpcomingMoviesRemoteDataSource = checkNotNull(upcomingMoviesRemoteDataSource);
        mUpcomingMoviesLocalDataSource = checkNotNull(upcomingMoviesLocalDataSource);
    }

    public static UpcomingMoviesRepository getInstance(UpcomingMoviesDataSource upcomingMoviesRemoteDataSource,
                                                       UpcomingMoviesDataSource upcomingMoviesLocalDataSource) {
        if (instance == null) {
            instance = new UpcomingMoviesRepository(upcomingMoviesRemoteDataSource, upcomingMoviesLocalDataSource);
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
    public void saveUpcomingMovie(@NonNull UpcomingMovie upcomingMovie) {
        checkNotNull(upcomingMovie);
        mUpcomingMoviesRemoteDataSource.saveUpcomingMovie(upcomingMovie);
        mUpcomingMoviesLocalDataSource.saveUpcomingMovie(upcomingMovie);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedUpcomingMovies == null) {
            mCachedUpcomingMovies = new LinkedHashMap<>();
        }
        mCachedUpcomingMovies.put(String.valueOf(upcomingMovie.getId()), upcomingMovie);
    }

    @Override
    public void refreshUpcomingMovies() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllUpcomingMovies() {
        mUpcomingMoviesLocalDataSource.deleteAllUpcomingMovies();

        if (mCachedUpcomingMovies == null) {
            mCachedUpcomingMovies = new LinkedHashMap<>();
        }
        mCachedUpcomingMovies.clear();
    }

    private void getUpcomingMoviesFromRemoteDataSource(@NonNull final LoadUpcomingMoviesCallback callback) {
        // Try remote first if available. If not, go local storage.
        mUpcomingMoviesRemoteDataSource.getUpcomingMovies(new LoadUpcomingMoviesCallback() {

            @Override
            public void onUpcomingMoviesLoaded(List<UpcomingMovie> upcomingMovies) {
                refreshCache(upcomingMovies);
                refreshLocalDataSource(upcomingMovies);
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
        mUpcomingMoviesLocalDataSource.getUpcomingMovies(new LoadUpcomingMoviesCallback() {

            @Override
            public void onUpcomingMoviesLoaded(List<UpcomingMovie> upcomingMovies) {
                refreshCache(upcomingMovies);
                callback.onUpcomingMoviesLoaded(new ArrayList<>(mCachedUpcomingMovies.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<UpcomingMovie> upcomingMovies) {
        if (mCachedUpcomingMovies == null) {
            mCachedUpcomingMovies = new LinkedHashMap<>();
        }
        mCachedUpcomingMovies.clear();
        for (UpcomingMovie upcomingMovie : upcomingMovies) {
            mCachedUpcomingMovies.put(String.valueOf(upcomingMovie.getId()), upcomingMovie);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<UpcomingMovie> upcomingMovies) {
        mUpcomingMoviesLocalDataSource.deleteAllUpcomingMovies();
        for (UpcomingMovie upcomingMovie : upcomingMovies) {
            mUpcomingMoviesLocalDataSource.saveUpcomingMovie(upcomingMovie);
        }
    }

    @Nullable
    private UpcomingMovie getUpcomingMovieWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedUpcomingMovies == null || mCachedUpcomingMovies.isEmpty()) {
            return null;
        } else {
            return mCachedUpcomingMovies.get(id);
        }
    }
}
