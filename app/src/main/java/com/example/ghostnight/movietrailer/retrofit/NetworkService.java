package com.example.ghostnight.movietrailer.retrofit;

import com.example.ghostnight.movietrailer.model.MovieVideoResponseModel;
import com.example.ghostnight.movietrailer.model.MoviesPageResbonseModel;

import io.reactivex.Single;
import retrofit2.Call;

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

    public void getMovies(int page, BaseResponseListener listener){
        RestClient restClient = new RestClient();
        APIInterface apiInterface = restClient.createService();
        Call<MoviesPageResbonseModel> call = apiInterface.getMovies(API_KEY, LANGUAGE, SORT, page);
        call.enqueue(new BaseResponse<MoviesPageResbonseModel>(listener));
    }

    public void getMovieVideo(int id, BaseResponseListener listener){
        RestClient restClient = new RestClient();
        APIInterface apiInterface = restClient.createService();
        Call<MovieVideoResponseModel> call = apiInterface.getMovieVideo(id, API_KEY, LANGUAGE);
        call.enqueue(new BaseResponse<MovieVideoResponseModel>(listener));
    }

    public Single<MoviesPageResbonseModel> searchMovie(int page, String query) {
        RestClient restClient = new RestClient();
        APIInterface apiInterface = restClient.createService();
        Single<MoviesPageResbonseModel> call = apiInterface.searchMovie(API_KEY, LANGUAGE, query, page);
        return call;
//        call.enqueue(new BaseResponse<MoviesPageResbonseModel>(listener));
    }
}
