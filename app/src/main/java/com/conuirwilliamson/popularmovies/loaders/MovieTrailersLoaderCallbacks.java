package com.conuirwilliamson.popularmovies.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.conuirwilliamson.popularmovies.R;
import com.conuirwilliamson.popularmovies.activities.DetailsActivity;
import com.conuirwilliamson.popularmovies.models.Trailer;
import com.conuirwilliamson.popularmovies.utilities.NetworkUtil;
import com.conuirwilliamson.popularmovies.utilities.TheMovieDBUtil;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by conuirwilliamson on 30/03/2017.
 */

public class MovieTrailersLoaderCallbacks implements LoaderManager.LoaderCallbacks<ArrayList<Trailer>> {

    public static final String TAG = MovieTrailersLoaderCallbacks.class.getSimpleName();

    private final Context context;
    private final LoaderFinishedHandler handler;

    public MovieTrailersLoaderCallbacks(@NonNull Context context, @NonNull LoaderFinishedHandler handler){
        this.context = context;
        this.handler = handler;
    }

    @Override
    public Loader<ArrayList<Trailer>> onCreateLoader(int id, final Bundle args) {
        switch (id){
            case DetailsActivity.MOVIE_TRAILERS_LOADER:
                return new AsyncTaskLoader<ArrayList<Trailer>>(context) {

                    private ArrayList<Trailer> cache;
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
                    public ArrayList<Trailer> loadInBackground() {
                        try {
                            String response = NetworkUtil.getResponseFromHttpUrl(TheMovieDBUtil.getMovieTrailersUrl(movieId));
                            return Trailer.getTrailersFromJsonString(response, Trailer.YOUTUBE_TRAILER);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void deliverResult(ArrayList<Trailer> data) {
                        cache = data;
                        super.deliverResult(data);
                    }
                };
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> data) {
        handler.movieTrailersLoadFinished(loader, data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {}

    public interface LoaderFinishedHandler{
        void movieTrailersLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> data);
    }
}