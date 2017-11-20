package com.code.arctouch.arctouchcodechallenge.data.source.remote;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.tools.ApiUrl;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.tools.Requester;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.tools.UrlReader;

import org.apache.commons.lang3.StringUtils;

/**
 * Api main class, contains a singleton
 */
public class Api {

    private static Api instance;
    private String mApiKey = "1f54bd990f1cdfb230adb312546d765d";
    private UrlReader mUrlReader;

    private Api() {
        this.mUrlReader = new Requester();
    }

    public static Api getInstance() {
        if (instance == null) {
            instance = new Api();
        }
        return instance;
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
