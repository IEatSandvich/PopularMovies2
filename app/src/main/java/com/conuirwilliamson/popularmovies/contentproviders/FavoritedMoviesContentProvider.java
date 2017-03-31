package com.conuirwilliamson.popularmovies.contentproviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.conuirwilliamson.popularmovies.data.FavouriteMoviesContract;
import com.conuirwilliamson.popularmovies.data.FavouriteMoviesContract.FavouriteMoviesTable;
import com.conuirwilliamson.popularmovies.data.PopularMoviesDbHelper;

public class FavoritedMoviesContentProvider extends ContentProvider {

    public static final String TAG = FavoritedMoviesContentProvider.class.getSimpleName();

    private PopularMoviesDbHelper dbHelper;

    public static final int FAVORITE_MOVIES = 100;
    public static final int FAVORITE_MOVIE_WITH_ID = 101;
    public static final int FAVORITE_MOVIES_CLEANUP = 102;

    @Override
    public boolean onCreate() {
        dbHelper = new PopularMoviesDbHelper(getContext());
        return true;
    }

    private static UriMatcher matcher;
    public static UriMatcher getUriMatcher(){
        if(matcher != null) return matcher;

        matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(FavouriteMoviesContract.AUTHORITY,
                FavouriteMoviesContract.PATH_FAVORITE_MOVIES,
                FAVORITE_MOVIES);
        matcher.addURI(FavouriteMoviesContract.AUTHORITY,
                FavouriteMoviesContract.PATH_FAVORITE_MOVIES + "/#",
                FAVORITE_MOVIE_WITH_ID);
        matcher.addURI(FavouriteMoviesContract.AUTHORITY,
                FavouriteMoviesContract.PATH_FAVORITE_MOVIES + "/" + FavouriteMoviesContract.PATH_CLEANUP,
                FAVORITE_MOVIES_CLEANUP);

        return matcher;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        int uriCode = getUriMatcher().match(uri);

        switch (uriCode){
            case FAVORITE_MOVIES:
                return db.query(FavouriteMoviesTable.TABLE_NAME, projection, FavouriteMoviesTable.COLUMN_FAVORITED + "=1", null, null, null, sortOrder);
            case FAVORITE_MOVIE_WITH_ID:
                return db.query(FavouriteMoviesTable.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int uriCode = getUriMatcher().match(uri);

        switch (uriCode){
            case FAVORITE_MOVIES:
                long id = db.insert(FavouriteMoviesTable.TABLE_NAME, null, values);
                if(id <= 0){
                    throw new SQLiteException("Failed to insert row into " + uri);
                }
                return FavouriteMoviesTable.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int uriCode = getUriMatcher().match(uri);
        int rowsAffected;

        switch (uriCode){
            case FAVORITE_MOVIES:
                rowsAffected = db.delete(FavouriteMoviesTable.TABLE_NAME, "1", null);
                notifyContentResolver(uri);
                break;
            case FAVORITE_MOVIE_WITH_ID:
                int id = Integer.parseInt(uri.getPathSegments().get(1));
                rowsAffected = db.delete(FavouriteMoviesTable.TABLE_NAME, FavouriteMoviesTable._ID + "=" + id, null);
                notifyContentResolver(uri);
                break;
            case FAVORITE_MOVIES_CLEANUP:
                rowsAffected = db.delete(FavouriteMoviesTable.TABLE_NAME, FavouriteMoviesTable.COLUMN_FAVORITED + "=0", null);
                notifyContentResolver(uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return rowsAffected;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int uriCode = getUriMatcher().match(uri);
        int rowsAffected;

        switch (uriCode){
            case FAVORITE_MOVIE_WITH_ID:
                int id = Integer.parseInt(uri.getPathSegments().get(1));
                String whereClause = FavouriteMoviesTable._ID + "=?";
                String[] whereArgs = new String[]{String.valueOf(id)};

                rowsAffected = db.update(FavouriteMoviesTable.TABLE_NAME, values, whereClause, whereArgs);
                notifyContentResolver(uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return rowsAffected;
    }

    private void notifyContentResolver(Uri uri){
        getContext().getContentResolver().notifyChange(uri,null);
    }
}
