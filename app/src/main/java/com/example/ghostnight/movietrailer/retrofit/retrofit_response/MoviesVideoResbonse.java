package com.example.ghostnight.movietrailer.retrofit.retrofit_response;

import com.example.ghostnight.movietrailer.retrofit.retrofit_models.MovieVideoResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesVideoResbonse implements Callback<MovieVideoResponseModel> {

    private MoviesVideoResbonseListener mListener;

    public MoviesVideoResbonse(MoviesVideoResbonseListener listener) {
        mListener = listener;
    }

    @Override
    public void onResponse(Call<MovieVideoResponseModel> call, Response<MovieVideoResponseModel> response) {
        if (response.code() == 200 && response.body() != null) {
            onGetMoviesVideoSuccessfuly(response.body());
        } else {
            onGetMoviesVideoFailed("Error in onResponse method");
        }
    }

    @Override
    public void onFailure(Call<MovieVideoResponseModel> call, Throwable t) {
        onGetMoviesVideoFailed(t.getLocalizedMessage());
    }

    public interface MoviesVideoResbonseListener {
        void onGetMoviesVideoSuccessfuly(MovieVideoResponseModel body);

        void onGetMoviesVideoFailed(String status_message);
    }

    private void onGetMoviesVideoFailed(String status_message) {
        if (mListener != null)
            mListener.onGetMoviesVideoFailed(status_message);
    }

    private void onGetMoviesVideoSuccessfuly(MovieVideoResponseModel body) {
        if (mListener != null)
            mListener.onGetMoviesVideoSuccessfuly(body);
    }
}
