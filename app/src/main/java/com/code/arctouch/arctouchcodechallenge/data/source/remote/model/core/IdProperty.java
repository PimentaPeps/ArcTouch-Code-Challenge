package com.code.arctouch.arctouchcodechallenge.data.source.remote.model.core;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Base IdProperty Model class to extend
 */
public class IdProperty extends AbstractJsonMapper implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @JsonProperty("id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {


        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdProperty idProperty = (IdProperty) o;

        return id == idProperty.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
