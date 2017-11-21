package com.code.arctouch.arctouchcodechallenge.upcomingmovies;

import android.support.annotation.NonNull;

import com.code.arctouch.arctouchcodechallenge.UseCase;
import com.code.arctouch.arctouchcodechallenge.UseCaseHandler;
import com.code.arctouch.arctouchcodechallenge.data.source.MoviesDataSource;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.Movie;
import com.code.arctouch.arctouchcodechallenge.upcomingmovies.domain.usecase.GetUpcomingMovies;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link UpcomingMoviesFragment}), retrieves the data and updates the
 * UI as required.
 */
public class UpcomingMoviesPresenter implements UpcomingMoviesContract.Presenter {


    private final UpcomingMoviesContract.View mUpcomingMoviesView;
    private final GetUpcomingMovies mGetUpcomingMovies;
    private final UseCaseHandler mUseCaseHandler;
    private UpcomingMoviesFilterType mCurrentFiltering = UpcomingMoviesFilterType.RELEASE_DATE_UPCOMINGMOVIES;
    private boolean mFirstLoad = true;

    public UpcomingMoviesPresenter(@NonNull UseCaseHandler useCaseHandler,
                                   @NonNull UpcomingMoviesContract.View upcomingMoviesView, @NonNull GetUpcomingMovies getUpcomingMovies) {
        mUseCaseHandler = checkNotNull(useCaseHandler, "usecaseHandler cannot be null");
        mUpcomingMoviesView = checkNotNull(upcomingMoviesView, "upcomingMoviesView cannot be null!");
        mGetUpcomingMovies = checkNotNull(getUpcomingMovies, "getUpcomingMovie cannot be null!");
        mUpcomingMoviesView.setPresenter(this);
    }

    @Override
    public void start() {
        loadUpcomingMovies(false);
    }


    @Override
    public void loadUpcomingMovies(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadUpcomingMovies(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link MoviesDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadUpcomingMovies(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mUpcomingMoviesView.setLoadingIndicator(true);
        }

        GetUpcomingMovies.RequestValues requestValue = new GetUpcomingMovies.RequestValues(forceUpdate,
                mCurrentFiltering);

        mUseCaseHandler.execute(mGetUpcomingMovies, requestValue,
                new UseCase.UseCaseCallback<GetUpcomingMovies.ResponseValue>() {
                    @Override
                    public void onSuccess(GetUpcomingMovies.ResponseValue response) {
                        List<Movie> movies = response.getUpcomingMovies();
                        if (showLoadingUI) {
                            mUpcomingMoviesView.setLoadingIndicator(false);
                        }

                        processUpcomingMovies(movies);
                    }

                    @Override
                    public void onError() {
                        mUpcomingMoviesView.showLoadingUpcomingMoviesError();
                    }
                });
    }

    private void processUpcomingMovies(List<Movie> movies) {
        if (movies.isEmpty()) {
            // Show a message indicating there are no movies for that filter type.
            processEmptyUpcomingMovies();
        } else {
            // Show the list of movies
            mUpcomingMoviesView.showUpcomingMovies(movies);
            // Set the filter label's text.
            showFilterLabel();
        }
    }

    private void showFilterLabel() {
        switch (mCurrentFiltering) {
            case POPULARITY_UPCOMINGMOVIES:
                mUpcomingMoviesView.showPopularityFilterLabel();
                break;
            case TITLE_ASC_UPCOMINGMOVIES:
                mUpcomingMoviesView.showTitleAscFilterLabel();
                break;
            case TITLE_DESC_UPCOMINGMOVIES:
                mUpcomingMoviesView.showTitleDescFilterLabel();
                break;
            default:
                mUpcomingMoviesView.showReleaseDateFilterLabel();
                break;
        }
    }

    private void processEmptyUpcomingMovies() {
        mUpcomingMoviesView.showNoUpcomingMovies();
    }

    @Override
    public void openUpcomingMovieDetails(@NonNull Movie requestedMovie) {
        checkNotNull(requestedMovie, "requestedMovie cannot be null!");
        mUpcomingMoviesView.showUpcomingMovieDetailsUi(String.valueOf(requestedMovie.getId()));
    }

    @Override
    public UpcomingMoviesFilterType getFiltering() {
        return mCurrentFiltering;
    }

    /**
     * Sets the current upcomingMovie filtering type.
     *
     * @param requestType Can be {@link UpcomingMoviesFilterType#RELEASE_DATE_UPCOMINGMOVIES},
     *                    {@link UpcomingMoviesFilterType#TITLE_ASC_UPCOMINGMOVIES}, or
     *                    {@link UpcomingMoviesFilterType#POPULARITY_UPCOMINGMOVIES}
     */
    @Override
    public void setFiltering(UpcomingMoviesFilterType requestType) {
        mCurrentFiltering = requestType;
    }

}
