package com.example.ghostnight.movietrailer.model;

import android.content.Context;

import java.util.List;

import io.realm.Realm;

public class MovieHelper {
    private static MovieHelper mInstance;

    public static MovieHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MovieHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    public boolean isMovieInFavorite(int id, Realm realm) {
        if (realm.where(Movie.class).equalTo("id", id).findFirst() != null) {
            return true;
        }
        return false;
    }

    private final String TAG = getClass().getSimpleName();

    private Context mContext;

    private MovieHelper(Context context) {
        mContext = context;
    }

    public List<Movie> getAllMovies(Realm realm) {
        return realm.where(Movie.class).findAll();
    }

    public void removeFromFavorite(final int id, Realm realm) {
        if (realm.where(Movie.class).equalTo("id", id).findFirst() != null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(Movie.class).equalTo("id", id).findFirst().deleteFromRealm();
                }
            });
        }
    }

    public void addToFavorite(final MovieHolder model, Realm realm) {
        if (realm.where(Movie.class).equalTo("id", model.getId()).findFirst() == null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Movie movie = realm.createObject(Movie.class, model.getId());
                    movie.setTitle(model.getTitle());
                    movie.setRelease_date(model.getRelease_date());
                    movie.setRating(model.getRating());
                    movie.setImage(model.getImage());
                    movie.setOverview(model.getOverview());
                    movie.setFavorite(true);
                }
            });
        }
    }
}
