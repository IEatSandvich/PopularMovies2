package com.conuirwilliamson.popularmovies.models;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by conuirwilliamson on 14/03/2017.
 */

public class Review {

    public static final String TAG = Review.class.getSimpleName();

    private static final String PAGE = "page";
    private static final String RESULTS = "results";
    private static final String TOTAL_PAGES = "total_pages";
    private static final String TOTAL_RESULTS = "total_results";

    private static final String ID = "id";
    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";
    private static final String URL = "url";

    private static final String STATUS_CODE = "status_code";
    private static final String STATUS_MESSAGE = "status_message";

    private String mId;
    private void setId(String value) { mId = value; }
    public String getId() { return mId; }

    private String mAuthor;
    private void setAuthor(String value) { mAuthor = value; }
    public String getAuthor() { return mAuthor; }

    private String mContent;
    private void setContent(String value) { mContent = value; }
    public String getContent() { return mContent; }

    private String mReviewUrl;
    private void setReviewUrl(String value) { mReviewUrl = value; }
    public String getReviewUrl() { return mReviewUrl; }

    private static int mStatusCode;
    private static void setStatusCode(int value) { mStatusCode = value; }
    public static int getStatusCode() { return mStatusCode; }

    private Review(){}

    public static Review getReviewFromJson(@NonNull JSONObject json) throws JSONException{
        if (json.has(STATUS_CODE)) {
            int statusCode = json.getInt(STATUS_CODE);
            Log.w(TAG, "Error retrieving review. -- Status Code: " + statusCode + " -- Status Message: " + json.getString(STATUS_MESSAGE));
            Review.setStatusCode(statusCode);
            return null;
        }

        Review review = new Review();

        if(json.has(AUTHOR)){
            review.setAuthor(json.getString(AUTHOR));
        }
        if(json.has(CONTENT)){
            review.setContent(json.getString(CONTENT));
        }
        if(json.has(ID)){
            review.setId(json.getString(ID));
        }
        if(json.has(URL)){
            review.setReviewUrl(json.getString(URL));
        }

        return review;
    }

    public static Review getReviewFromJsonString(@NonNull String json) throws JSONException {
        return getReviewFromJson(new JSONObject(json));
    }

    public static ArrayList<Review> getReviewsFromJson(@NonNull JSONObject json) throws JSONException{
        if (json.has(STATUS_CODE)) {
            int statusCode = json.getInt(STATUS_CODE);
            Log.w(TAG, "Error retrieving reviews. -- Status Code: " + statusCode + " -- Status Message: " + json.getString(STATUS_MESSAGE));
            Review.setStatusCode(statusCode);
            return null;
        }

        JSONArray reviewsJSONArray = json.getJSONArray(RESULTS);

        int len = reviewsJSONArray.length();
        ArrayList<Review> reviews = new ArrayList<>(len);

        for(int i = 0; i < len; i++){
            reviews.add(getReviewFromJson(reviewsJSONArray.getJSONObject(i)));
        }

        return reviews;
    }

    public static ArrayList<Review> getReviewsFromJsonString(@NonNull String json) throws JSONException{
        return getReviewsFromJson(new JSONObject(json));
    }

    public String toString() {
        String toReturn = getAuthor() + "  -  ";
        String content = getContent();

        toReturn += content.length() > 200 ?
                content.replace("\r\n", " ").substring(0, 200).trim() + "..." :
                content;

        return toReturn;
    }
}
