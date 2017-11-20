package com.code.arctouch.arctouchcodechallenge.data.source.remote.model.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response Model class to serialize from API in case of a code 200
 */
public class ResponsePage<T> extends AbstractJsonMapper {

    @JsonProperty("page")
    private int page;

    @JsonProperty("results")
    private List<T> results;

    @JsonProperty("total_pages")
    private int totalPages;

    @JsonProperty("total_results")
    private int totalResults;

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

}
