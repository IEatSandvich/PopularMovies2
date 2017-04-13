package com.conuirwilliamson.popularmovies.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.conuirwilliamson.popularmovies.R;
import com.conuirwilliamson.popularmovies.activities.DetailsActivity;
import com.conuirwilliamson.popularmovies.activities.MainActivity;
import com.conuirwilliamson.popularmovies.models.Movie;
import com.conuirwilliamson.popularmovies.utilities.DatabaseUtil;

public class GetFavoritedMoviesLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor>{

    private final Context context;
    private final LoaderFinishedHandler handler;

    public GetFavoritedMoviesLoaderCallbacks(@NonNull Context context, @NonNull LoaderFinishedHandler handler){
        this.context = context;
        this.handler = handler;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {
        switch(id) {
            case DetailsActivity.FAVORITED_MOVIE_LOADER:
                return new AsyncTaskLoader<Cursor>(context) {

                    private int movieId;

                    @Override
                    protected void onStartLoading() {
                        String movieIdKey;
                        if (args != null && args.containsKey(movieIdKey = context.getString(R.string.bundle_movie_id))) {
                            movieId = args.getInt(movieIdKey);
                            forceLoad();
                        } else {
                            deliverResult(null);
                        }
                    }

                    @Override
                    public Cursor loadInBackground() {
                        return DatabaseUtil.getFavoritedMovie(context, movieId);
                    }
                };
            case MainActivity.FAVORITED_MOVIES_LOADER:
                return new AsyncTaskLoader<Cursor>(context) {
                    private Cursor cache;

                    @Override
                    public void onStartLoading(){
                        String favoritedChangedKey;
                        if(cache == null ||
                            (args != null &&
                                    args.containsKey(favoritedChangedKey = context.getString(R.string.bundle_favorited_changed)) &&
                                    args.getBoolean(favoritedChangedKey, false))){
                            forceLoad();
                        } else {
                            deliverResult(cache);
                        }
                    }

                    @Override
                    public Cursor loadInBackground() {
                        return cache = DatabaseUtil.getFavoritedMovies(context);
                    }

                    @Override
                    public void deliverResult(Cursor data) {
                        super.deliverResult(data);
                    }
                };
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        handler.getFavoritedMoviesLoaderFinished(loader, data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    public interface LoaderFinishedHandler{
        void getFavoritedMoviesLoaderFinished(Loader<Cursor> loader, Cursor data);
    }
}
