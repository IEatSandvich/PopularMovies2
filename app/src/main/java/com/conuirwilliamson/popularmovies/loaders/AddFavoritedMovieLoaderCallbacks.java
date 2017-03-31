package com.conuirwilliamson.popularmovies.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.conuirwilliamson.popularmovies.R;
import com.conuirwilliamson.popularmovies.models.Movie;
import com.conuirwilliamson.popularmovies.utilities.DatabaseUtil;

public class AddFavoritedMovieLoaderCallbacks implements LoaderManager.LoaderCallbacks<Long> {

    private final Context context;
    private final LoaderFinishedHandler handler;

    public AddFavoritedMovieLoaderCallbacks(@NonNull Context context, @NonNull LoaderFinishedHandler handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    public Loader<Long> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Long>(context) {

            private Movie movie;

            @Override
            protected void onStartLoading() {
                String movieKey;
                if(args != null && args.containsKey(movieKey = context.getString(R.string.bundle_movie))) {
                    movie = args.getParcelable(movieKey);
                }

                if (movie != null) {
                    forceLoad();
                } else {
                    deliverResult(-1L);
                }
            }

            @Override
            public Long loadInBackground() {
                return DatabaseUtil.addFavoritedMovie(context, movie);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Long> loader, Long data) {
        handler.addFavoritedMovieLoaderFinished(loader, data);
    }

    @Override
    public void onLoaderReset(Loader<Long> loader) {}

    public interface LoaderFinishedHandler{
        void addFavoritedMovieLoaderFinished(Loader<Long> loader, Long data);
    }

}