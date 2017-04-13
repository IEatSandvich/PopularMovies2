package com.conuirwilliamson.popularmovies.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.conuirwilliamson.popularmovies.BuildConfig;
import com.conuirwilliamson.popularmovies.R;
import com.conuirwilliamson.popularmovies.activities.DetailsActivity;
import com.conuirwilliamson.popularmovies.models.Movie;
import com.conuirwilliamson.popularmovies.utilities.TheMovieDBUtil;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by conuirwilliamson on 13/04/2017.
 */

public class GetMovieLoaderCallbacks implements LoaderManager.LoaderCallbacks<Movie>{

    private Context context;
    private LoaderFinishedHandler handler;

    public GetMovieLoaderCallbacks(@NonNull Context context, @NonNull LoaderFinishedHandler handler){
        this.context = context;
        this.handler = handler;
    }

    @Override
    public Loader<Movie> onCreateLoader(int id, final Bundle args) {
        switch(id) {
            case DetailsActivity.MOVIE_DETAILS_LOADER:
                return new AsyncTaskLoader<Movie>(context) {

                    private Movie cache;
                    private int movieId = -2;

                    @Override
                    protected void onStartLoading() {
                        int currentMovieId;
                        if (cache == null &&
                                args != null &&
                                movieId != (currentMovieId = args.getInt(context.getString(R.string.bundle_movie_id), -1)) &&
                                currentMovieId != -1) {
                            movieId = currentMovieId;
                            forceLoad();
                        } else {
                            deliverResult(cache);
                        }
                    }

                    @Override
                    public Movie loadInBackground() {
                        try {
                            return cache  = TheMovieDBUtil.getAPIService().getMovieById(movieId, BuildConfig.TMDBAK).execute().body();
                        } catch (IOException e) {
                            // Errors handled in DetailActivity.movieDetailsLoadFinished()
                        }
                        return null;
                    }

                    @Override
                    public void deliverResult(Movie data) {
                        super.deliverResult(data);
                    }
                };
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {
        handler.movieLoaderFinished(loader, data);
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {}

    public interface LoaderFinishedHandler{
        void movieLoaderFinished(Loader<Movie> loader, Movie data);
    }
}
