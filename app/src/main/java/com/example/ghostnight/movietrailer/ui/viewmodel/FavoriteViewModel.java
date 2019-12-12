package com.example.ghostnight.movietrailer.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.ghostnight.movietrailer.Base.BaseViewModel;
import com.example.ghostnight.movietrailer.model.Movie;

import java.util.List;

public class FavoriteViewModel extends BaseViewModel {

    private LiveData<List<Movie>> movies;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        movies = getAllMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
}