package com.code.arctouch.arctouchcodechallenge.data.source.local;

import android.arch.persistence.room.TypeConverter;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.UpcomingMovieDetailGenre;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by gusta on 20/11/2017.
 */

public class ConvertersListGenre {
    @TypeConverter
    public static ArrayList<UpcomingMovieDetailGenre> fromString(String value) {
        Type listType = new TypeToken<ArrayList<UpcomingMovieDetailGenre>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<UpcomingMovieDetailGenre> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}