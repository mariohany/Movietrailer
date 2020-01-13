package com.example.ghostnight.movietrailer.retrofit;

public interface BaseResponseListener<T>{
    void onSuccess(T t);
    void onFailure(String msg);
}
