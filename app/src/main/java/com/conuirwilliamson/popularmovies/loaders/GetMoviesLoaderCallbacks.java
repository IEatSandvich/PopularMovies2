package com.conuirwilliamson.popularmovies.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.conuirwilliamson.popularmovies.activities.DetailsActivity;
import com.conuirwilliamson.popularmovies.activities.MainActivity;
import com.conuirwilliamson.popularmovies.R;
import com.conuirwilliamson.popularmovies.models.Movie;
import com.conuirwilliamson.popularmovies.utilities.DatabaseUtil;
import com.conuirwilliamson.popularmovies.utilities.NetworkUtil;
import com.conuirwilliamson.popularmovies.utilities.TheMovieDBUtil;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class GetMoviesLoaderCallbacks implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    private final Context context;
    private final LoaderFinishedHandler handler;

    public GetMoviesLoaderCallbacks(@NonNull Context context, @NonNull LoaderFinishedHandler handler){
        this.context = context;
        this.handler = handler;
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(final int id, final Bundle args) {
        switch(id){
            case MainActivity.FAVORITED_MOVIES_LOADER:
                return new AsyncTaskLoader<ArrayList<Movie>>(context) {
                    private ArrayList<Movie> cache;

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
                    public ArrayList<Movie> loadInBackground() {
                        return Movie.getFavoritedMoviesFromCursor(DatabaseUtil.getFavoritedMovies(context));
                    }

                    @Override
                    public void deliverResult(ArrayList<Movie> data) {
                        cache = data;
                        super.deliverResult(data);
                    }
                };

            case MainActivity.MOST_POPULAR_MOVIES_LOADER:
                return new AsyncTaskLoader<ArrayList<Movie>>(context) {

                    private ArrayList<Movie> cache;

                    @Override
                    public void onStartLoading(){
                        if(cache == null) {
                            forceLoad();
                        } else {
                            deliverResult(cache);
                        }
                    }

                    @Override
                    public ArrayList<Movie> loadInBackground() {
                        try {
                            String response = NetworkUtil.getResponseFromHttpUrl(TheMovieDBUtil.getMostPopularUrl());
                            return Movie.getMoviesFromJSONString(getContext(), response);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void deliverResult(ArrayList<Movie> data) {
                        cache = data;
                        super.deliverResult(data);
                    }
                };
            case MainActivity.TOP_RATED_MOVIES_LOADER:
                return new AsyncTaskLoader<ArrayList<Movie>>(context) {

                    private ArrayList<Movie> cache;

                    @Override
                    public void onStartLoading(){
                        if(cache == null) {
                            forceLoad();
                        } else {
                            deliverResult(cache);
                        }
                    }

                    @Override
                    public ArrayList<Movie> loadInBackground() {
                        try {
                            String response = NetworkUtil.getResponseFromHttpUrl(TheMovieDBUtil.getTopRatedUrl());
                            return Movie.getMoviesFromJSONString(getContext(), response);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void deliverResult(ArrayList<Movie> data) {
                        cache = data;
                        super.deliverResult(data);
                    }
                };
            case DetailsActivity.MOVIE_DETAILS_LOADER:
                return new AsyncTaskLoader<ArrayList<Movie>>(context) {

                    private ArrayList<Movie> cache;
                    private int movieId = -2;

                    @Override
                    protected void onStartLoading() {
                        int currentMovieId;
                        if(cache == null &&
                                args != null &&
                                movieId != (currentMovieId = args.getInt(context.getString(R.string.bundle_movie_id), -1)) &&
                                currentMovieId != -1){
                            movieId = currentMovieId;
                            forceLoad();
                        } else {
                            deliverResult(cache);
                        }
                    }

                    @Override
                    public ArrayList<Movie> loadInBackground() {
                        try {
                            ArrayList<Movie> result = new ArrayList<>(1);
                            result.add(Movie.getMovieFromJSONString(context, NetworkUtil.getResponseFromHttpUrl(TheMovieDBUtil.getMovieUrl(movieId))));
                            return result;
                        } catch (JSONException | IOException e){
                            // Errors handled in DetailActivity.movieDetailsLoadFinished()
                        }
                        return null;
                    }

                    @Override
                    public void deliverResult(ArrayList<Movie> data) {
                        cache = data;
                        super.deliverResult(data);
                    }
                };
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        handler.moviesLoaderFinished(loader, data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {}

    public interface LoaderFinishedHandler{
        void moviesLoaderFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data);
    }
}
