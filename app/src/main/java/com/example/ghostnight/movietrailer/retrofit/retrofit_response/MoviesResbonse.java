package com.example.ghostnight.movietrailer.retrofit.retrofit_response;

import com.example.ghostnight.movietrailer.retrofit.retrofit_models.MoviesPageResbonseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesResbonse implements Callback<MoviesPageResbonseModel> {

    private MoviesResbonseListener mListener;

    public MoviesResbonse(MoviesResbonseListener listener) {
        mListener = listener;
    }

    @Override
    public void onResponse(Call<MoviesPageResbonseModel> call, Response<MoviesPageResbonseModel> response) {
        if(response.code()==200 && response.body() != null){
            onGetMoviesSuccessfuly(response.body());
        }else if(response.body() != null && response.body().getStatus_message()!= null){
                onGetMoviesFailed(response.body().getStatus_message());
        }else
            onGetMoviesFailed("Failed to get Movies from API");
    }

    @Override
    public void onFailure(Call<MoviesPageResbonseModel> call, Throwable t) {
        onGetMoviesFailed(t.getLocalizedMessage());
    }

    public interface MoviesResbonseListener {
        void onGetMoviesSuccessfuly(MoviesPageResbonseModel body);
        void onGetMoviesFailed(String status_message);
    }

    private void onGetMoviesFailed(String status_message) {
        if (mListener != null)
            mListener.onGetMoviesFailed(status_message);
    }

    private void onGetMoviesSuccessfuly(MoviesPageResbonseModel body) {
        if (mListener != null)
            mListener.onGetMoviesSuccessfuly(body);
    }
}
