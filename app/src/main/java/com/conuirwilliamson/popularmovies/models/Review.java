package com.conuirwilliamson.popularmovies.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by conuirwilliamson on 14/03/2017.
 */

public class Review {

    @Expose
    private String id;
    private void setId(String value) { id = value; }
    public String getId() { return id; }

    @Expose
    private String author;
    private void setAuthor(String value) { author = value; }
    public String getAuthor() { return author; }

    @Expose
    private String content;
    private void setContent(String value) { content = value; }
    public String getContent() { return content; }

    @Expose
    private String url;
    private void setReviewUrl(String value) { url = value; }
    public String getReviewUrl() { return url; }

    private Review(){}

    public String toString() {
        String toReturn = getAuthor() + "  -  ";
        String content = getContent();

        toReturn += content.length() > 200 ?
                content.replace("\r\n", " ").substring(0, 200).trim() + "..." :
                content;

        return toReturn;
    }
}
