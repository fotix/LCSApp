package com.trappz.app.api.models.Youtube;

/**
 * Created by Filipe Oliveira on 21-07-2014.
 */
public class PageInfo {

    private Integer totalResults;

    private Integer resultsPerPage;

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(Integer resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }
}
