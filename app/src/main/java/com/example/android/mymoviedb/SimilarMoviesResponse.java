package com.example.android.mymoviedb;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Archita on 22-08-2017.
 */

public class SimilarMoviesResponse {

    @SerializedName("page")
    private Integer page;
    @SerializedName("results")
    private List<Movie.Results> results;
    @SerializedName("total_pages")
    private Integer totalPages;
    @SerializedName("total_results")
    private Integer totalResults;

//    public SimilarMoviesResponse(Integer page, List<Movie> results, Integer totalPages, Integer totalResults) {
//        this.page = page;
//        this.results = results;
//        this.totalPages = totalPages;
//        this.totalResults = totalResults;
//    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Movie.Results> getResults() {
        return results;
    }

    public void setResults(List<Movie.Results> results) {
        this.results = results;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
}
