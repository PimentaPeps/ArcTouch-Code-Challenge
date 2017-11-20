package com.code.arctouch.arctouchcodechallenge.data.source.remote.model.genre;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.core.AbstractJsonMapper;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Images Model class from GenreResults to serialize from API
 */
public class GenreResponse extends AbstractJsonMapper {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
