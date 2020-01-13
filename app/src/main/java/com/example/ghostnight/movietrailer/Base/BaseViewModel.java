package com.example.ghostnight.movietrailer.Base;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ghostnight.movietrailer.model.Movie;
import com.example.ghostnight.movietrailer.repo.MovieRepository;

import java.util.List;

public class BaseViewModel extends AndroidViewModel {

    public MutableLiveData<Boolean> showLoading = new MutableLiveData<>();
    protected Context context;
    protected MovieRepository repo;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        repo = MovieRepository.getInstance(application.getApplicationContext());
    }

    public LiveData<Boolean> getShowLoading() {
        return showLoading;
    }

    public void addMovieToFavorite(Movie movie) {
        repo.addMovieToFavorite(movie);
    }

    public void removeMovieFromFavorite(Movie movie) {
        repo.removeMovieFromFavorite(movie);
    }

    public boolean isMovieFavorite(int id) {
        return repo.findMovie(id);
    }

    public LiveData<List<Movie>> getAllMovies() {
        return repo.getAllMovies();
    }


}
