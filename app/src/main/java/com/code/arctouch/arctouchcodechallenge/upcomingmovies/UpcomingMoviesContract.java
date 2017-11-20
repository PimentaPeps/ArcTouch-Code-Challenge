package com.code.arctouch.arctouchcodechallenge.upcomingmovies;

import android.support.annotation.NonNull;

import com.code.arctouch.arctouchcodechallenge.BasePresenter;
import com.code.arctouch.arctouchcodechallenge.BaseView;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.UpcomingMovie;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface UpcomingMoviesContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showUpcomingMovies(List<UpcomingMovie> upcomingMovies);

        void showUpcomingMovieDetailsUi(String upcomingMoviesId);

        void showLoadingUpcomingMoviesError();

        void showNoUpcomingMovies();

        void showPopularityFilterLabel();

        void showTitleAscFilterLabel();

        void showTitleDescFilterLabel();

        void showReleaseDateFilterLabel();

        void showFilteringPopUpMenu();
    }

    interface Presenter extends BasePresenter {

        void loadUpcomingMovies(boolean forceUpdate);

        void openUpcomingMovieDetails(@NonNull UpcomingMovie requestedUpcomingMovie);

        UpcomingMoviesFilterType getFiltering();

        void setFiltering(UpcomingMoviesFilterType requestType);
    }
}
