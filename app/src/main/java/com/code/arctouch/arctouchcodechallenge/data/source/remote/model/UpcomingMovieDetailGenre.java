package com.code.arctouch.arctouchcodechallenge.data.source.remote.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.Nullable;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.core.IdProperty;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Movie Model entity for database and API
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@Entity(tableName = "upcomingMovies")
public class UpcomingMovieDetailGenre extends IdProperty {

    @Nullable
    @ColumnInfo(name = "name")
    @JsonProperty("name")
    private String name;

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }
}
