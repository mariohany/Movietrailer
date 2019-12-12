package com.example.ghostnight.movietrailer.ui.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ghostnight.movietrailer.model.Movie;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private Movie mParam;


    public ViewModelFactory(Application application, Movie param) {
        mApplication = application;
        mParam = param;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailsViewModel(mApplication, mParam);
    }
}
