package com.conuirwilliamson.popularmovies.models;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.conuirwilliamson.popularmovies.data.FavouriteMoviesContract;
import com.conuirwilliamson.popularmovies.utilities.TheMovieDBUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Movie implements Parcelable{

    private static final String TAG = Movie.class.getSimpleName();

    private static final String PAGE = "page";
    private static final String RESULTS = "results";
    private static final String TOTAL_RESULTS = "total_results";
    private static final String TOTAL_PAGES = "total_pages";

    private static final String POSTER_PATH = "poster_path";
    private static final String ADULT = "adult";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String GENRE_IDS = "genre_ids";
    private static final String ID = "id";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String ORIGINAL_LANGUAGE = "original_language";
    private static final String TITLE = "title";
    private static final String BACKDROP_PATH = "backdrop_path";
    private static final String POPULARITY = "popularity";
    private static final String VOTE_COUNT = "vote_count";
    private static final String VIDEO = "video";
    private static final String VOTE_AVERAGE = "vote_average";

    private static final String IMDB_ID = "imdb_id";
    private static final String HOMEPAGE = "homepage";
    private static final String RUNTIME = "runtime";
    private static final String BUDGET = "budget";

    private static final String STATUS_CODE = "status_code";
    private static final String STATUS_MESSAGE = "status_message";


    private static int mStatusCode;
    private static void setStatusCode(int value) { mStatusCode = value; }
    public static int getStatusCode() { return mStatusCode; }

    private String mPosterPath;
    private void setPosterPath(String value) { mPosterPath = value; }
    public String getPosterPath(){ return mPosterPath; }

    private String mOverview;
    private void setOverview(String value) { mOverview = value; }
    public String getOverview() { return mOverview; }

    private String mReleaseDate;
    private void setReleaseDate(String value) { mReleaseDate = value; }
    public String getReleaseDate() { return mReleaseDate; }

    private int mID;
    private void setID(int value) { mID = value; }
    public int getID() { return mID; }

    private String mTitle;
    private void setTitle(String value) { mTitle = value; }
    public String getTitle() { return mTitle; }

    private String mBackdropPath;
    private void setBackdropPath(String value) { mBackdropPath = value; }
    public String getBackdropPath() { return mBackdropPath; }

    private String mVoteAverage;
    private void setVoteAverage(String value) { mVoteAverage = value; }
    public String getVoteAverage() { return mVoteAverage; }

    private String mRuntime;
    private void setRuntime(String value) { mRuntime = value; }
    public String getRuntime() { return mRuntime; }

//    private boolean mIsAdult;
//    private void setIsAdult(boolean value) { mIsAdult = value; }
//    public boolean getIsAdult() { return mIsAdult; }

//    private String[] mGenres;
//    private void setGenres(String[] value) { mGenres = value; }
//    public String[] getGenres() { return mGenres; }

//    private String mOriginalTitle;
//    private void setOriginalTitle(String value) { mOriginalTitle = value; }
//    public String getOriginalTitle() { return mOriginalTitle; }

//    private String mOriginalLanguage;
//    private void setOriginalLanguage(String value) { mOriginalLanguage = value; }
//    public String getOriginalLanguage() { return mOriginalLanguage; }

//    private double mPopularity;
//    private void setPopularity(double value) { mPopularity = value; }
//    public double getPopularity() { return mPopularity; }

//    private int mVoteCount;
//    private void setVoteCount(int value) { mVoteCount = value; }
//    public int getVoteCount() { return mVoteCount; }

//    private boolean mVideo;
//    private void setVideo(boolean value) { mVideo = value; }
//    public boolean getVideo() { return mVideo; }

//    private String mImdbId;
//    private void setImdbId(String value) { mImdbId = value; }
//    public String getImdbId() { return mImdbId; }

//    private String mHomepage;
//    private void setHomepage(String value) { mHomepage = value; }
//    public String getHomepage() { return mHomepage; }

//    private int mBudget;
//    private void setBudget(int value) { mBudget = value; }
//    public int getBudget() { return mBudget; }

    private Movie(){}

    public static final Creator<Movie> CREATOR
            = new Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in){
        mPosterPath = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mID = in.readInt();
        mTitle = in.readString();
        mBackdropPath = in.readString();
        mVoteAverage = in.readString();
        mRuntime = in.readString();
//        mIsAdult = in.readInt() == 1;
//        mGenres = in.createStringArray();
//        mOriginalTitle = in.readString();
//        mOriginalLanguage = in.readString();
//        mPopularity = in.readDouble();
//        mVoteCount = in.readInt();
//        mVideo = in.readInt() == 1;
//        mImdbId = in.readString();
//        mHomepage = in.readString();
//        mBudget = in.readInt();
    }

    public static Movie getMovieFromJSON(@NonNull Context context, @NonNull JSONObject json) throws JSONException {
        if(json.has(STATUS_CODE)){
            int statusCode = json.getInt(STATUS_CODE);
            Log.w(TAG, "Error retrieving movie. -- Status Code: " + statusCode + " -- Status Message: " + json.getString(STATUS_MESSAGE));
            Movie.setStatusCode(statusCode);
            return null;
        }

        Movie movie = new Movie();

        if(json.has(POSTER_PATH)){
            movie.setPosterPath(json.getString(POSTER_PATH));
        }
        if(json.has(OVERVIEW)){
            movie.setOverview(json.getString(OVERVIEW));
        }
        if(json.has(RELEASE_DATE)){
            movie.setReleaseDate(TheMovieDBUtil.formatDate(context, json.getString(RELEASE_DATE)));
        }
        if(json.has(ID)){
            movie.setID(json.getInt(ID));
        }
        if(json.has(TITLE)){
            movie.setTitle(json.getString(TITLE));
        }
        if(json.has(BACKDROP_PATH)){
            movie.setBackdropPath(json.getString(BACKDROP_PATH));
        }
        if(json.has(VOTE_AVERAGE)){
            movie.setVoteAverage(json.getDouble(VOTE_AVERAGE) + " / 10.0");
        }
        if(json.has(RUNTIME)){
            movie.setRuntime(json.getInt(RUNTIME) + " min");
        }
//        if(json.has(ADULT)){
//            movie.setIsAdult(json.getBoolean(ADULT));
//        }
//        if(json.has(GENRE_IDS)){
//            JSONArray genreIdsJSONArray = json.getJSONArray(GENRE_IDS);
//            int len = genreIdsJSONArray.length();
//            int[] genreIds = new int[len];
//            for(int i = 0; i < len; i++){
//                genreIds[i] = genreIdsJSONArray.getInt(i);
//            }
//            movie.setGenres(TheMovieDBUtil.getStringsForGenreIds(context, genreIds));
//        }
//        if(json.has(ORIGINAL_TITLE)){
//            movie.setOriginalTitle(json.getString(ORIGINAL_TITLE));
//        }
//        if(json.has(ORIGINAL_LANGUAGE)){
//            movie.setOriginalLanguage(json.getString(ORIGINAL_LANGUAGE));
//        }
//        if(json.has(POPULARITY)){
//            movie.setPopularity(json.getDouble(POPULARITY));
//        }
//        if(json.has(VOTE_COUNT)){
//            movie.setVoteCount(json.getInt(VOTE_COUNT));
//        }
//        if(json.has(VIDEO)){
//            movie.setVideo(json.getBoolean(VIDEO));
//        }
//        if(json.has(IMDB_ID)){
//            movie.setImdbId(json.getString(IMDB_ID));
//        }
//        if(json.has(HOMEPAGE)){
//            movie.setHomepage(json.getString(HOMEPAGE));
//        }
//        if(json.has(BUDGET)){
//            movie.setBudget(json.getInt(BUDGET));
//        }
        return movie;
    }

    public static ArrayList<Movie> getMoviesFromJSON(@NonNull Context context, @NonNull JSONObject json) throws JSONException {
        if(json.has(STATUS_CODE)){
            int statusCode = json.getInt(STATUS_CODE);
            Log.w(TAG, "Error retrieving movies. -- Status Code: " + statusCode + " -- Status Message: " + json.getString(STATUS_MESSAGE));
            Movie.setStatusCode(statusCode);
            return null;
        }

        JSONArray moviesJSONArray = json.getJSONArray(RESULTS);

        int len = moviesJSONArray.length();
        ArrayList<Movie> movies = new ArrayList<>(len);

        for(int i = 0; i < len; i++){
            movies.add(getMovieFromJSON(context, moviesJSONArray.getJSONObject(i)));
        }

        return movies;
    }

    public static Movie getMovieFromJSONString(@NonNull Context context, @NonNull String json) throws JSONException {
        return getMovieFromJSON(context, new JSONObject(json));
    }

    public static ArrayList<Movie> getFavoritedMoviesFromCursor(@NonNull Cursor cursor) {
        if(cursor.getCount() == 0) return null;

        ArrayList<Movie> result = new ArrayList<>(cursor.getCount());

        int titleColumn = cursor.getColumnIndex(FavouriteMoviesContract.FavouriteMoviesTable.COLUMN_MOVIE_TITLE);
        int posterColumn = cursor.getColumnIndex(FavouriteMoviesContract.FavouriteMoviesTable.COLUMN_MOVIE_POSTER);
        int idColumn = cursor.getColumnIndex(FavouriteMoviesContract.FavouriteMoviesTable._ID);

        while(cursor.moveToNext()){
            Movie movie = new Movie();
            movie.setPosterPath(cursor.getString(posterColumn));
            movie.setTitle(cursor.getString(titleColumn));
            movie.setID(cursor.getInt(idColumn));
            result.add(movie);
        }

        cursor.close();

        return result;
    }

    public static ArrayList<Movie> getMoviesFromJSONString(@NonNull Context context, @NonNull String json) throws JSONException{ return getMoviesFromJSON(context, new JSONObject(json)); }



    @Override
    public String toString() { return getTitle(); }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPosterPath);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeInt(mID);
        dest.writeString(mTitle);
        dest.writeString(mBackdropPath);
        dest.writeString(mVoteAverage);
        dest.writeString(mRuntime);
//        dest.writeInt(mIsAdult ? 1 : 0);
//        dest.writeStringArray(mGenres);
//        dest.writeString(mOriginalTitle);
//        dest.writeString(mOriginalLanguage);
//        dest.writeDouble(mPopularity);
//        dest.writeInt(mVoteCount);
//        dest.writeInt(mVideo ? 1 : 0);
//        dest.writeString(mImdbId);
//        dest.writeString(mHomepage);
//        dest.writeInt(mBudget);
    }
}
