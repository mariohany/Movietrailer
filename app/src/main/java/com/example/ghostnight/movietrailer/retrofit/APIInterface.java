package com.example.ghostnight.movietrailer.retrofit;

import com.example.ghostnight.movietrailer.model.MovieVideoResponseModel;
import com.example.ghostnight.movietrailer.model.MoviesPageResbonseModel;

import io.reactivex.Single;
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
    Single<MoviesPageResbonseModel> searchMovie(
            @Query("api_key") String key,
            @Query("language") String language,
            @Query("query") String query,
            @Query("page") int page);
}
