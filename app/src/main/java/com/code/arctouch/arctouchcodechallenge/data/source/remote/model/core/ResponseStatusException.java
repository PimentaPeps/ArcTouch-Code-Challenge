package com.code.arctouch.arctouchcodechallenge.data.source.remote.model.core;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.tools.UpcomingMovieException;

/**
 * Exception from ResponseStatus
 */
public class ResponseStatusException extends UpcomingMovieException {

    private final ResponseStatus mResponseStatus;

    public ResponseStatusException(ResponseStatus mResponseStatus) {
        super(mResponseStatus.getStatusCode() + " | " + mResponseStatus.getStatusMessage());
        this.mResponseStatus = mResponseStatus;
    }

    public ResponseStatus getResponseStatus() {
        return mResponseStatus;
    }

    @Override
    public String toString() {
        return mResponseStatus.toString();
    }
}
