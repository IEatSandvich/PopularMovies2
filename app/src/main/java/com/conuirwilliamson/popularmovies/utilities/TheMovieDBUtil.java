package com.conuirwilliamson.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.conuirwilliamson.popularmovies.BuildConfig;
import com.conuirwilliamson.popularmovies.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by conuirwilliamson on 29/03/2017.
 */

public class TheMovieDBUtil {

    private static final String TAG = TheMovieDBUtil.class.getSimpleName();

    public static final int INVALID_API_KEY = 7;
    public static final int RESOURCE_NOT_FOUND = 34;

    private final static String BASE_URL = "https://api.themoviedb.org/3";
    private final static String BASE_IMAGE_URL = "https://image.tmdb.org/t/p";
    private final static String MOVIE_PATH = "movie";
    private final static String POPULAR_PATH = "popular";
    private final static String TOP_RATED_PATH = "top_rated";
    private final static String REVIEWS_PATH = "reviews";
    private final static String TRAILERS_PATH = "trailers";

    private final static String QUERY_PARAM = "query";
    private final static String API_KEY_PARAM = "api_key";

    private final static int WIDTH_DEFAULT = R.string.w_185;

    public static String formatDate(Context context, String date){
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy");
        try {
            return newDateFormat.format(oldDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Error parsing date. Given date: " + date);
            return context.getResources().getString(R.string.unknown);
        }
    }

    public static String[] getStringsForGenreIds(Context context, int[] genreIds) {
        int len = genreIds.length;
        String[] genres = new String[len];
        for (int i = 0; i < len; i++) genres[i] = getStringForGenreId(context, genreIds[i]);
        return genres;
    }

    public static String getStringForGenreId(Context context, int genreId){
        int stringId;
        switch(genreId){
            case 28:
                stringId = R.string.genre_28;
                break;
            case 12:
                stringId = R.string.genre_12;
                break;
            case 16:
                stringId = R.string.genre_16;
                break;
            case 35:
                stringId = R.string.genre_35;
                break;
            case 80:
                stringId = R.string.genre_80;
                break;
            case 99:
                stringId = R.string.genre_99;
                break;
            case 18:
                stringId = R.string.genre_18;
                break;
            case 10751:
                stringId = R.string.genre_10751;
                break;
            case 14:
                stringId = R.string.genre_14;
                break;
            case 36:
                stringId = R.string.genre_36;
                break;
            case 27:
                stringId = R.string.genre_27;
                break;
            case 10402:
                stringId = R.string.genre_10402;
                break;
            case 9648:
                stringId = R.string.genre_9648;
                break;
            case 10749:
                stringId = R.string.genre_10749;
                break;
            case 878:
                stringId = R.string.genre_878;
                break;
            case 10770:
                stringId = R.string.genre_10770;
                break;
            case 53:
                stringId = R.string.genre_53;
                break;
            case 10752:
                stringId = R.string.genre_10752;
                break;
            case 37:
                stringId = R.string.genre_37;
                break;
            default:
                stringId = R.string.no_genre;
        }
        return context.getResources().getString(stringId);
    }

    public static URL getMostPopularUrl(){
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(POPULAR_PATH)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.TMDBAK)
                .build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL getTopRatedUrl(){
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(TOP_RATED_PATH)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.TMDBAK)
                .build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Uri getImageUri(Context context, String imageName){ return getImageUri(context, imageName, WIDTH_DEFAULT); }

    public static Uri getImageUri(Context context, String imageName, int widthId) {
        return Uri.parse(Uri.parse(BASE_IMAGE_URL).buildUpon()
                .appendPath(context.getResources().getString(widthId))
                .build().toString().concat(imageName));
    }

    public static URL getMovieUrl(int movieID){
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(String.valueOf(movieID))
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.TMDBAK)
                .build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL getMovieReviewsUrl(int movieID){
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(String.valueOf(movieID))
                .appendPath(REVIEWS_PATH)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.TMDBAK)
                .build();
        try{
            return new URL(uri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static URL getMovieTrailersUrl(int movieID){
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(String.valueOf(movieID))
                .appendPath(TRAILERS_PATH)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.TMDBAK)
                .build();
        try{
            return new URL(uri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }
    }
}