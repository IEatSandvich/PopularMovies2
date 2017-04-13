package com.conuirwilliamson.popularmovies.models;

import java.util.ArrayList;

/**
 * Created by conuirwilliamson on 13/04/2017.
 */

public class TrailersResponse {

    ArrayList<Trailer> youtube;

    public ArrayList<Trailer> getYoutubeResults() { return youtube; }

    public TrailersResponse(){
        youtube = new ArrayList<>();
    }

}
