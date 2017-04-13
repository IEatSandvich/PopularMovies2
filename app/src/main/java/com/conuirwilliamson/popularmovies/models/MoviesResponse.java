package com.conuirwilliamson.popularmovies.models;

import java.util.ArrayList;

/**
 * Created by conuirwilliamson on 11/04/2017.
 */

public class MoviesResponse extends PagedResponse{

    ArrayList<Movie> results;

    public ArrayList<Movie> getResults() { return results; }

    public MoviesResponse(){
        results = new ArrayList<>();
    }
}
