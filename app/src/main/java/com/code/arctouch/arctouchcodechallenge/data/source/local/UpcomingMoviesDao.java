package com.code.arctouch.arctouchcodechallenge.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.UpcomingMovie;

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
    @Query("SELECT * FROM UpcomingMovies")
    List<UpcomingMovie> getUpcomingMovies();

    /**
     * Select a upcomingMovie by id.
     *
     * @param upcomingMovieId the upcomingMovie id.
     * @return the upcomingMovie with upcomingMovieId.
     */
    @Query("SELECT * FROM UpcomingMovies WHERE id = :upcomingMovieId")
    UpcomingMovie getUpcomingMovieById(String upcomingMovieId);

    /**
     * Insert a upcomingMovie in the database. If the upcomingMovie already exists, replace it.
     *
     * @param upcomingMovie the upcomingMovie to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUpcomingMovie(UpcomingMovie upcomingMovie);

    /**
     * Delete a upcomingMovie by id.
     *
     * @return the number of upcomingMovies deleted. This should always be 1.
     */
    @Query("DELETE FROM UpcomingMovies WHERE id = :upcomingMovieId")
    int deleteUpcomingMovieById(String upcomingMovieId);

    /**
     * Delete all upcomingMovies.
     */
    @Query("DELETE FROM UpcomingMovies")
    void deleteUpcomingMovies();
}
