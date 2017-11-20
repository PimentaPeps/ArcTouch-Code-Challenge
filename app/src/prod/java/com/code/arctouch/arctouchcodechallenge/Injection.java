package com.code.arctouch.arctouchcodechallenge;

import android.content.Context;
import android.support.annotation.NonNull;

import com.code.arctouch.arctouchcodechallenge.data.source.UpcomingMoviesDataSource;
import com.code.arctouch.arctouchcodechallenge.data.source.UpcomingMoviesRepository;
import com.code.arctouch.arctouchcodechallenge.data.source.local.RoomDatabase;
import com.code.arctouch.arctouchcodechallenge.data.source.local.UpcomingMoviesLocalDataSource;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.UpcomingMoviesRemoteDataSource;
import com.code.arctouch.arctouchcodechallenge.upcomingmovies.domain.filter.FilterFactory;
import com.code.arctouch.arctouchcodechallenge.upcomingmovies.domain.usecase.GetUpcomingMovies;
import com.code.arctouch.arctouchcodechallenge.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of production implementations for
 * {@link UpcomingMoviesDataSource} at compile time.
 */
public class Injection {

    public static UpcomingMoviesRepository provideUpcomingMoviesRepository(@NonNull Context context) {
        checkNotNull(context);
        RoomDatabase database = RoomDatabase.getInstance(context);
        AppExecutors executors = new AppExecutors();
        return UpcomingMoviesRepository.getInstance(UpcomingMoviesRemoteDataSource.getInstance(executors),
                UpcomingMoviesLocalDataSource.getInstance(executors,
                        database.upcomingMovieDao()));
    }

    public static GetUpcomingMovies provideGetUpcomingMovies(@NonNull Context context) {
        return new GetUpcomingMovies(provideUpcomingMoviesRepository(context), new FilterFactory());
    }

    public static UseCaseHandler provideUseCaseHandler() {
        return UseCaseHandler.getInstance();
    }
}
