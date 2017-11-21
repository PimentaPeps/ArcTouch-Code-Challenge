package com.code.arctouch.arctouchcodechallenge.upcomingmovies.domain.filter;


import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.Movie;

import java.util.List;

public interface UpcomingMovieFilter {
    List<Movie> filter(List<Movie> tasks, String word);
}
