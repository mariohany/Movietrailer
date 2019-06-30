package com.example.ghostnight.movietrailer.retrofit;

import com.example.ghostnight.movietrailer.BuildConfig;
import com.example.ghostnight.movietrailer.retrofit.retrofit_models.MovieVideoResponseModel;
import com.example.ghostnight.movietrailer.retrofit.retrofit_models.MoviesPageResbonseModel;
import com.example.ghostnight.movietrailer.retrofit.retrofit_models.SearchMoviesPageResbonseModel;
import com.example.ghostnight.movietrailer.retrofit.retrofit_response.MoviesResbonse;
import com.example.ghostnight.movietrailer.retrofit.retrofit_response.MoviesVideoResbonse;
import com.example.ghostnight.movietrailer.retrofit.retrofit_response.SearchMoviesResbonse;

import retrofit2.Call;

public class NetworkService {

    private static final String API_KEY = BuildConfig.ApiKey;
    private static final String SORT = "popularity.desc";
    private static final String LANGUAGE = "EN";

    private static NetworkService INSTANCE = null;

    private NetworkService(){}

    public static NetworkService getInstance() {
        if (INSTANCE == null)
            INSTANCE = new NetworkService();
        return INSTANCE;
    }

    public void getMovies(int page, MoviesResbonse.MoviesResbonseListener listener){
        RestClient restClient = new RestClient();
        APIInterface apiInterface = restClient.createService();
        Call<MoviesPageResbonseModel> call = apiInterface.getMovies(API_KEY, LANGUAGE, SORT, page);
        call.enqueue(new MoviesResbonse(listener));
    }

    public void getMovieVideo(int id,MoviesVideoResbonse.MoviesVideoResbonseListener listener){
        RestClient restClient = new RestClient();
        APIInterface apiInterface = restClient.createService();
        Call<MovieVideoResponseModel> call = apiInterface.getMovieVideo(id, API_KEY, LANGUAGE);
        call.enqueue(new MoviesVideoResbonse(listener));
    }

    public void searchMovie(int page, String query, SearchMoviesResbonse.MoviesResbonseListener listener){
        RestClient restClient = new RestClient();
        APIInterface apiInterface = restClient.createService();
        Call<SearchMoviesPageResbonseModel> call = apiInterface.searchMovie(API_KEY, LANGUAGE, query, page);
        call.enqueue(new SearchMoviesResbonse(listener));
    }
}
