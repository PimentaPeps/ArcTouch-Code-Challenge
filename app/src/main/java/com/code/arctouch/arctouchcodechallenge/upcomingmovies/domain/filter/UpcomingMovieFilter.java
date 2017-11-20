package com.code.arctouch.arctouchcodechallenge.upcomingmovies.domain.filter;


import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.UpcomingMovie;

import java.util.List;

public interface UpcomingMovieFilter {
    List<UpcomingMovie> filter(List<UpcomingMovie> tasks);
}
