package com.code.arctouch.arctouchcodechallenge.moviedetail;

import com.code.arctouch.arctouchcodechallenge.BasePresenter;
import com.code.arctouch.arctouchcodechallenge.BaseView;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.Movie;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface MovieDetailContract {

    interface View extends BaseView<Presenter> {
        void showMovie(Movie movie);

        void showLoadingMovieError();

        void showNoMovie();
    }

    interface Presenter extends BasePresenter {
        void loadMovie(String movieId);
    }
}
