package com.code.arctouch.arctouchcodechallenge.upcomingmovies.domain.filter;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.Movie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Returns upcomingMovies from a list of {@link Movie}s.
 */
class TitleDescUpcomingMovieFilter implements UpcomingMovieFilter {
    @Override
    public List<Movie> filter(List<Movie> movies, String word) {
        Collections.sort(movies, new Comparator<Movie>() {
            public int compare(Movie obj1, Movie obj2) {
                return obj2.getTitle().compareToIgnoreCase(obj1.getTitle());
            }
        });

        List<Movie> filteredMovies = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.getTitle().toLowerCase().contains(word.toLowerCase()))
                filteredMovies.add(movie);
        }
        return filteredMovies;
    }
}
