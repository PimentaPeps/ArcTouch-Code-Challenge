package com.code.arctouch.arctouchcodechallenge.moviedetail;

import android.support.annotation.NonNull;

import com.code.arctouch.arctouchcodechallenge.UseCase;
import com.code.arctouch.arctouchcodechallenge.UseCaseHandler;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.Movie;
import com.code.arctouch.arctouchcodechallenge.moviedetail.usecase.GetMovieDetail;
import com.code.arctouch.arctouchcodechallenge.upcomingmovies.UpcomingMoviesFragment;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link UpcomingMoviesFragment}), retrieves the data and updates the
 * UI as required.
 */
public class MovieDetailPresenter implements MovieDetailContract.Presenter {


    private final MovieDetailContract.View mUpcomingMoviesDetailView;
    private final GetMovieDetail getMovieDetail;
    private final UseCaseHandler mUseCaseHandler;
    private final String mMovieId;

    public MovieDetailPresenter(@NonNull UseCaseHandler useCaseHandler,
                                @NonNull MovieDetailContract.View moviesDetailContractView,
                                @NonNull GetMovieDetail getUpcomingMovies, String movieId) {
        mUseCaseHandler = checkNotNull(useCaseHandler, "useCaseHandler cannot be null");
        mUpcomingMoviesDetailView = checkNotNull(moviesDetailContractView, "moviesDetailContractView cannot be null!");
        getMovieDetail = checkNotNull(getUpcomingMovies, "getUpcomingMovie cannot be null!");
        mUpcomingMoviesDetailView.setPresenter(this);
        mMovieId = movieId;
    }

    @Override
    public void start() {
        loadMovie(mMovieId);
    }


    @Override
    public void loadMovie(String movieId) {
        GetMovieDetail.RequestValues requestValue = new GetMovieDetail.RequestValues(movieId);

        mUseCaseHandler.execute(getMovieDetail, requestValue,
                new UseCase.UseCaseCallback<GetMovieDetail.ResponseValue>() {
                    @Override
                    public void onSuccess(GetMovieDetail.ResponseValue response) {
                        Movie movie = response.getMovie();
                        processUpcomingMovies(movie);
                    }

                    @Override
                    public void onError() {
                        mUpcomingMoviesDetailView.showLoadingMovieError();
                    }
                });
    }

    private void processUpcomingMovies(Movie movie) {
        if (movie == null) {
            // Show a message indicating there are no upcomingMovies for that filter type.
            processEmptyUpcomingMovies();
        } else {
            // Show the list of upcomingMovies
            mUpcomingMoviesDetailView.showMovie(movie);
        }
    }

    private void processEmptyUpcomingMovies() {
        mUpcomingMoviesDetailView.showNoMovie();
    }

}
