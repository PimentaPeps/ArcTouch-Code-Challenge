
package com.code.arctouch.arctouchcodechallenge.upcomingmovies.domain.usecase;

import android.support.annotation.NonNull;

import com.code.arctouch.arctouchcodechallenge.UseCase;
import com.code.arctouch.arctouchcodechallenge.data.source.UpcomingMoviesDataSource;
import com.code.arctouch.arctouchcodechallenge.data.source.UpcomingMoviesRepository;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.UpcomingMovie;
import com.code.arctouch.arctouchcodechallenge.upcomingmovies.UpcomingMoviesFilterType;
import com.code.arctouch.arctouchcodechallenge.upcomingmovies.domain.filter.FilterFactory;
import com.code.arctouch.arctouchcodechallenge.upcomingmovies.domain.filter.UpcomingMovieFilter;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Fetches the list of upcomingMovies.
 */
public class GetUpcomingMovies extends UseCase<GetUpcomingMovies.RequestValues, GetUpcomingMovies.ResponseValue> {

    private final UpcomingMoviesRepository mUpcomingMoviesRepository;

    private final FilterFactory mFilterFactory;

    public GetUpcomingMovies(@NonNull UpcomingMoviesRepository upcomingMoviesRepository, @NonNull FilterFactory filterFactory) {
        mUpcomingMoviesRepository = checkNotNull(upcomingMoviesRepository, "upcomingMoviesRepository cannot be null!");
        mFilterFactory = checkNotNull(filterFactory, "filterFactory cannot be null!");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        if (values.isForceUpdate()) {
            mUpcomingMoviesRepository.refreshUpcomingMovies();
        }

        mUpcomingMoviesRepository.getUpcomingMovies(new UpcomingMoviesDataSource.LoadUpcomingMoviesCallback() {
            @Override
            public void onUpcomingMoviesLoaded(List<UpcomingMovie> upcomingMovies) {
                UpcomingMoviesFilterType currentFiltering = values.getCurrentFiltering();
                UpcomingMovieFilter upcomingMovieFilter = mFilterFactory.create(currentFiltering);

                List<UpcomingMovie> upcomingMoviesFiltered = upcomingMovieFilter.filter(upcomingMovies);
                ResponseValue responseValue = new ResponseValue(upcomingMoviesFiltered);
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

        public RequestValues(boolean forceUpdate, @NonNull UpcomingMoviesFilterType currentFiltering) {
            mForceUpdate = forceUpdate;
            mCurrentFiltering = checkNotNull(currentFiltering, "currentFiltering cannot be null!");
        }

        public boolean isForceUpdate() {
            return mForceUpdate;
        }

        public UpcomingMoviesFilterType getCurrentFiltering() {
            return mCurrentFiltering;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final List<UpcomingMovie> mUpcomingMovies;

        public ResponseValue(@NonNull List<UpcomingMovie> upcomingMovies) {
            mUpcomingMovies = checkNotNull(upcomingMovies, "upcomingMovies cannot be null!");
        }

        public List<UpcomingMovie> getUpcomingMovies() {
            return mUpcomingMovies;
        }
    }
}
