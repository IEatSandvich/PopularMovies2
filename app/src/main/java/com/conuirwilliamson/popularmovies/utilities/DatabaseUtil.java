package com.conuirwilliamson.popularmovies.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.conuirwilliamson.popularmovies.contentproviders.FavoritedMoviesContentProvider;
import com.conuirwilliamson.popularmovies.data.FavouriteMoviesContract.*;
import com.conuirwilliamson.popularmovies.models.Movie;

/**
 * Created by conuirwilliamson on 29/03/2017.
 */

public class DatabaseUtil {

    public static final String TAG = DatabaseUtil.class.getSimpleName();

    public static Cursor getFavoritedMovie(@NonNull Context context, int movieId){
        return context.getContentResolver().query(FavouriteMoviesTable.CONTENT_URI.buildUpon().appendPath(String.valueOf(movieId)).build(),
                new String[]{FavouriteMoviesTable.COLUMN_FAVORITED,
                        FavouriteMoviesTable.COLUMN_MOVIE_POSTER},
                FavouriteMoviesTable._ID + "=" + movieId,
                null,
                null);
    }

    public static int updateFavoritedMovieFavorited(@NonNull Context context, @NonNull int movieId, boolean favorited){

        ContentValues values = new ContentValues();
        values.put(FavouriteMoviesTable.COLUMN_FAVORITED, favorited);

        return context.getContentResolver().update(FavouriteMoviesTable.CONTENT_URI.buildUpon().appendPath(String.valueOf(movieId)).build(),
                values,
                null,
                null);
    }

    public static long addFavoritedMovie(@NonNull Context context, @NonNull Movie movie){

        ContentValues values = new ContentValues();
        values.put(FavouriteMoviesTable._ID, movie.getID());
        values.put(FavouriteMoviesTable.COLUMN_MOVIE_POSTER, movie.getPosterPath());
        values.put(FavouriteMoviesTable.COLUMN_MOVIE_TITLE, movie.getTitle());

        return getFavoritedIdFromUri(context.getContentResolver().insert(FavouriteMoviesTable.CONTENT_URI,
                values));
    }

    public static int updateFavoritedMoviePoster(@NonNull Context context, @NonNull int movieId, @NonNull String moviePosterPath) {

        ContentValues values = new ContentValues();
        values.put(FavouriteMoviesTable.COLUMN_MOVIE_POSTER, moviePosterPath);

        return context.getContentResolver().update(FavouriteMoviesTable.CONTENT_URI.buildUpon().appendPath(String.valueOf(movieId)).build(),
                values,
                null,
                null);
    }

    public static Cursor getFavoritedMovies(@NonNull Context context) {

        String[] projection = new String[]{FavouriteMoviesTable._ID,
                FavouriteMoviesTable.COLUMN_MOVIE_TITLE,
                FavouriteMoviesTable.COLUMN_MOVIE_POSTER};

        return context.getContentResolver().query(FavouriteMoviesTable.CONTENT_URI,
                projection,
                FavouriteMoviesTable.COLUMN_FAVORITED + "=" + 1,
                null,
                FavouriteMoviesTable.COLUMN_UPDATED);
    }

    public static long getFavoritedIdFromUri(Uri uri){
        int uriCode = FavoritedMoviesContentProvider.getUriMatcher().match(uri);
        if (uriCode == FavoritedMoviesContentProvider.FAVORITE_MOVIE_WITH_ID){
            return Long.parseLong(uri.getPathSegments().get(1));
        }
        return -1;
    }
}