package com.conuirwilliamson.popularmovies.models;

import android.support.annotation.IntDef;

import com.google.gson.annotations.Expose;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by conuirwilliamson on 13/04/2017.
 */

public class TheMovieDBResponse {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ERROR, SUCCESS})
    private @interface ResponseType{}

    public static final int SUCCESS = 0;
    public static final int ERROR = 1;

    @TheMovieDBResponse.ResponseType
    private int responseType;
    public int getResponseType(){ return responseType; }
    public void setResponseType(@ResponseType int value) { responseType = value; }

    @Expose
    private int status_code;
    public int getStatusCode() { return status_code; }

    @Expose
    private String status_message;
    public String getStatusMessage() { return status_message; }

}
