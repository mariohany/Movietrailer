package com.example.ghostnight.movietrailer.ui.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ghostnight.movietrailer.Base.BaseViewModel;
import com.example.ghostnight.movietrailer.model.Movie;

public class DetailsViewModel extends BaseViewModel {
    private Context context;
    private MutableLiveData<Movie> movie = new MutableLiveData<>();

    public DetailsViewModel(@NonNull Application application, Movie mParam) {
        super(application);
        this.context = application.getApplicationContext();
        movie.setValue(mParam);
        movie = repo.getVideo(mParam);
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }

}
