package com.code.arctouch.arctouchcodechallenge.data.source.remote.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.Nullable;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.core.IdProperty;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Movie Model entity for database and API
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@Entity(tableName = "Movie")
public class Movie extends IdProperty {

    @Nullable
    @ColumnInfo(name = "title")
    @JsonProperty("title")
    private String title;

    @Nullable
    @ColumnInfo(name = "original_title")
    @JsonProperty("original_title")
    private String originalTitle;

    @Nullable
    @ColumnInfo(name = "popularity")
    @JsonProperty("popularity")
    private float popularity;

    @Nullable
    @ColumnInfo(name = "backdrop_path")
    @JsonProperty("backdrop_path")
    private String backdropPath;

    @Nullable
    @ColumnInfo(name = "poster_path")
    @JsonProperty("poster_path")
    private String posterPath;

    @Nullable
    @ColumnInfo(name = "release_date")
    @JsonProperty("release_date")
    private String releaseDate;

    @Nullable
    @ColumnInfo(name = "adult")
    @JsonProperty("adult")
    private boolean adult;

    @Nullable
    @ColumnInfo(name = "overview")
    @JsonProperty("overview")
    private String overview;

    @Nullable
    @ColumnInfo(name = "original_language")
    @JsonProperty("original_language")
    private String originalLanguage;

    @Nullable
    @ColumnInfo(name = "rating")
    @JsonProperty("rating")
    private float userRating;

    @Nullable
    @ColumnInfo(name = "vote_average")
    @JsonProperty("vote_average")
    private float voteAverage;

    @Nullable
    @ColumnInfo(name = "vote_count")
    @JsonProperty("vote_count")
    private int voteCount;

    @Nullable
    @ColumnInfo(name = "genre_ids")
    @JsonProperty("genre_ids")
    private ArrayList<String> genre_ids;

    @Nullable
    @ColumnInfo(name = "genres")
    @JsonProperty("genres")
    private ArrayList<UpcomingMovieDetailGenre> genres;

    @Nullable
    @ColumnInfo(name = "budget")
    @JsonProperty("budget")
    private int budget;


    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(@Nullable String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(@Nullable String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(@Nullable float popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(@Nullable String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(@Nullable String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(@Nullable boolean adult) {
        this.adult = adult;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(@Nullable String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(@Nullable String overview) {
        this.overview = overview;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(@Nullable float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(@Nullable int voteCount) {
        this.voteCount = voteCount;
    }

    public float getUserRating() {
        return userRating;
    }

    public void setUserRating(@Nullable float userRating) {
        this.userRating = userRating;
    }

    @Override
    public String toString() {
        return getTitle() + " - " + getReleaseDate();
    }

    @Nullable
    public ArrayList<String> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(@Nullable ArrayList<String> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getGenresString() {
        StringBuilder sb = new StringBuilder();
        if (genre_ids != null) {
            for (Iterator iter = genre_ids.iterator(); iter.hasNext(); ) {
                sb.append(iter.next());
                if (iter.hasNext())
                    sb.append(" | ");
            }
        } else if (genres != null) {
            for (Iterator iter = genres.iterator(); iter.hasNext(); ) {
                sb.append(((UpcomingMovieDetailGenre) iter.next()).getName());
                if (iter.hasNext())
                    sb.append(" | ");
            }
        }
        return sb.toString();
    }

    @Nullable
    public int getBudget() {
        return budget;
    }

    public void setBudget(@Nullable int budget) {
        this.budget = budget;
    }

    @Nullable
    public ArrayList<UpcomingMovieDetailGenre> getGenres() {
        return genres;
    }

    public void setGenres(@Nullable ArrayList<UpcomingMovieDetailGenre> genres) {
        this.genres = genres;
    }
}
