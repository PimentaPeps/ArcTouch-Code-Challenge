package com.code.arctouch.arctouchcodechallenge.upcomingmovies;

import android.support.annotation.NonNull;

import com.code.arctouch.arctouchcodechallenge.BasePresenter;
import com.code.arctouch.arctouchcodechallenge.BaseView;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.Movie;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface UpcomingMoviesContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showUpcomingMovies(List<Movie> movies);

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

        void openUpcomingMovieDetails(@NonNull Movie requestedMovie);

        UpcomingMoviesFilterType getFiltering();

        void setFiltering(UpcomingMoviesFilterType requestType, String word);
    }
}
