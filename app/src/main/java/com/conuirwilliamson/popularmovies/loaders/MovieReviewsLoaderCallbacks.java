package com.conuirwilliamson.popularmovies.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.conuirwilliamson.popularmovies.R;
import com.conuirwilliamson.popularmovies.activities.DetailsActivity;
import com.conuirwilliamson.popularmovies.models.Review;
import com.conuirwilliamson.popularmovies.utilities.NetworkUtil;
import com.conuirwilliamson.popularmovies.utilities.TheMovieDBUtil;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by conuirwilliamson on 30/03/2017.
 */

public class MovieReviewsLoaderCallbacks  implements LoaderManager.LoaderCallbacks<ArrayList<Review>> {

    private final Context context;
    private final LoaderFinishedHandler handler;

    public MovieReviewsLoaderCallbacks (@NonNull Context context, @NonNull LoaderFinishedHandler handler){
        this.context = context;
        this.handler = handler;
    }

    @Override
    public Loader<ArrayList<Review>> onCreateLoader(int id, final Bundle args) {
        switch (id){
            case DetailsActivity.MOVIE_REVIEWS_LOADER:
                return new AsyncTaskLoader<ArrayList<Review>>(context) {

                    private ArrayList<Review> cache;
                    private int movieId;

                    @Override
                    protected void onStartLoading() {
                        if(cache != null ||
                                args == null ||
                                (movieId = args.getInt(context.getString(R.string.bundle_movie_id), -1)) == -1){
                            deliverResult(cache);
                        } else {
                            forceLoad();
                        }
                    }

                    @Override
                    public ArrayList<Review> loadInBackground() {
                        try {
                            String response = NetworkUtil.getResponseFromHttpUrl(TheMovieDBUtil.getMovieReviewsUrl(movieId));
                            return Review.getReviewsFromJsonString(response);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void deliverResult(ArrayList<Review> data) {
                        cache = data;
                        super.deliverResult(data);
                    }
                };
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> data) {
        handler.movieReviewsLoadFinished(loader, data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Review>> loader) {}

    public interface LoaderFinishedHandler{
        void movieReviewsLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> data);
    }
}