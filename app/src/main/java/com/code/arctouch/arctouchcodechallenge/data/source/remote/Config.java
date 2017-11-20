package com.code.arctouch.arctouchcodechallenge.data.source.remote;

import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.config.ConfigResults;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.tools.ApiUrl;

/**
 * Config API
 */
class Config extends AbstractApi {

    public static final String CONFIGURATION = "configuration";

    Config(Api api) {
        super(api);
    }

    /**
     * Returns list of configurations.
     */
    public ConfigResults getConfig() {
        return mapJsonResult(new ApiUrl(CONFIGURATION), ConfigResults.class);
    }

}
