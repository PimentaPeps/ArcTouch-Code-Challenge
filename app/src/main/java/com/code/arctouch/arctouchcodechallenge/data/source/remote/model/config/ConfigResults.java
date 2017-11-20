package com.code.arctouch.arctouchcodechallenge.data.source.remote.model.config;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.core.AbstractJsonMapper;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Configuration Model class to serialize from API
 */
public class ConfigResults extends AbstractJsonMapper implements Serializable {

    @JsonProperty("images")
    private ImagesConfiguration imagesConfiguration;
    @JsonProperty("change_keys")
    private List<String> changeKeys;


    public ImagesConfiguration getImagesConfiguration() {
        return imagesConfiguration;
    }

    public void setImagesConfiguration(ImagesConfiguration imagesConfiguration) {
        this.imagesConfiguration = imagesConfiguration;
    }

    public List<String> getChangeKeys() {
        return changeKeys;
    }

    public void setChangeKeys(List<String> changeKeys) {
        this.changeKeys = changeKeys;
    }
}
