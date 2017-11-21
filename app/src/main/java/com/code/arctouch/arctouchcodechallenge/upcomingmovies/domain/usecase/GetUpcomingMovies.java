package com.code.arctouch.arctouchcodechallenge.upcomingmovies.domain.usecase;

import android.support.annotation.NonNull;

import com.code.arctouch.arctouchcodechallenge.UseCase;
import com.code.arctouch.arctouchcodechallenge.data.source.MoviesDataSource;
import com.code.arctouch.arctouchcodechallenge.data.source.MoviesRepository;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.Movie;
import com.code.arctouch.arctouchcodechallenge.upcomingmovies.UpcomingMoviesFilterType;
import com.code.arctouch.arctouchcodechallenge.upcomingmovies.domain.filter.FilterFactory;
import com.code.arctouch.arctouchcodechallenge.upcomingmovies.domain.filter.UpcomingMovieFilter;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Fetches the list of upcomingMovies.
 */
public class GetUpcomingMovies extends UseCase<GetUpcomingMovies.RequestValues, GetUpcomingMovies.ResponseValue> {

    private final MoviesRepository mUpcomingMoviesRepository;

    private final FilterFactory mFilterFactory;

    public GetUpcomingMovies(@NonNull MoviesRepository upcomingMoviesRepository, @NonNull FilterFactory filterFactory) {
        mUpcomingMoviesRepository = checkNotNull(upcomingMoviesRepository, "upcomingMoviesRepository cannot be null!");
        mFilterFactory = checkNotNull(filterFactory, "filterFactory cannot be null!");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        if (values.isForceUpdate()) {
            mUpcomingMoviesRepository.refreshUpcomingMovies();
        }

        mUpcomingMoviesRepository.getUpcomingMovies(new MoviesDataSource.LoadUpcomingMoviesCallback() {
            @Override
            public void onUpcomingMoviesLoaded(List<Movie> movies) {
                UpcomingMoviesFilterType currentFiltering = values.getCurrentFiltering();
                UpcomingMovieFilter upcomingMovieFilter = mFilterFactory.create(currentFiltering);

                List<Movie> moviesFiltered = upcomingMovieFilter.filter(movies, values.getFilteringWord());
                ResponseValue responseValue = new ResponseValue(moviesFiltered);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });

    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final UpcomingMoviesFilterType mCurrentFiltering;
        private final boolean mForceUpdate;
        private final String mFilteringWord;

        public RequestValues(boolean forceUpdate, @NonNull UpcomingMoviesFilterType currentFiltering, String filteringWord) {
            mForceUpdate = forceUpdate;
            mCurrentFiltering = checkNotNull(currentFiltering, "currentFiltering cannot be null!");
            mFilteringWord = filteringWord;
        }

        public boolean isForceUpdate() {
            return mForceUpdate;
        }

        public UpcomingMoviesFilterType getCurrentFiltering() {
            return mCurrentFiltering;
        }

        public String getFilteringWord() {
            return mFilteringWord;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final List<Movie> mMovies;

        public ResponseValue(@NonNull List<Movie> movies) {
            mMovies = checkNotNull(movies, "movies cannot be null!");
        }

        public List<Movie> getUpcomingMovies() {
            return mMovies;
        }
    }
}
