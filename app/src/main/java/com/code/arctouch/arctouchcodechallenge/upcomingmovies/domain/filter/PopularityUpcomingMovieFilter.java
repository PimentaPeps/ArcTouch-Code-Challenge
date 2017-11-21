package com.code.arctouch.arctouchcodechallenge.upcomingmovies.domain.filter;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Returns upcomingMovies from a list of {@link Movie}s.
 */
class PopularityUpcomingMovieFilter implements UpcomingMovieFilter {
    @Override
    public List<Movie> filter(List<Movie> movies) {
        List<Movie> filteredMovies = new ArrayList<>();
        //TODO
        return filteredMovies;
    }
}
