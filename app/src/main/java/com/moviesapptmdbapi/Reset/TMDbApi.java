package com.moviesapptmdbapi.Reset;

import com.moviesapptmdbapi.Model.GenresResponse;
import com.moviesapptmdbapi.Model.Movie;
import com.moviesapptmdbapi.Model.MoviesResponse;
import com.moviesapptmdbapi.Model.ReviewResponse;
import com.moviesapptmdbapi.Model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDbApi {

//    OBTENER Películas populares
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );
//  OBTENGA la mejor calificación de la película
    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRateMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );
//  OBTENER Película próxima
    @GET("movie/upcoming")
    Call<MoviesResponse> getUpcomingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );
//  OBTENER Lista de películas de género
    @GET("genre/movie/list")
    Call<GenresResponse> getGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );
//  OBTENER una película por ID
    @GET("movie/{movie_id}")
    Call<Movie> getMovie(
            @Path("movie_id") int id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );
//  OBTENER videos de una película
    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getTrailers(
            @Path("movie_id") int id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );
//  OBTENER Reseñas de una película
    @GET("movie/{movie_id}/reviews")
    Call<ReviewResponse> getReviews(
            @Path("movie_id") int id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );
}

