package com.code.arctouch.arctouchcodechallenge.data.source.remote;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.core.MovieResponse;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.tools.ApiUrl;

/**
 * Movies API
 */
public class Movies extends AbstractApi {

    // API Methods
    public static final String MOVIE = "movie";
    public static final String UPCOMING = "upcoming";

    public Movies(Api api) {
        super(api);
    }

    /**
     * Returns list of upcoming movies.
     */
    public MovieResponse getUpcomingMovies(String language, Integer page) {
        ApiUrl apiUrl = new ApiUrl(MOVIE, UPCOMING);
        apiUrl.addLanguage(language);
        apiUrl.addPage(page);
        return mapJsonResult(apiUrl, MovieResponse.class);
    }
}
