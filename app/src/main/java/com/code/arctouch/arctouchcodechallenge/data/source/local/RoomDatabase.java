package com.code.arctouch.arctouchcodechallenge.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.UpcomingMovie;

/**
 * Database that contains upcomingMovies table
 */
@Database(entities = {UpcomingMovie.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class RoomDatabase extends android.arch.persistence.room.RoomDatabase {

    //static object to ensure only one thread at time to getInstance
    private static final Object sLock = new Object();
    private static RoomDatabase instance;

    public static RoomDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (instance == null) {
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        RoomDatabase.class, "Database.db")
                        .build();
            }
            return instance;
        }
    }

    public abstract UpcomingMoviesDao upcomingMovieDao();

}
