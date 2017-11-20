package com.code.arctouch.arctouchcodechallenge.data.source.remote.model.genre;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.core.AbstractJsonMapper;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Configuration Model class to serialize from API
 */
public class GenreResults extends AbstractJsonMapper implements Serializable {

    @JsonProperty("genres")
    private List<GenreResponse> genres;

    public List<GenreResponse> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreResponse> genres) {
        this.genres = genres;
    }
}
