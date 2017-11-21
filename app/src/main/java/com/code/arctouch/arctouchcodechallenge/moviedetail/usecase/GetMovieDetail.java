package com.code.arctouch.arctouchcodechallenge.moviedetail.usecase;

import android.support.annotation.NonNull;

import com.code.arctouch.arctouchcodechallenge.UseCase;
import com.code.arctouch.arctouchcodechallenge.data.source.MoviesDataSource;
import com.code.arctouch.arctouchcodechallenge.data.source.MoviesRepository;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.Movie;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Fetches the upcomingMoviesDetail.
 */
public class GetMovieDetail extends UseCase<GetMovieDetail.RequestValues, GetMovieDetail.ResponseValue> {

    private final MoviesRepository mMoviesRepository;

    public GetMovieDetail(@NonNull MoviesRepository moviesRepository) {
        mMoviesRepository = checkNotNull(moviesRepository, "moviesRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        mMoviesRepository.getMovieDetail(new MoviesDataSource.LoadMovieCallback() {

            @Override
            public void onMovieLoaded(Movie movie) {
                ResponseValue responseValue = new ResponseValue(movie);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable(String movieId) {
                getUseCaseCallback().onError();
            }
        }, values.getMovieId());

    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final String mMovieId;

        public RequestValues(@NonNull String upMovieId) {
            mMovieId = checkNotNull(upMovieId, "UpcomingMovieDetail cannot be null!");
        }

        public String getMovieId() {
            return mMovieId;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {
        private final Movie mMovie;

        public ResponseValue(@NonNull Movie movie) {
            mMovie = checkNotNull(movie, "tasks cannot be null!");
        }

        public Movie getMovie() {
            return mMovie;
        }
    }
}
