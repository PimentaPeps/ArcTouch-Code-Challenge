package com.code.arctouch.arctouchcodechallenge.data.source.remote.tools;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.AbstractApi;

import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * API URL Builder
 */
public class ApiUrl {

    //Base URL
    private static final String API_BASE_URL = "https://api.themoviedb.org/3/";

    private final String mBaseUrl;

    private final Map<String, String> mParams = new HashMap<>();


    public ApiUrl(Object... urlElements) {
        StringBuilder baseUrlBuilder = new StringBuilder(API_BASE_URL);

        for (int i = 0; i < urlElements.length; i++) {
            baseUrlBuilder.append(urlElements[i]);

            if (i < urlElements.length - 1) {
                baseUrlBuilder.append("/");
            }
        }
        mBaseUrl = baseUrlBuilder.toString();
    }


    public URL buildUrl() {
        StringBuilder urlBuilder = new StringBuilder(mBaseUrl);

        try {

            if (mParams.size() > 0) {
                List<String> keys = new ArrayList<String>(mParams.keySet());
                for (int i = 0; i < keys.size(); i++) {
                    urlBuilder.append(i == 0 ? "?" : "&");
                    String paramName = keys.get(i);

                    urlBuilder.append(paramName).append("=");
                    urlBuilder.append(URLEncoder.encode(mParams.get(paramName), "UTF-8"));
                }
            }

            return new URL(urlBuilder.toString());

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void addParam(String name, Object value) {
        addParam(name, value.toString());
    }


    public void addParam(String name, String value) {
        if (mParams.containsKey(name)) {
            throw new RuntimeException("paramater '" + name + "' already defined");
        }

        name = StringUtils.trimToEmpty(name);
        if (name.isEmpty()) {
            throw new RuntimeException("parameter name can not be empty");
        }

        value = StringUtils.trimToEmpty(value);
        if (value.isEmpty()) {
            throw new RuntimeException("value of parameter '" + name + "' can not be empty");
        }

        mParams.put(name, value);
    }

    public void addPage(Integer page) {
        if (page != null && page > 0) {
            addParam(AbstractApi.PARAM_PAGE, page);
        }
    }


    public void addLanguage(String language) {
        if (isNotBlank(language)) {
            addParam(AbstractApi.PARAM_LANGUAGE, language);
        }
    }
}
