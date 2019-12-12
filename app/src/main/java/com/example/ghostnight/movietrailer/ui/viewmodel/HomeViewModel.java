package com.example.ghostnight.movietrailer.ui.viewmodel;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ghostnight.movietrailer.Base.BaseViewModel;
import com.example.ghostnight.movietrailer.model.Movie;
import com.example.ghostnight.movietrailer.model.MoviesPageResbonseModel;
import com.example.ghostnight.movietrailer.retrofit.BaseResponseListener;
import com.example.ghostnight.movietrailer.retrofit.NetworkService;
import com.example.ghostnight.movietrailer.utills.NetworkUtils;

import java.util.ArrayList;

public class HomeViewModel extends BaseViewModel {

    private Context context;
    private int pageNumber = 1;
    private MutableLiveData<ArrayList<Movie>> movies = new MutableLiveData<>();
    public SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            pageNumber = 1;
            getMoviesFromApi();
        }
    };

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        movies.setValue(new ArrayList<Movie>());
        getMoviesFromApi();

    }

    private void getMoviesFromApi() {
        if (NetworkUtils.isNetworkConnected(context)) {
            showLoading.setValue(true);
            NetworkService.getInstance().getMovies(pageNumber, new BaseResponseListener<MoviesPageResbonseModel>() {
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
                public void onFailure(String msg) {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    showLoading.setValue(false);
                }
            });
        } else if (pageNumber == 1) {
            movies.setValue(new ArrayList<Movie>());
        }
    }

    public LiveData<ArrayList<Movie>> getMovies() {
        return movies;
    }

    public void loadMoreMovies() {
        getMoviesFromApi();
    }

}