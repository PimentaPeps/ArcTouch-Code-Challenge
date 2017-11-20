package com.code.arctouch.arctouchcodechallenge.data.source.remote.model.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

/**
 * Response Model class to serialize from API in case of a code different from 200
 */
public class ResponseStatus extends AbstractJsonMapper {

    @JsonProperty("status_code")
    private Integer statusCode;

    @JsonProperty("status_message")
    private String statusMessage;

    //Constructor needed for jackson
    public ResponseStatus() {
    }

    public ResponseStatus(Integer statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("code", getStatusCode()).add("message", getStatusMessage()).toString();
    }
}
