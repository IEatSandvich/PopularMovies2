package com.conuirwilliamson.popularmovies.models;

/**
 * Created by conuirwilliamson on 13/04/2017.
 */

public class PagedResponse extends TheMovieDBResponse {

    public PagedResponse nextPage;
    public PagedResponse previousPage;

    int page;
    int total_results;
    int total_pages;

    public int getPage() { return page; }
    public int getTotalResults() { return total_results; }
    public int getTotalPages() { return total_pages; }

}
