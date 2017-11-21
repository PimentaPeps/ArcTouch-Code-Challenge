package com.code.arctouch.arctouchcodechallenge.upcomingmovies.domain.filter;


import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.Movie;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Returns upcomingMovies from a list of {@link Movie}s.
 */
class ReleaseDateUpcomingMovieFilter implements UpcomingMovieFilter {
    @Override
    public List<Movie> filter(List<Movie> movies, String word) {
        Collections.sort(movies, new Comparator<Movie>() {
            public int compare(Movie obj1, Movie obj2) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = null, date2 = null;
                try {
                    date1 = sdf.parse(obj2.getReleaseDate());
                    date2 = sdf.parse(obj1.getReleaseDate());

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return date1.before(date2) == true ? -1 : 1;
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
