package com.code.arctouch.arctouchcodechallenge.data.source.remote;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.genre.GenreResults;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.tools.ApiUrl;

/**
 * Config API
 */
class Genre extends AbstractApi {

    public static final String GENRE = "genre/movie/list";

    Genre(Api api) {
        super(api);
    }

    /**
     * Returns list of configurations.
     */
    public GenreResults getGenre() {
        return mapJsonResult(new ApiUrl(GENRE), GenreResults.class);
    }

}
