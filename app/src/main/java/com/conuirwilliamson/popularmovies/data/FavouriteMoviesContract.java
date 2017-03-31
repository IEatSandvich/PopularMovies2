package com.conuirwilliamson.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavouriteMoviesContract {

    private FavouriteMoviesContract(){}

    public static final String AUTHORITY = "com.conuirwilliamson.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVORITE_MOVIES = "favoriteMovies";
    public static final String PATH_CLEANUP = "cleanup";

    public static final class FavouriteMoviesTable implements BaseColumns{

        public static final String TABLE_NAME = "favouriteMovies";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();
        public static final Uri CLEANUP_URI = CONTENT_URI.buildUpon().appendPath(PATH_CLEANUP).build();

        public static final String COLUMN_MOVIE_TITLE = "movieTitle";
        public static final String COLUMN_MOVIE_POSTER = "moviePoster";
        public static final String COLUMN_FAVORITED = "favorited";
        public static final String COLUMN_CREATED = "created";
        public static final String COLUMN_UPDATED = "updated";

        public static final String DEFINITION_ID = "INTEGER PRIMARY KEY";
        public static final String DEFINITION_MOVIE_TITLE = "TEXT NOT NULL";
        public static final String DEFINITION_MOVIE_POSTER = "TEXT NOT NULL";
        public static final String DEFINITION_FAVORITED = "BIT NOT NULL DEFAULT 1";
        public static final String DEFINITION_CREATED = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP";
        public static final String DEFINITION_UPDATED = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP";

        public static final String[] COLUMN_NAMES = new String[]{
                _ID,
                COLUMN_MOVIE_TITLE,
                COLUMN_MOVIE_POSTER,
                COLUMN_FAVORITED,
                COLUMN_CREATED,
                COLUMN_UPDATED
        };

        public static final String[] COLUMN_DEFINITIONS = new String[]{
                DEFINITION_ID, // Note: the _ID field will be used to store the Movie ID therefore this is not AUTOINCREMENT
                DEFINITION_MOVIE_TITLE,
                DEFINITION_MOVIE_POSTER,
                DEFINITION_FAVORITED,
                DEFINITION_CREATED,
                DEFINITION_UPDATED
        };

    }

}
