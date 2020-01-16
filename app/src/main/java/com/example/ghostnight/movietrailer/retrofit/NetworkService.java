package com.example.ghostnight.movietrailer.retrofit;

import com.example.ghostnight.movietrailer.model.MovieVideoResponseModel;
import com.example.ghostnight.movietrailer.model.MoviesPageResbonseModel;

import io.reactivex.Single;

public class NetworkService {

    private static final String API_KEY = "2f5aec71051329a23ceff81275e0be83";
    private static final String SORT = "popularity.desc";
    private static final String LANGUAGE = "EN";

    private static NetworkService INSTANCE = null;

    private NetworkService(){}

    public static NetworkService getInstance() {
        if (INSTANCE == null)
            INSTANCE = new NetworkService();
        return INSTANCE;
    }

    public Single<MoviesPageResbonseModel> getMovies(int page) {
        RestClient restClient = new RestClient();
        APIInterface apiInterface = restClient.createService();
        Single<MoviesPageResbonseModel> call = apiInterface.getMovies(API_KEY, LANGUAGE, SORT, page);
        return call;
    }

    public Single<MovieVideoResponseModel> getMovieVideo(int id) {
        RestClient restClient = new RestClient();
        APIInterface apiInterface = restClient.createService();
        Single<MovieVideoResponseModel> call = apiInterface.getMovieVideo(id, API_KEY, LANGUAGE);
        return call;
    }

    public Single<MoviesPageResbonseModel> searchMovie(int page, String query) {
        RestClient restClient = new RestClient();
        APIInterface apiInterface = restClient.createService();
        Single<MoviesPageResbonseModel> call = apiInterface.searchMovie(API_KEY, LANGUAGE, query, page);
        return call;
    }
}
