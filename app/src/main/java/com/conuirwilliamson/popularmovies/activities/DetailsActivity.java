package com.conuirwilliamson.popularmovies.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.conuirwilliamson.popularmovies.R;
import com.conuirwilliamson.popularmovies.adapters.MovieReviewsAdapter;
import com.conuirwilliamson.popularmovies.adapters.MovieTrailersAdapter;
import com.conuirwilliamson.popularmovies.data.FavouriteMoviesContract;
import com.conuirwilliamson.popularmovies.loaders.AddFavoritedMovieLoaderCallbacks;
import com.conuirwilliamson.popularmovies.loaders.GetFavoritedMovieLoaderCallbacks;
import com.conuirwilliamson.popularmovies.loaders.GetMoviesLoaderCallbacks;
import com.conuirwilliamson.popularmovies.loaders.MovieReviewsLoaderCallbacks;
import com.conuirwilliamson.popularmovies.loaders.MovieTrailersLoaderCallbacks;
import com.conuirwilliamson.popularmovies.loaders.UpdateFavoritedMovieLoaderCallbacks;
import com.conuirwilliamson.popularmovies.models.Movie;
import com.conuirwilliamson.popularmovies.models.Review;
import com.conuirwilliamson.popularmovies.models.Trailer;
import com.conuirwilliamson.popularmovies.utilities.TheMovieDBUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements
        GetMoviesLoaderCallbacks.LoaderFinishedHandler,
        MovieTrailersLoaderCallbacks.LoaderFinishedHandler,
        MovieReviewsLoaderCallbacks.LoaderFinishedHandler,
        GetFavoritedMovieLoaderCallbacks.LoaderFinishedHandler,
        AddFavoritedMovieLoaderCallbacks.LoaderFinishedHandler,
        UpdateFavoritedMovieLoaderCallbacks.LoaderFinishedHandler,
        MovieTrailersAdapter.TrailersAdapterOnClickHandler{


    private int movieId;

    private MenuItem favoriteMenuItem;

    private boolean favoriteMovieEntryExists    = false;
    private boolean movieIsFavorited            = false;
    private boolean initialFavoritedState       = false;

    private boolean shownOverview;
    private boolean loadedPoster;
    private boolean loadedBackdrop;

    public static final int MOVIE_DETAILS_LOADER                = 5004;
    public static final int MOVIE_TRAILERS_LOADER               = 5005;
    public static final int MOVIE_REVIEWS_LOADER                = 5006;
    public static final int FAVORITED_MOVIE_LOADER              = 5007;
    public static final int UPDATE_MOVIE_IS_FAVORITED_LOADER    = 5008;
    public static final int UPDATE_MOVIE_POSTER_LOADER          = 5009;
    public static final int ADD_FAVORITED_MOVIE_LOADER          = 5010;

    private GetMoviesLoaderCallbacks            movieDetailsLoaderCallbacks;
    private MovieTrailersLoaderCallbacks        movieTrailersLoaderCallbacks;
    private MovieReviewsLoaderCallbacks         movieReviewsLoaderCallbacks;
    private GetFavoritedMovieLoaderCallbacks    getFavoritedMovieLoaderCallbacks;
    private AddFavoritedMovieLoaderCallbacks    addFavoritedMovieLoaderCallbacks;
    private UpdateFavoritedMovieLoaderCallbacks updateFavoritedMovieLoaderCallbacks;

    private MovieReviewsAdapter     reviewsAdapter;
    private MovieTrailersAdapter    trailersAdapter;

    private Movie movie;

    @BindView(R.id.layout_root)                 FrameLayout flLayoutRoot;

    @BindView(R.id.cl_movie_synopsis)           ConstraintLayout clMovieSynopsis;
    @BindView(R.id.pb_loading_movie_details)    ProgressBar pbLoadingDetails;
    @BindView(R.id.tv_error_msg)                TextView tvErrorMovieDetails;

    @BindView(R.id.rv_movie_trailers)           RecyclerView rvTrailers;
    @BindView(R.id.rv_movie_reviews)            RecyclerView rvReviews;

    @BindView(R.id.tv_trailers_error)           TextView tvErrorTrailers;
    @BindView(R.id.pb_loading_trailers)         ProgressBar pbLoadingTrailers;

    @BindView(R.id.tv_reviews_error)            TextView tvErrorReviews;
    @BindView(R.id.pb_loading_reviews)          ProgressBar pbLoadingReviews;

    @BindView(R.id.iv_movie_backdrop)           ImageView ivMovieBackdrop;
    @BindView(R.id.iv_movie_poster)             ImageView ivMoviePoster;
    @BindView(R.id.tv_movie_title)              TextView tvMovieTitle;
    @BindView(R.id.tv_movie_release)            TextView tvMovieRelease;
    @BindView(R.id.tv_movie_length)             TextView tvMovieLength;
    @BindView(R.id.tv_movie_rating)             TextView tvMovieRating;
    @BindView(R.id.tv_movie_overview)           TextView tvMovieOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        String movieIdKey = getResources().getString(R.string.intent_movie_id);
        Intent intent = getIntent();

        if (!intent.hasExtra(movieIdKey) || (movieId = intent.getIntExtra(movieIdKey, -1)) == -1) {
            setResult(RESULT_CANCELED);
            NavUtils.navigateUpFromSameTask(this);
            return;
        }

        reviewsAdapter = new MovieReviewsAdapter();
        rvReviews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvReviews.setAdapter(reviewsAdapter);

        trailersAdapter = new MovieTrailersAdapter(this);
        rvTrailers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvTrailers.setAdapter(trailersAdapter);

        movieDetailsLoaderCallbacks = new GetMoviesLoaderCallbacks(this, this);
        movieTrailersLoaderCallbacks = new MovieTrailersLoaderCallbacks(this, this);
        movieReviewsLoaderCallbacks = new MovieReviewsLoaderCallbacks(this, this);
        getFavoritedMovieLoaderCallbacks = new GetFavoritedMovieLoaderCallbacks(this, this);
        addFavoritedMovieLoaderCallbacks = new AddFavoritedMovieLoaderCallbacks(this, this);
        updateFavoritedMovieLoaderCallbacks = new UpdateFavoritedMovieLoaderCallbacks(this, this);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        showLoading();
        Bundle queryBundle = new Bundle();
        queryBundle.putInt(getString(R.string.bundle_movie_id), movieId);
        getSupportLoaderManager().restartLoader(MOVIE_DETAILS_LOADER, queryBundle, movieDetailsLoaderCallbacks);
        getSupportLoaderManager().restartLoader(MOVIE_REVIEWS_LOADER, queryBundle, movieReviewsLoaderCallbacks);
        getSupportLoaderManager().restartLoader(MOVIE_TRAILERS_LOADER, queryBundle, movieTrailersLoaderCallbacks);
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);

        favoriteMenuItem = menu.findItem(R.id.action_favorite);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_favorite:
                item.setEnabled(false);
                Bundle queryBundle = new Bundle();
                if (favoriteMovieEntryExists) {
                    queryBundle.putInt(getString(R.string.bundle_movie_id), movieId);
                    queryBundle.putBoolean(getString(R.string.bundle_movie_is_favorited), !movieIsFavorited);
                    getSupportLoaderManager().restartLoader(UPDATE_MOVIE_IS_FAVORITED_LOADER, queryBundle, updateFavoritedMovieLoaderCallbacks);
                } else {
                    queryBundle.putParcelable(getString(R.string.bundle_movie), movie);
                    getSupportLoaderManager().restartLoader(ADD_FAVORITED_MOVIE_LOADER, queryBundle, addFavoritedMovieLoaderCallbacks);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResultWithIntent(RESULT_OK);
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public void movieReviewsLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> reviews) {
        if (reviews == null) {
            showNoReviews(getString(R.string.error_reviews_null));
            return;
        }
        if (reviews.size() == 0) {
            showNoReviews(getString(R.string.msg_no_reviews_found));
            return;
        }
        reviewsAdapter.setData(reviews);
        showReviews();
    }

    @Override
    public void movieTrailersLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> trailers) {
        if (trailers == null) {
            showNoTrailers(getString(R.string.error_trailers_null));
            return;
        }
        if (trailers.size() == 0) {
            showNoTrailers(getString(R.string.msg_no_trailers_found));
            return;
        }
        trailersAdapter.setData(trailers);
        showTrailers();
    }

    @Override
    public void moviesLoaderFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> movies) {
        if(movies == null || movies.size() == 0) return;

        Movie movie = movies.get(0);
        if (movie != null) {
            this.movie = movie;
            Bundle queryBundle = new Bundle();
            queryBundle.putInt(getString(R.string.bundle_movie_id), movieId);
            getSupportLoaderManager().restartLoader(FAVORITED_MOVIE_LOADER, queryBundle, getFavoritedMovieLoaderCallbacks);
            displayMovie(movie);
        } else {
            int statusCode;
            switch (statusCode = Movie.getStatusCode()) {
                case TheMovieDBUtil.INVALID_API_KEY:
                case TheMovieDBUtil.RESOURCE_NOT_FOUND:
                    setResultWithIntent(statusCode);
                    NavUtils.navigateUpFromSameTask(this);
                    return;
                default:
                    showMessage(R.string.error_loading_movie_details);
            }
        }
    }

    @Override
    public void getFavoritedMovieLoaderFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                favoriteMovieEntryExists = true;
                initialFavoritedState = movieIsFavorited = cursor.getInt(cursor.getColumnIndex(FavouriteMoviesContract.FavouriteMoviesTable.COLUMN_FAVORITED)) > 0;
                String moviePoster;
                if (!(moviePoster = movie.getPosterPath()).equals(cursor.getString(cursor.getColumnIndex(FavouriteMoviesContract.FavouriteMoviesTable.COLUMN_MOVIE_POSTER)))) {
                    Bundle queryBundle = new Bundle();
                    queryBundle.putInt(getString(R.string.bundle_movie_id), movieId);
                    queryBundle.putString(getString(R.string.bundle_movie_poster), moviePoster);
                    getSupportLoaderManager().restartLoader(UPDATE_MOVIE_POSTER_LOADER, queryBundle, updateFavoritedMovieLoaderCallbacks);
                }
            }
            cursor.close();
        }
        updateFavoriteMenuItem(favoriteMenuItem, movieIsFavorited);
        favoriteMenuItem.setEnabled(true);
        favoriteMenuItem.setVisible(true);
    }

    @Override
    public void addFavoritedMovieLoaderFinished(Loader<Long> loader, Long rowId) {
        if (rowId == movieId) {
            updateFavoriteMenuItem(favoriteMenuItem, movieIsFavorited = !movieIsFavorited);
            favoriteMovieEntryExists = true;
        } else {
            Snackbar.make(flLayoutRoot, R.string.error_add_favorite_movie_db, Snackbar.LENGTH_LONG).show();
        }
        favoriteMenuItem.setEnabled(true);
    }

    @Override
    public void updateFavoritedMovieLoadFinished(Loader<Integer> loader, int rowsAffected) {
        favoriteMenuItem.setEnabled(true);
        if (rowsAffected > 0) {
            updateFavoriteMenuItem(favoriteMenuItem, movieIsFavorited = !movieIsFavorited);
            return;
        }
        Snackbar.make(flLayoutRoot, R.string.error_update_favorite_movie_db, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void updateFavoritedMoviePosterLoadFinished(Loader<Integer> loader, int rowsAffected) {
        if (rowsAffected > 0) return;
        Snackbar.make(flLayoutRoot, R.string.error_update_favorite_movie_poster_db, Snackbar.LENGTH_LONG).show();
    }

    private void displayMovie(Movie movie) {
        resetLoadingFlags();
        setTitle(movie.getTitle());

        Picasso.with(this)
                .load(TheMovieDBUtil.getImageUrl(this, movie.getBackdropPath(), R.string.w_780).toString())
                .placeholder(R.drawable.ic_backdrop_placeholder)
                .into(ivMovieBackdrop, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        loadedBackdrop = true;
                        if (isDoneLoading()) { hideView(pbLoadingDetails); }
                    }

                    @Override
                    public void onError() {
                        loadedBackdrop = true;
                        Snackbar.make(flLayoutRoot, R.string.error_loading_backdrop, Snackbar.LENGTH_LONG).show();
                        if (isDoneLoading()) { hideView(pbLoadingDetails); }
                    }
                });

        Picasso.with(this)
                .load(TheMovieDBUtil.getImageUrl(this, movie.getPosterPath(), R.string.w_342).toString())
                .placeholder(R.drawable.ic_poster_placeholder)
                .into(ivMoviePoster, new Callback.EmptyCallback() {

                    @Override
                    public void onSuccess() {
                        loadedPoster = true;
                        if (isDoneLoading()) { hideView(pbLoadingDetails); }
                    }

                    @Override
                    public void onError() {
                        loadedPoster = true;
                        Snackbar.make(flLayoutRoot, R.string.error_loading_poster, Snackbar.LENGTH_LONG).show();
                        if (isDoneLoading()) { hideView(pbLoadingDetails); }
                    }
        });

        tvMovieTitle.setText(movie.getTitle());
        tvMovieRelease.setText(movie.getReleaseDate());
        tvMovieLength.setText(movie.getRuntime());
        tvMovieRating.setText(movie.getVoteAverage());
        tvMovieOverview.setText(movie.getOverview());
        showContent();
    }

    private void showReviews(){
        goneView(tvErrorReviews);
        goneView(pbLoadingReviews);
        showView(rvReviews);
    }

    private void showTrailers(){
        goneView(tvErrorTrailers);
        goneView(pbLoadingTrailers);
        showView(rvTrailers);
    }

    private void showNoReviews(String message){
        goneView(rvReviews);
        goneView(pbLoadingReviews);
        tvErrorReviews.setText(message);
        showView(tvErrorReviews);
    }

    private void showNoTrailers(String message){
        goneView(rvTrailers);
        goneView(pbLoadingTrailers);
        tvErrorTrailers.setText(message);
        showView(tvErrorTrailers);
    }

    private void setResultWithIntent(int statusCode) {
        switch (statusCode) {
            case RESULT_OK:
                Intent intent = new Intent();
                intent.putExtra(getString(R.string.intent_favorited_changed), initialFavoritedState != movieIsFavorited);
                setResult(statusCode, intent);
                break;
            case TheMovieDBUtil.INVALID_API_KEY:
            case TheMovieDBUtil.RESOURCE_NOT_FOUND:
                setResult(statusCode);
                break;
        }
    }

    private void updateFavoriteMenuItem(MenuItem item, boolean isFavorited) {
        if(isFavorited){
            item.setIcon(R.drawable.ic_favorite_pink_500);
            item.setTitle(R.string.mi_title_unfavorite);
        } else {
            item.setIcon(R.drawable.ic_not_favourite_white);
            item.setTitle(R.string.mi_title_favorite);
        }
    }

    private boolean isDoneLoading() {return shownOverview && loadedPoster && loadedBackdrop;}
    private void resetLoadingFlags() {shownOverview = loadedPoster = loadedBackdrop = false;}
    private void hideView(View view) {view.setVisibility(View.INVISIBLE);}
    private void showView(View view) {view.setVisibility(View.VISIBLE);}
    private void goneView(View view) {view.setVisibility(View.GONE);}

    @Override
    public void handleClick(String url) {
        Uri trailerUri = Uri.parse(url);
        Intent launchTrailerIntent = new Intent(Intent.ACTION_VIEW, trailerUri);
        startActivity(launchTrailerIntent);
    }

    private void showLoading() {
        hideView(clMovieSynopsis);
        hideView(tvErrorMovieDetails);
        showView(pbLoadingDetails);
    }

    private void showContent() {
        shownOverview = true;
        hideView(tvErrorMovieDetails);
        if (isDoneLoading()) { hideView(pbLoadingDetails); }
        showView(clMovieSynopsis);
    }

    private void showMessage(int messageId) {
        hideView(clMovieSynopsis);
        hideView(pbLoadingDetails);
        showView(tvErrorMovieDetails);
        tvErrorMovieDetails.setText(getResources().getString(messageId));
    }
}
