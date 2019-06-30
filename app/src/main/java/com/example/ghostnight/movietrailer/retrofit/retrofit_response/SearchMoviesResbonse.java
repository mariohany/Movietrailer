package com.example.ghostnight.movietrailer.retrofit.retrofit_response;

import com.example.ghostnight.movietrailer.retrofit.retrofit_models.SearchMoviesPageResbonseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMoviesResbonse implements Callback<SearchMoviesPageResbonseModel> {

    private MoviesResbonseListener mListener;

    public SearchMoviesResbonse(MoviesResbonseListener listener) {
        mListener = listener;
    }

    @Override
    public void onResponse(Call<SearchMoviesPageResbonseModel> call, Response<SearchMoviesPageResbonseModel> response) {
        if (response.code() == 200 && response.body() != null) {
            onGetMoviesSuccessfuly(response.body());
        } else if (response.body() != null && response.body().getStatus_message() != null) {
            onGetMoviesFailed(response.body().getStatus_message());
        } else
            onGetMoviesFailed("Failed to get Movies from API");
    }

    @Override
    public void onFailure(Call<SearchMoviesPageResbonseModel> call, Throwable t) {
        onGetMoviesFailed(t.getLocalizedMessage());
    }

    public interface MoviesResbonseListener {
        void onGetMoviesSuccessfuly(SearchMoviesPageResbonseModel body);

        void onGetMoviesFailed(String status_message);
    }

    private void onGetMoviesFailed(String status_message) {
        if (mListener != null)
            mListener.onGetMoviesFailed(status_message);
    }

    private void onGetMoviesSuccessfuly(SearchMoviesPageResbonseModel body) {
        if (mListener != null)
            mListener.onGetMoviesSuccessfuly(body);
    }
}
