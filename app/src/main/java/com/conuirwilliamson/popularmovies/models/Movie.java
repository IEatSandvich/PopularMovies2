package com.conuirwilliamson.popularmovies.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.conuirwilliamson.popularmovies.data.FavouriteMoviesContract;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class Movie extends TheMovieDBResponse implements Parcelable{

    @Expose
    private String poster_path;
    public String getPosterPath(){ return poster_path; }

    @Expose
    private String overview;
    public String getOverview() { return overview; }

    @Expose
    private String release_date;
    public String getReleaseDate() { return release_date; }

    @Expose
    private int id;
    public int getID() { return id; }

    @Expose
    private String title;
    public String getTitle() { return title; }

    @Expose
    private String backdrop_path;
    public String getBackdropPath() { return backdrop_path; }

    @Expose
    private String vote_average;
    public String getVoteAverage() { return vote_average; }

    @Expose
    private String runtime;
    public String getRuntime() { return runtime; }

    @Expose
    private boolean adult;
    public boolean getAdult() { return adult; }

    @Expose
    private ArrayList<Integer> genre_ids;
    public ArrayList<Integer> getGenreIds() { return genre_ids; }

    @Expose
    private String original_title;
    public String getOriginalTitle() { return original_title; }

    @Expose
    private String original_language;
    public String getOriginalLanguage() { return original_language; }

    @Expose
    private double popularity;
    public double getPopularity() { return popularity; }

    @Expose
    private int vote_count;
    public int getVoteCount() { return vote_count; }

    @Expose
    private boolean video;
    public boolean getVideo() { return video; }

    @Expose
    private String imdb_id;
    public String getImdbId() { return imdb_id; }

    @Expose
    private String homepage;
    public String getHomepage() { return homepage; }

    @Expose
    private int budget;
    public int getBudget() { return budget; }

    public Movie() { genre_ids = new ArrayList<>(); }

    public static final Creator<Movie> CREATOR
            = new Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in){
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        id = in.readInt();
        title = in.readString();
        backdrop_path = in.readString();
        vote_average = in.readString();
        runtime = in.readString();
        adult = in.readInt() == 1;
        genre_ids = in.readArrayList(Integer.class.getClassLoader());
        original_title = in.readString();
        original_language = in.readString();
        popularity = in.readDouble();
        vote_count = in.readInt();
        video = in.readInt() == 1;
        imdb_id = in.readString();
        homepage = in.readString();
        budget = in.readInt();
    }

    public static ArrayList<Movie> getFavoritedMoviesFromCursor(@NonNull Cursor cursor) {
        if(cursor.getCount() == 0) return null;

        ArrayList<Movie> result = new ArrayList<>(cursor.getCount());

        int titleColumn = cursor.getColumnIndex(FavouriteMoviesContract.FavouriteMoviesTable.COLUMN_MOVIE_TITLE);
        int posterColumn = cursor.getColumnIndex(FavouriteMoviesContract.FavouriteMoviesTable.COLUMN_MOVIE_POSTER);
        int idColumn = cursor.getColumnIndex(FavouriteMoviesContract.FavouriteMoviesTable._ID);

        while(cursor.moveToNext()){
            Movie movie = new Movie();
            movie.poster_path = cursor.getString(posterColumn);
            movie.title = cursor.getString(titleColumn);
            movie.id = cursor.getInt(idColumn);
            result.add(movie);
        }

        cursor.close();

        return result;
    }

    @Override
    public String toString() { return getTitle(); }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(backdrop_path);
        dest.writeString(vote_average);
        dest.writeString(runtime);
        dest.writeInt(adult ? 1 : 0);
        dest.writeList(genre_ids);
        dest.writeString(original_title);
        dest.writeString(original_language);
        dest.writeDouble(popularity);
        dest.writeInt(vote_count);
        dest.writeInt(video ? 1 : 0);
        dest.writeString(imdb_id);
        dest.writeString(homepage);
        dest.writeInt(budget);
    }
}
