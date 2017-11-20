package com.code.arctouch.arctouchcodechallenge.data.source.remote.model.core;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Abstract class to handle logging of unknown JsonMapping
 */
public abstract class AbstractJsonMapper implements Serializable {

    /**
     * Handle unknown properties and print a message
     *
     * @param key
     * @param value
     */
    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
        StringBuilder sb = new StringBuilder();
        sb.append("Unknown property: '").append(key);
        sb.append("' value: '").append(value).append("'");

        LoggerFactory.getLogger(this.getClass()).trace(sb.toString());
    }
}
