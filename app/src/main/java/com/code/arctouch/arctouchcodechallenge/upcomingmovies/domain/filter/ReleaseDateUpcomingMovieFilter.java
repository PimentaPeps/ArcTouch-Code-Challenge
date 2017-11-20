package com.code.arctouch.arctouchcodechallenge.upcomingmovies.domain.filter;


import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.UpcomingMovie;

import java.util.ArrayList;
import java.util.List;

/**
 * Returns upcomingMovies from a list of {@link UpcomingMovie}s.
 */
class ReleaseDateUpcomingMovieFilter implements UpcomingMovieFilter {
    @Override
    public List<UpcomingMovie> filter(List<UpcomingMovie> upcomingMovies) {
        List<UpcomingMovie> filteredUpcomingMovies = new ArrayList<>(upcomingMovies);
        //TODO
        return filteredUpcomingMovies;
    }
}
