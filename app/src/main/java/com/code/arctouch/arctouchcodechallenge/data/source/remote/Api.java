package com.code.arctouch.arctouchcodechallenge.data.source.remote;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.config.ImagesConfiguration;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.tools.ApiUrl;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.tools.Requester;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.tools.UpcomingMovieException;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.tools.UrlReader;

import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Api main class, contains a singleton
 */
public class Api {

    private static Api instance;
    private static ImagesConfiguration imagesConfiguration;
    private String mApiKey = "1f54bd990f1cdfb230adb312546d765d";
    private UrlReader mUrlReader;

    private Api() {
        this.mUrlReader = new Requester();
        try {
            imagesConfiguration = new Config(this).getConfig().getImagesConfiguration();
        } catch (UpcomingMovieException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new UpcomingMovieException("Failed to read configuration", ex);
        }
    }

    public static Api getInstance() {
        if (instance == null) {
            instance = new Api();
        }
        return instance;
    }

    public static URL createImageUrl(String imagePath, String requiredSize) {
        if (StringUtils.isBlank(imagePath)) {
            return null;
        }

        if (!imagesConfiguration.isValidSize(requiredSize)) {
            throw new UpcomingMovieException("Invalid size: " + requiredSize);
        }

        StringBuilder sb = new StringBuilder(imagesConfiguration.getBaseUrl());
        sb.append(requiredSize);
        sb.append(imagePath);
        try {
            return (new URL(sb.toString()));
        } catch (MalformedURLException ex) {
            throw new UpcomingMovieException(sb.toString(), ex);
        }
    }

    public String requestWebPage(ApiUrl apiUrl) {
        assert StringUtils.isNotBlank(mApiKey);
        apiUrl.addParam(AbstractApi.PARAM_API_KEY, getApiKey());
        return mUrlReader.request(apiUrl.buildUrl());
    }

    private String getApiKey() {
        return mApiKey;
    }

    public Movies getMovies() {
        return new Movies(this);
    }

}
