package com.example.ghostnight.movietrailer.ui.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ghostnight.movietrailer.Base.BaseViewModel;
import com.example.ghostnight.movietrailer.model.Movie;
import com.example.ghostnight.movietrailer.model.MoviesPageResbonseModel;
import com.example.ghostnight.movietrailer.repo.MovieRepository;

import java.util.ArrayList;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchViewModel extends BaseViewModel {

    private static final String TAG = SearchViewModel.class.getName();
    private Context context;
    private String lastQuery = "";
    private int pageNumber = 1;
    private MutableLiveData<ArrayList<Movie>> movies = new MutableLiveData<>();
    private MovieRepository repo;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        movies.setValue(new ArrayList<Movie>());
        repo = MovieRepository.getInstance(application);
    }

    public LiveData<ArrayList<Movie>> getMovies() {
        return movies;
    }


    public void searchMovie(String query) {
        if (!lastQuery.equalsIgnoreCase(query)) {
            pageNumber = 1;
            movies.setValue(new ArrayList<Movie>());
        }
        showLoading.setValue(true);
        repo.searchMovie(pageNumber, query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<MoviesPageResbonseModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onSuccess(MoviesPageResbonseModel moviesPageResbonseModel) {
                        if (pageNumber == 1) {
                            movies.setValue(moviesPageResbonseModel.getResults());
                            showLoading.setValue(false);
                        } else {
                            ArrayList<Movie> oldMovies = movies.getValue();
                            oldMovies.addAll(moviesPageResbonseModel.getResults());
                            movies.setValue(oldMovies);
                            showLoading.setValue(false);
                        }
                        pageNumber++;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        showLoading.setValue(false);
                    }
                });
    }
}