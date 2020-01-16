package com.example.ghostnight.movietrailer.repo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ghostnight.movietrailer.model.Movie;
import com.example.ghostnight.movietrailer.model.MovieVideoResponseModel;
import com.example.ghostnight.movietrailer.model.MoviesPageResbonseModel;
import com.example.ghostnight.movietrailer.retrofit.NetworkService;
import com.example.ghostnight.movietrailer.room.MovieDao;
import com.example.ghostnight.movietrailer.room.MovieDatabase;
import com.example.ghostnight.movietrailer.utills.NetworkUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieRepository {

    private static final String TAG = MovieRepository.class.getName();
    private static MovieRepository INSTANCE;
    private final Context context;
    private MovieDatabase movieDatabase;
    private MovieDao movieDao;
    private LiveData movies;

    private MovieRepository(Context context) {
        this.context = context;
        movieDatabase = MovieDatabase.getInstance(context);
        movieDao = movieDatabase.movieDao();
        movies = movieDao.getAllMovies();
    }

    public static MovieRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MovieRepository(context);
        }
        return INSTANCE;
    }


    public void addMovieToFavorite(final Movie movie) {
        new InsertMovieAsyncTask(movieDao).execute(movie);
    }

    public void removeMovieFromFavorite(final Movie movie) {
        new RemoveMovieAsyncTask(movieDao).execute(movie);
    }

    public boolean findMovie(final int movieId) {
        try {
            return new FindMovieAsyncTask(movieDao).execute(movieId).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public LiveData<List<Movie>> getAllMovies() {
        return movieDao.getAllMovies();
    }

    public MutableLiveData<Movie> getVideo(final Movie movie) {
        final MutableLiveData<Movie> movieLiveData = new MutableLiveData<>();
        if (NetworkUtils.isNetworkConnected(context)) {
            NetworkService.getInstance().getMovieVideo(movie.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<MovieVideoResponseModel>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(TAG, "onSubscribe: " + Thread.currentThread().getName());
                        }

                        @Override
                        public void onSuccess(MovieVideoResponseModel responseModel) {
                            if (responseModel.getResults().size() > 0) {
                                movie.setVideoPath(responseModel.getResults().get(0).getKey());
                                movieLiveData.setValue(movie);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
        return movieLiveData;
    }

    public Single<MoviesPageResbonseModel> searchMovie(int pageNumber, String query) {
        return NetworkService.getInstance().searchMovie(pageNumber, query);
    }

    @SuppressLint("StaticFieldLeak")
    private final class InsertMovieAsyncTask extends AsyncTask<Movie, Void, Void> {
        MovieDao dao;

        InsertMovieAsyncTask(MovieDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            dao.insert(movies[0]);
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private final class RemoveMovieAsyncTask extends AsyncTask<Movie, Void, Void> {
        MovieDao dao;

        RemoveMovieAsyncTask(MovieDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            dao.delete(movies[0]);
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private final class FindMovieAsyncTask extends AsyncTask<Integer, Void, Boolean> {
        MovieDao dao;

        FindMovieAsyncTask(MovieDao dao) {
            this.dao = dao;
        }

        @Override
        protected Boolean doInBackground(Integer... ids) {
            return dao.findMovie(ids[0]) != 0;
        }
    }
}
