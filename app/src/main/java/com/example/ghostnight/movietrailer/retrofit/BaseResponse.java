package com.example.ghostnight.movietrailer.retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseResponse<T> implements Callback<T> {
    BaseResponseListener<T> listener;

    BaseResponse(BaseResponseListener<T> listener) {
        this.listener = listener;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        listener.onSuccess(response.body());
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        listener.onFailure(t.getLocalizedMessage());
    }

}

