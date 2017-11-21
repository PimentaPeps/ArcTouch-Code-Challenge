package com.code.arctouch.arctouchcodechallenge.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.Movie;

import java.util.List;

/**
 * Data Access Object for the upcomingMovie table.
 */
@Dao
public interface UpcomingMoviesDao {

    /**
     * Select all upcomingMovies from the upcomingMovies table.
     *
     * @return all upcomingMovies.
     */
    @Query("SELECT * FROM Movie")
    List<Movie> getUpcomingMovies();

    /**
     * Select a upcomingMovie by id.
     *
     * @param upcomingMovieId the upcomingMovie id.
     * @return the upcomingMovie with upcomingMovieId.
     */
    @Query("SELECT * FROM Movie WHERE id = :upcomingMovieId")
    Movie getUpcomingMovieById(String upcomingMovieId);

    /**
     * Insert a movie in the database. If the movie already exists, replace it.
     *
     * @param movie the movie to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUpcomingMovie(Movie movie);

    /**
     * Delete a upcomingMovie by id.
     *
     * @return the number of upcomingMovies deleted. This should always be 1.
     */
    @Query("DELETE FROM Movie WHERE id = :upcomingMovieId")
    int deleteUpcomingMovieById(String upcomingMovieId);

    /**
     * Delete all upcomingMovies.
     */
    @Query("DELETE FROM Movie")
    void deleteUpcomingMovies();
}
