package com.conuirwilliamson.popularmovies.apis;

import com.conuirwilliamson.popularmovies.models.Movie;
import com.conuirwilliamson.popularmovies.models.MoviesResponse;
import com.conuirwilliamson.popularmovies.models.ReviewsResponse;
import com.conuirwilliamson.popularmovies.models.TrailersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by conuirwilliamson on 11/04/2017.
 */

public interface TheMovieDBAPIService {

    @GET("movie/popular")
    Call<MoviesResponse> getMostPopularMovies(@Query("api_key") String api_key);

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String api_key);

    @GET("movie/{id}")
    Call<Movie> getMovieById(@Path("id") int movieId, @Query("api_key") String api_key);

    @GET("movie/{id}/reviews")
    Call<ReviewsResponse> getReviewsByMovieId(@Path("id") int movieId, @Query("api_key") String api_key);

    @GET("movie/{id}/trailers")
    Call<TrailersResponse> getTrailersByMovieId(@Path("id") int movieId, @Query("api_key") String api_key);

}
