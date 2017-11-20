package com.code.arctouch.arctouchcodechallenge.upcomingmovies.domain.filter;

import com.code.arctouch.arctouchcodechallenge.upcomingmovies.UpcomingMoviesFilterType;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory of {@link UpcomingMovieFilter}s.
 */
public class FilterFactory {
    private static final Map<UpcomingMoviesFilterType, UpcomingMovieFilter> mFilters = new HashMap<>();

    public FilterFactory() {
        mFilters.put(UpcomingMoviesFilterType.RELEASE_DATE_UPCOMINGMOVIES, new ReleaseDateUpcomingMovieFilter());
        mFilters.put(UpcomingMoviesFilterType.POPULARITY_UPCOMINGMOVIES, new PopularityUpcomingMovieFilter());
        mFilters.put(UpcomingMoviesFilterType.TITLE_ASC_UPCOMINGMOVIES, new TitleAscUpcomingMovieFilter());
        mFilters.put(UpcomingMoviesFilterType.TITLE_DESC_UPCOMINGMOVIES, new TitleDescUpcomingMovieFilter());
    }

    public UpcomingMovieFilter create(UpcomingMoviesFilterType filterType) {
        return mFilters.get(filterType);
    }
}
