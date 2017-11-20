package com.code.arctouch.arctouchcodechallenge.data.source.remote;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.core.ResponseStatus;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.core.ResponseStatusException;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.tools.ApiUrl;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.tools.UpcomingMovieException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Abstract class for API use
 */
public abstract class AbstractApi {

    public static final String PARAM_PAGE = "page";
    public static final String PARAM_LANGUAGE = "language";
    public static final String PARAM_API_KEY = "api_key";

    protected static final ObjectMapper jsonMapper = new ObjectMapper();
    private static final Collection<Integer> SUCCESS_STATUS_CODES = Arrays.asList(
            1, // Success
            12, // The item/record was updated successfully.
            13 // The item/record was updated successfully.
    );

    protected final Api mApi;

    AbstractApi(Api mApi) {
        this.mApi = mApi;
    }

    public <T> T mapJsonResult(ApiUrl apiUrl, Class<T> someClass) {
        String webpage = mApi.requestWebPage(apiUrl);

        try {
            // check if was error responseStatus
            ResponseStatus responseStatus = jsonMapper.readValue(webpage, ResponseStatus.class);
            // if null, the json response was not a error responseStatus code, and but something else
            Integer statusCode = responseStatus.getStatusCode();
            if (statusCode != null && !SUCCESS_STATUS_CODES.contains(statusCode)) {
                throw new ResponseStatusException(responseStatus);
            }
            return jsonMapper.readValue(webpage, someClass);
        } catch (IOException ex) {
            throw new UpcomingMovieException("Mapping failed:\n" + webpage);
        }
    }
}
