package com.conuirwilliamson.popularmovies.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.conuirwilliamson.popularmovies.BuildConfig;
import com.conuirwilliamson.popularmovies.activities.DetailsActivity;
import com.conuirwilliamson.popularmovies.activities.MainActivity;
import com.conuirwilliamson.popularmovies.R;
import com.conuirwilliamson.popularmovies.apis.TheMovieDBAPIService;
import com.conuirwilliamson.popularmovies.models.Movie;
import com.conuirwilliamson.popularmovies.models.MoviesResponse;
import com.conuirwilliamson.popularmovies.models.ReviewsResponse;
import com.conuirwilliamson.popularmovies.models.TheMovieDBResponse;
import com.conuirwilliamson.popularmovies.utilities.DatabaseUtil;
import com.conuirwilliamson.popularmovies.utilities.NetworkUtil;
import com.conuirwilliamson.popularmovies.utilities.TheMovieDBUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Response;

public class GetMoviesLoaderCallbacks implements LoaderManager.LoaderCallbacks<MoviesResponse> {

    private final Context context;
    private final LoaderFinishedHandler handler;

    public GetMoviesLoaderCallbacks(@NonNull Context context, @NonNull LoaderFinishedHandler handler){
        this.context = context;
        this.handler = handler;
    }

    @Override
    public Loader<MoviesResponse> onCreateLoader(final int id, final Bundle args) {
        switch(id){
            /*case MainActivity.FAVORITED_MOVIES_LOADER:
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
                };*/

            case MainActivity.MOST_POPULAR_MOVIES_LOADER:
                return new AsyncTaskLoader<MoviesResponse>(context) {

                    private MoviesResponse cache;

                    @Override
                    public void onStartLoading(){
                        if(cache == null || cache.getResponseType() == TheMovieDBResponse.ERROR) {
                            forceLoad();
                        } else {
                            deliverResult(cache);
                        }
                    }

                    @Override
                    public MoviesResponse loadInBackground() {
                        try {
                            Response response = TheMovieDBUtil.getAPIService().getMostPopularMovies(BuildConfig.TMDBAK).execute();
                            MoviesResponse moviesResponse = (MoviesResponse) response.body();
                            if(moviesResponse != null) return cache = moviesResponse;

                            cache = new MoviesResponse();
                            cache.setResponseType(TheMovieDBResponse.ERROR);
                            return cache = new GsonBuilder().create().fromJson(response.errorBody().string(), MoviesResponse.class);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void deliverResult(MoviesResponse data) {
                        super.deliverResult(data);
                    }
                };
            case MainActivity.TOP_RATED_MOVIES_LOADER:
                return new AsyncTaskLoader<MoviesResponse>(context) {

                    private MoviesResponse cache;

                    @Override
                    public void onStartLoading(){
                        if(cache == null || cache.getResponseType() == TheMovieDBResponse.ERROR) {
                            forceLoad();
                        } else {
                            deliverResult(cache);
                        }
                    }

                    @Override
                    public MoviesResponse loadInBackground() {
                        try {
                            Response response = TheMovieDBUtil.getAPIService().getTopRatedMovies(BuildConfig.TMDBAK).execute();
                            MoviesResponse moviesResponse = (MoviesResponse) response.body();
                            if(moviesResponse != null) return cache = moviesResponse;

                            cache = new MoviesResponse();
                            cache.setResponseType(TheMovieDBResponse.ERROR);
                            return cache = new GsonBuilder().create().fromJson(response.errorBody().string(), MoviesResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void deliverResult(MoviesResponse data) {
                        super.deliverResult(data);
                    }
                };
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<MoviesResponse> loader, MoviesResponse data) {
        handler.moviesLoaderFinished(loader, data);
    }

    @Override
    public void onLoaderReset(Loader<MoviesResponse> loader) {}

    public interface LoaderFinishedHandler{
        void moviesLoaderFinished(Loader<MoviesResponse> loader, MoviesResponse data);
    }
}
