package com.conuirwilliamson.popularmovies.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.conuirwilliamson.popularmovies.activities.DetailsActivity;
import com.conuirwilliamson.popularmovies.R;
import com.conuirwilliamson.popularmovies.utilities.DatabaseUtil;

public class UpdateFavoritedMovieLoaderCallbacks implements LoaderManager.LoaderCallbacks<Integer> {

    private final Context context;
    private final LoaderFinishedHandler handler;

    public UpdateFavoritedMovieLoaderCallbacks(@NonNull Context context, @NonNull LoaderFinishedHandler handler){
        this.context = context;
        this.handler = handler;
    }

    @Override
    public Loader<Integer> onCreateLoader(int id, final Bundle args) {
        switch(id){
            case DetailsActivity.UPDATE_MOVIE_IS_FAVORITED_LOADER:
                return new AsyncTaskLoader<Integer>(context) {

                    private int movieId = -1;
                    private boolean movieIsFavorited = false;

                    @Override
                    protected void onStartLoading() {
                        String movieIdKey;
                        String movieIsFavoritedKey;

                        if(args != null && args.containsKey(movieIdKey = context.getString(R.string.bundle_movie_id))){

                            movieId = args.getInt(movieIdKey);

                            if(args.containsKey(movieIsFavoritedKey = context.getString(R.string.bundle_movie_is_favorited))){
                                movieIsFavorited = args.getBoolean(movieIsFavoritedKey);
                            }
                        }

                        if(movieId != -1){
                            forceLoad();
                        } else {
                            deliverResult(-1);
                        }
                    }

                    @Override
                    public Integer loadInBackground() {
                        return DatabaseUtil.updateFavoritedMovieFavorited(context, movieId, movieIsFavorited);
                    }
                };
            case DetailsActivity.UPDATE_MOVIE_POSTER_LOADER:
                return new AsyncTaskLoader<Integer>(context) {
                    private int movieId = -1;
                    private String moviePoster;

                    @Override
                    protected void onStartLoading() {
                        String movieIdKey;
                        String moviePosterKey;

                        if(args != null && args.containsKey(movieIdKey = context.getString(R.string.bundle_movie_id)) && args.containsKey(moviePosterKey = context.getString(R.string.bundle_movie_poster))){
                            movieId = args.getInt(movieIdKey);
                            moviePoster = args.getString(moviePosterKey);
                        }

                        if(movieId != -1){
                            forceLoad();
                        } else {
                            deliverResult(-1);
                        }
                    }

                    @Override
                    public Integer loadInBackground() {
                        return DatabaseUtil.updateFavoritedMoviePoster(context, movieId, moviePoster);
                    }
                };
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Integer> loader, Integer data) {
        switch(loader.getId()){
            case DetailsActivity.UPDATE_MOVIE_POSTER_LOADER:
                handler.updateFavoritedMoviePosterLoadFinished(loader, data);
                return;
            case DetailsActivity.UPDATE_MOVIE_IS_FAVORITED_LOADER:
                handler.updateFavoritedMovieLoadFinished(loader, data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Integer> loader) {}

    public interface LoaderFinishedHandler{
        void updateFavoritedMovieLoadFinished(Loader<Integer> loader, int data);
        void updateFavoritedMoviePosterLoadFinished(Loader<Integer> loader, int data);
    }
}
