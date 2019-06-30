package com.example.ghostnight.movietrailer.retrofit;

import com.example.ghostnight.movietrailer.retrofit.retrofit_models.MovieVideoResponseModel;
import com.example.ghostnight.movietrailer.retrofit.retrofit_models.MoviesPageResbonseModel;
import com.example.ghostnight.movietrailer.retrofit.retrofit_models.SearchMoviesPageResbonseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("discover/movie")
    Call<MoviesPageResbonseModel> getMovies(
            @Query("api_key") String key,
            @Query("language") String language,
            @Query("sort_by") String sort,
            @Query("page") int page);

    @GET("movie/{movie_id}/videos")
    Call<MovieVideoResponseModel> getMovieVideo(
            @Path("movie_id") int id,
            @Query("api_key") String key,
            @Query("language") String language);

    @GET("search/movie")
    Call<SearchMoviesPageResbonseModel> searchMovie(
            @Query("api_key") String key,
            @Query("language") String language,
            @Query("query") String query,
            @Query("page") int page);
}
