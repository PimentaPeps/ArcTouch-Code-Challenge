package com.code.arctouch.arctouchcodechallenge.upcomingmovies.domain.filter;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.Movie;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Returns upcomingMovies from a list of {@link Movie}s.
 */
class PopularityUpcomingMovieFilter implements UpcomingMovieFilter {
    @Override
    public List<Movie> filter(List<Movie> movies) {
        Collections.sort(movies, new Comparator<Movie>() {
            public int compare(Movie obj1, Movie obj2) {
                return obj2.getPopularity() < obj1.getPopularity() ? -1 : 1;
            }
        });
        return movies;
    }
}
