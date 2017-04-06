package com.conuirwilliamson.popularmovies.models;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public class Trailer {

    private static final String TAG = Trailer.class.getSimpleName();

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({YOUTUBE_TRAILER, QUICKTIME_TRAILER})
    private @interface TrailerType{}

    public static final int YOUTUBE_TRAILER = 0;
    public static final int QUICKTIME_TRAILER = 1;

    private static final String YOUTUBE = "youtube";
    private static final String QUICKTIME = "quicktime";

    private static final String NAME = "name";
    private static final String SIZE = "size";
    private static final String SOURCE = "source";
    private static final String TYPE = "type";

    private static final String STATUS_CODE = "status_code";
    private static final String STATUS_MESSAGE = "status_message";

    private String mTrailerName;
    private void setTrailerName(String value) { mTrailerName = value; }
    public String getTrailerName() { return mTrailerName; }
    
    private String mSize;
    private void setSize(String value) { mSize = value; }
    public String getSize() { return mSize; }

    private String mSource;
    private void setSource(String value) { mSource = "https://youtube.com/watch?v=" + value;
        mThumbnail = "https://i3.ytimg.com/vi/" + value + "/0.jpg"; }
    public String getSource() { return mSource; }

    private String mThumbnail;
    public String getThumbnail() { return mThumbnail; }

    private String mType;
    private void setType(String value) { mType = value; }
    public String getType() { return mType; }

    private static int mStatusCode;
    private static void setStatusCode(int value) { mStatusCode = value; }
    public static int getStatusCode() { return mStatusCode; }

    private Trailer(){}

    public static Trailer getTrailerFromJson(@NonNull JSONObject json) throws JSONException {
        if (json.has(STATUS_CODE)) {
            int statusCode = json.getInt(STATUS_CODE);
            Log.e(TAG, "Error retrieving trailer. -- Status Code: " + statusCode + " -- Status Message: " + json.getString(STATUS_MESSAGE));
            Trailer.setStatusCode(statusCode);
            return null;
        }

        Trailer trailer = new Trailer();

        if(json.has(NAME)){
            trailer.setTrailerName(json.getString(NAME));
        }
        if(json.has(SIZE)){
            trailer.setSize(json.getString(SIZE));
        }
        if(json.has(SOURCE)){
            trailer.setSource(json.getString(SOURCE));
        }
        if(json.has(TYPE)){
            trailer.setType(json.getString(TYPE));
        }

        return trailer;
    }

    public static Trailer getTrailerFromJsonString(@NonNull String json) throws JSONException{
        return getTrailerFromJson(new JSONObject(json));
    }

    public static ArrayList<Trailer> getTrailersFromJson(@NonNull JSONObject json, @TrailerType int site) throws JSONException{
        if (json.has(STATUS_CODE)) {
            int statusCode = json.getInt(STATUS_CODE);
            Log.e(TAG, "Error retrieving trailers. -- Status Code: " + statusCode + " -- Status Message: " + json.getString(STATUS_MESSAGE));
            Trailer.setStatusCode(statusCode);
            return null;
        }

        String siteKey = null;
        switch(site){
            case YOUTUBE_TRAILER:
                siteKey = YOUTUBE;
                break;
            case QUICKTIME_TRAILER:
                throw new IllegalArgumentException("Quicktime trailers not yet implemented");
        }

        JSONArray trailersJSONArray = json.getJSONArray(siteKey);

        int len = trailersJSONArray.length();
        ArrayList<Trailer> trailers = new ArrayList<>(len);

        for(int i = 0; i < len; i++){
            Trailer trailer = getTrailerFromJson(trailersJSONArray.getJSONObject(i));
            if(trailer != null && "Trailer".equals(trailer.getType())) trailers.add(trailer);
        }

        return trailers;
    }

    public static ArrayList<Trailer> getTrailersFromJsonString(@NonNull String json, @TrailerType int site) throws JSONException{
        return getTrailersFromJson(new JSONObject(json), site);
    }

    public String toString(){
        return getTrailerName() + " (" + getSize() + ")";
    }
}
