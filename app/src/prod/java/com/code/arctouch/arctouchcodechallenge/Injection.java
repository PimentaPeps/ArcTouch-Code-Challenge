package com.code.arctouch.arctouchcodechallenge;

import android.content.Context;
import android.support.annotation.NonNull;

import com.code.arctouch.arctouchcodechallenge.data.source.MoviesDataSource;
import com.code.arctouch.arctouchcodechallenge.data.source.MoviesRepository;
import com.code.arctouch.arctouchcodechallenge.data.source.local.MoviesLocalDataSource;
import com.code.arctouch.arctouchcodechallenge.data.source.local.RoomDatabase;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.MoviesRemoteDataSource;
import com.code.arctouch.arctouchcodechallenge.moviedetail.usecase.GetMovieDetail;
import com.code.arctouch.arctouchcodechallenge.upcomingmovies.domain.filter.FilterFactory;
import com.code.arctouch.arctouchcodechallenge.upcomingmovies.domain.usecase.GetUpcomingMovies;
import com.code.arctouch.arctouchcodechallenge.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of production implementations for
 * {@link MoviesDataSource} at compile time.
 */
public class Injection {

    public static MoviesRepository provideMoviesRepository(@NonNull Context context) {
        checkNotNull(context);
        RoomDatabase database = RoomDatabase.getInstance(context);
        AppExecutors executors = new AppExecutors();
        return MoviesRepository.getInstance(MoviesRemoteDataSource.getInstance(executors),
                MoviesLocalDataSource.getInstance(executors,
                        database.upcomingMovieDao()));
    }

    public static GetUpcomingMovies provideGetUpcomingMovies(@NonNull Context context) {
        return new GetUpcomingMovies(provideMoviesRepository(context), new FilterFactory());
    }

    public static GetMovieDetail provideGetMoviesDetail(@NonNull Context context) {
        return new GetMovieDetail(provideMoviesRepository(context));
    }

    public static UseCaseHandler provideUseCaseHandler() {
        return UseCaseHandler.getInstance();
    }
}
