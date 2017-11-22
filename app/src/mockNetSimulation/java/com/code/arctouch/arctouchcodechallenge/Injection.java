package com.code.arctouch.arctouchcodechallenge;

import android.content.Context;
import android.support.annotation.NonNull;

import com.code.arctouch.arctouchcodechallenge.data.MoviesRemoteDataSource;
import com.code.arctouch.arctouchcodechallenge.data.source.MoviesDataSource;
import com.code.arctouch.arctouchcodechallenge.data.source.MoviesRepository;
import com.code.arctouch.arctouchcodechallenge.data.source.local.MoviesLocalDataSource;
import com.code.arctouch.arctouchcodechallenge.data.source.local.RoomDatabase;
import com.code.arctouch.arctouchcodechallenge.upcomingmovies.domain.filter.FilterFactory;
import com.code.arctouch.arctouchcodechallenge.upcomingmovies.domain.usecase.GetUpcomingMovies;
import com.code.arctouch.arctouchcodechallenge.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of mock implementations for
 * {@link MoviesDataSource} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    public static MoviesRepository provideUpcomingMoviesRepository(@NonNull Context context) {
        checkNotNull(context);
        RoomDatabase database = RoomDatabase.getInstance(context);
        return MoviesRepository.getInstance(MoviesRemoteDataSource.getInstance(),
                MoviesLocalDataSource.getInstance(new AppExecutors(),
                        database.upcomingMovieDao()));
    }

    public static GetUpcomingMovies provideGetUpcomingMovies(@NonNull Context context) {
        return new GetUpcomingMovies(provideUpcomingMoviesRepository(context), new FilterFactory());
    }

    public static UseCaseHandler provideUseCaseHandler() {
        return UseCaseHandler.getInstance();
    }
}
