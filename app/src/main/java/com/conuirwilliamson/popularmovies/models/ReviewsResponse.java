package com.conuirwilliamson.popularmovies.models;

import java.util.ArrayList;

/**
 * Created by conuirwilliamson on 13/04/2017.
 */

public class ReviewsResponse extends PagedResponse {

    ArrayList<Review> results;

    public ArrayList<Review> getResults() { return results; }

    public ReviewsResponse() { results = new ArrayList<>(); }

}
