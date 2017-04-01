package com.conuirwilliamson.popularmovies.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.conuirwilliamson.popularmovies.R;
import com.conuirwilliamson.popularmovies.adapters.MoviesAdapter;
import com.conuirwilliamson.popularmovies.loaders.GetMoviesLoaderCallbacks;
import com.conuirwilliamson.popularmovies.models.Movie;
import com.conuirwilliamson.popularmovies.utilities.SharedPrefsUtil;
import com.conuirwilliamson.popularmovies.utilities.TheMovieDBUtil;
import com.conuirwilliamson.popularmovies.utilities.UIUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements
        MoviesAdapter.MoviesAdapterOnClickHandler,
        GetMoviesLoaderCallbacks.LoaderFinishedHandler{

    public enum SearchType{
        MostPopular,
        TopRated,
        Favorited
    }

    private SearchType lastSearch;

    private boolean requireFavoritedRefresh = false;

    public static final int MOST_POPULAR_MOVIES_LOADER = 5001;
    public static final int TOP_RATED_MOVIES_LOADER = 5002;
    public static final int FAVORITED_MOVIES_LOADER = 5003;

    public static final int REQUEST_IS_FAVORITE_UPDATED = 221;

    @BindView(R.id.layout_root)             ConstraintLayout clLayoutroot;
    @BindView(R.id.rv_movies)               RecyclerView rvMovies;
    @BindView(R.id.tv_error_msg)            TextView tvErrorMsg;
    @BindView(R.id.pb_loading_movies)       ProgressBar pbLoadingMovies;
    @BindView(R.id.btn_no_fav_most_pop)     Button btnFavToMostPopular;
    @BindView(R.id.btn_no_fav_top_rated)    Button btnFavToTopRated;

    private MenuItem miCancel;
    private MenuItem miSort;

    private GetMoviesLoaderCallbacks moviesLoaderCallbacks;

    private MoviesAdapter moviesAdapter;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManagerFourCol;
    private GridLayoutManager gridLayoutManagerTwoCol;
    private boolean isGridLayout = false;

    private int firstVisibleItemPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        moviesLoaderCallbacks = new GetMoviesLoaderCallbacks(this, this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        gridLayoutManagerFourCol = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        gridLayoutManagerTwoCol = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        rvMovies.setLayoutManager(getLayoutManager());
        rvMovies.setAdapter(moviesAdapter = new MoviesAdapter(this, this));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if(savedInstanceState != null) {
            // Get the last searched movies, or the preferred default if the value doesnt exist.
            SearchType search = SearchType.valueOf(savedInstanceState.getString(getString(R.string.intent_last_search), SharedPrefsUtil.getPreferredSearchType(this).name()));
            firstVisibleItemPosition = savedInstanceState.getInt(getString(R.string.intent_first_movie_position), -1);
            startLoadingMovies(lastSearch = search);
        } else {
            startLoadingMovies(lastSearch = SharedPrefsUtil.getPreferredSearchType(this));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case REQUEST_IS_FAVORITE_UPDATED:
                switch (resultCode){
                    case RESULT_OK:
                        if (requireFavoritedRefresh = requireFavoritedRefresh || data.getBooleanExtra(getString(R.string.intent_favorited_changed), false)) {
                            if(lastSearch == SearchType.Favorited) {
                                Bundle queryBundle = new Bundle();
                                queryBundle.putBoolean(getString(R.string.bundle_favorited_changed), requireFavoritedRefresh);
                                getSupportLoaderManager().restartLoader(FAVORITED_MOVIES_LOADER, queryBundle, moviesLoaderCallbacks);
                            }
                        }
                        break;
                    case TheMovieDBUtil.INVALID_API_KEY:
                        Snackbar.make(clLayoutroot, R.string.error_invalid_api_key, Snackbar.LENGTH_LONG).show();
                        break;
                    case TheMovieDBUtil.RESOURCE_NOT_FOUND:
                        Snackbar.make(clLayoutroot, R.string.error_resource_not_found, Snackbar.LENGTH_LONG).show();
                        break;
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        firstVisibleItemPosition = isGridLayout ?
                ((GridLayoutManager) rvMovies.getLayoutManager()).findFirstCompletelyVisibleItemPosition() :
                ((LinearLayoutManager) rvMovies.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

        if(firstVisibleItemPosition < 0 ) firstVisibleItemPosition = 0;
        outState.putString(getString(R.string.intent_last_search), lastSearch.name());
        outState.putInt(getString(R.string.intent_first_movie_position), firstVisibleItemPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.main_menu, menu);

            miCancel = menu.findItem(R.id.action_cancel);
            miSort = menu.findItem(R.id.action_sort_by);

            return true;
        } catch (InflateException e){
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_most_popular:
                showLoading();
                startLoadingMovies(lastSearch = SearchType.MostPopular);
                return true;
            case R.id.action_top_rated:
                showLoading();
                startLoadingMovies(lastSearch = SearchType.TopRated);
                return true;
            case R.id.action_favorited:
                showLoading();
                startLoadingMovies(lastSearch = SearchType.Favorited);
                requireFavoritedRefresh = false;
                return true;
            case R.id.action_cancel:
                cancelLoadingMovies();
                return true;
            case R.id.action_settings:
                showSettingsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startLoadingMovies(SearchType type, Bundle bundle){
        showCancel();
        updateTitle();
        switch(type){
            case MostPopular:
                getSupportLoaderManager().restartLoader(MOST_POPULAR_MOVIES_LOADER, bundle, moviesLoaderCallbacks);
                break;
            case TopRated:
                getSupportLoaderManager().restartLoader(TOP_RATED_MOVIES_LOADER, bundle, moviesLoaderCallbacks);
                break;
            case Favorited:
                getSupportLoaderManager().restartLoader(FAVORITED_MOVIES_LOADER, bundle, moviesLoaderCallbacks);
                break;
        }
    }
    private void startLoadingMovies(SearchType type){ startLoadingMovies(type, null); }

    private void cancelLoadingMovies(){
        showSort();
        showError(getString(R.string.msg_cancel_search));
        switch(lastSearch){
            case MostPopular:
                getSupportLoaderManager().getLoader(MOST_POPULAR_MOVIES_LOADER).cancelLoad();
                break;
            case TopRated:
                getSupportLoaderManager().getLoader(TOP_RATED_MOVIES_LOADER).cancelLoad();
                break;
            case Favorited:
                getSupportLoaderManager().getLoader(FAVORITED_MOVIES_LOADER).cancelLoad();
                break;
        }
    }

    private void showSettingsActivity() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    @OnClick(R.id.btn_no_fav_most_pop)
    public void favoritesToMostPopular(){
        showLoading();
        startLoadingMovies(lastSearch = SearchType.MostPopular);
    }

    @OnClick(R.id.btn_no_fav_top_rated)
    public void favoritesToTopRated(){
        showLoading();
        startLoadingMovies(lastSearch = SearchType.TopRated);
    }

    @Override
    public void moviesLoaderFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> movies) {

        // NOTE FOR REVIEWER:
        //
        // For some reason, when loading up movies, if you favorite/unfavorite a movie,
        // return to MainActivity, go back into a movie, favorite/unfavorite it, when you go back
        // upon returning to the MainActivity, if you had selected a movie from Most Popular or Top Rated,
        // it SHOULD display the appropriate set of movies, but for some reason, the FAVORITES_MOVIES_LOADER
        // loader is called, thus returning the favorited movies. I can't for the life of me figure out why this
        // is happening. If you can shed some light on this; maybe I'm implementing multiple loaders incorrectly,
        // I don't know; it would be much appreciated.

        // If loader.getId() doesn't match with current (or previously, if returning from DetailsActivity)
        // searched movies, return.

        int id = loader.getId();
        if(id == FAVORITED_MOVIES_LOADER && lastSearch != SearchType.Favorited ||
           id == MOST_POPULAR_MOVIES_LOADER && lastSearch != SearchType.MostPopular ||
           id == TOP_RATED_MOVIES_LOADER && lastSearch != SearchType.TopRated) return;

        showSort();

        if (movies != null) {
            showMovies(movies);
            if(lastSearch == SearchType.Favorited){ requireFavoritedRefresh = false; }
        } else {
            switch(Movie.getStatusCode()){
                case TheMovieDBUtil.INVALID_API_KEY:
                    showError(getString(R.string.error_invalid_api_key));
                    break;
                case TheMovieDBUtil.RESOURCE_NOT_FOUND:
                    showError(getString(R.string.error_resource_not_found));
                    break;
                default:
                    switch (lastSearch) {
                        case Favorited:
                            showError(getString(R.string.msg_no_favorited_movies));
                            break;
                        case MostPopular:
                        case TopRated:
                            showError(getString(R.string.error_loading_movies));
                            break;
                    }
            }
        }
    }

    @Override
    public void handleClick(int movieId) {
        firstVisibleItemPosition = isGridLayout ?
                ((GridLayoutManager) rvMovies.getLayoutManager()).findFirstCompletelyVisibleItemPosition() :
                ((LinearLayoutManager) rvMovies.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        if(firstVisibleItemPosition < 0) firstVisibleItemPosition = 0;

        Intent intent = new Intent(this, DetailsActivity.class).putExtra(getResources().getString(R.string.intent_movie_id), movieId);
        startActivityForResult(intent, REQUEST_IS_FAVORITE_UPDATED);
    }

    private void updateTitle(){
        switch (lastSearch) {
            case MostPopular:
                setTitle(R.string.most_popular);
                break;
            case TopRated:
                setTitle(R.string.top_rated);
                break;
            case Favorited:
                setTitle(R.string.favorited);
                break;
            default:
                setTitle(R.string.app_name);
        }
    }

    private RecyclerView.LayoutManager getLayoutManager(){
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        if(UIUtil.displayLinearLayoutMovies(metrics)) {
            isGridLayout = false;
            return linearLayoutManager;
        }
        isGridLayout = true;
        return UIUtil.isWiderThanIsTall(metrics) ?
                gridLayoutManagerFourCol :
                gridLayoutManagerTwoCol;
    }

    private void showLoading(){
        hideView(rvMovies);
        hideView(tvErrorMsg);
        hideView(btnFavToMostPopular);
        hideView(btnFavToTopRated);
        showView(pbLoadingMovies);
    }

    private void showError(String message){
        hideView(rvMovies);
        hideView(pbLoadingMovies);
        tvErrorMsg.setText(message);
        showView(tvErrorMsg);
        if(lastSearch == SearchType.Favorited){
            showView(btnFavToMostPopular);
            showView(btnFavToTopRated);
        }
    }

    private void showMovies(ArrayList<Movie> movies){
        hideView(tvErrorMsg);
        hideView(btnFavToMostPopular);
        hideView(btnFavToTopRated);
        hideView(pbLoadingMovies);
        moviesAdapter.setData(movies);
        if(firstVisibleItemPosition >= 0 && firstVisibleItemPosition < movies.size()){
            rvMovies.getLayoutManager().scrollToPosition(firstVisibleItemPosition);
            firstVisibleItemPosition = -1;
        }
        showView(rvMovies);
    }

    private void showCancel(){
        if(miSort == null || miCancel == null) return;
        miSort.setVisible(false);
        miCancel.setVisible(true);
    }

    private void showSort(){
        if(miSort == null || miCancel == null) return;
        miCancel.setVisible(false);
        miSort.setVisible(true);
    }

    private void showView(View view){ view.setVisibility(View.VISIBLE); }
    private void hideView(View view){ view.setVisibility(View.INVISIBLE); }
}

// TODO: 01/04/2017 - Priority 0 - Add in some 'onboarding', maybe, maybe not
// TODO: 01/04/2017 - Priority 0 - Maybe, on Long-click of a Movie, favorite the movie from here, in MainActivity, instead of having to go to DetailsActivity