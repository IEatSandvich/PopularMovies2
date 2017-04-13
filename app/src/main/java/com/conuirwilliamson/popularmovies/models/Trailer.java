package com.conuirwilliamson.popularmovies.models;

import android.support.annotation.IntDef;

import com.google.gson.annotations.Expose;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Trailer {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({YOUTUBE_TRAILER, QUICKTIME_TRAILER})
    private @interface TrailerType{}

    static final int YOUTUBE_TRAILER = 0;
    static final int QUICKTIME_TRAILER = 1;

    @Expose
    private String name;
    public String getName() { return name; }

    @Expose
    private String size;
    public String getSize() { return size; }

    @Expose
    private String source;
    public String getSource() { return "https://youtube.com/watch?v=" + source; }

    @Expose
    private String type;
    public String getType() { return type; }

    public String getThumbnail() { return "https://i3.ytimg.com/vi/" + source + "/0.jpg"; }

    private Trailer(){}

    public String toString(){
        return name + " (" + size + ")";
    }
}
