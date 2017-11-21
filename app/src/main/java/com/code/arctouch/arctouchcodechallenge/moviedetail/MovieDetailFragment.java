package com.code.arctouch.arctouchcodechallenge.moviedetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.code.arctouch.arctouchcodechallenge.R;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.Api;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.Movie;
import com.squareup.picasso.Picasso;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display a grid of {@link Movie}s. User can choose to view all, active or completed upcomingMovies.
 */
public class MovieDetailFragment extends DialogFragment implements MovieDetailContract.View {

    private MovieDetailContract.Presenter mPresenter;
    private ImageView imageCover;
    private TextView movieTitle;
    private TextView movieYear;
    private TextView movieOverview;
    private TextView movieGenre;
    private ImageButton closeButton;

    public MovieDetailFragment() {
        // Requires empty public constructor
    }

    public static MovieDetailFragment newInstance() {
        return new MovieDetailFragment();
    }

    @Override
    public void onStart() {

        super.onStart();
        getDialog().getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull MovieDetailContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.upcoming_movies_detail_frag, container, false);
        imageCover = root.findViewById(R.id.upcoming_movie_detail_cover);
        movieTitle = root.findViewById(R.id.upcoming_movie_detail_title);
        movieYear = root.findViewById(R.id.upcoming_movie_detail_year);
        movieOverview = root.findViewById(R.id.upcoming_movie_detail_overview);
        movieGenre = root.findViewById(R.id.upcoming_movie_detail_genre);
        closeButton = root.findViewById(R.id.upcoming_movie_detail_close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return root;
    }


    @Override
    public void showMovie(Movie movie) {
        Picasso.with(getContext())
                .load(Api.createImageUrl(movie.getPosterPath(), "w500").toString())
                .into(imageCover);
        movieTitle.setText(movie.getTitle());
        movieYear.setText(movie.getReleaseDate());
        movieOverview.setText(movie.getOverview());
        movieGenre.setText(movie.getGenresString());
        closeButton.bringToFront();
    }

    @Override
    public void showNoMovie() {
        showMessage(getString(R.string.loading_upcoming_movies_error));
    }


    @Override
    public void showLoadingMovieError() {
        showMessage(getString(R.string.loading_upcoming_movies_error));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

}
