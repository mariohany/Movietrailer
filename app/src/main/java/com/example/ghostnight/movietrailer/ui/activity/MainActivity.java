package com.example.ghostnight.movietrailer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.ghostnight.movietrailer.R;
import com.example.ghostnight.movietrailer.model.Movie;
import com.example.ghostnight.movietrailer.model.MovieHelper;
import com.example.ghostnight.movietrailer.model.MovieHolder;
import com.example.ghostnight.movietrailer.ui.fragment.FavoriteFragment;
import com.example.ghostnight.movietrailer.ui.fragment.HomeFragment;
import com.example.ghostnight.movietrailer.ui.fragment.SearchFragment;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener
        , FavoriteFragment.OnFragmentInteractionListener
        , SearchFragment.OnFragmentInteractionListener {

    private BottomNavigationView mNavigationView;
    Realm mRealm;
    MovieHelper mHelper;
    String fragmentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(this.getLocalClassName(), "onCreate");
        mNavigationView = findViewById(R.id.bottomNavigationView);
        mRealm = Realm.getDefaultInstance();
        mHelper = MovieHelper.getInstance(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, HomeFragment.newInstance()).commit();
        fragmentName = "Home";
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        if (!fragmentName.equalsIgnoreCase("Home")) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, HomeFragment.newInstance()).commit();
                            fragmentName = "Home";
                        }
                        return true;
                    case R.id.favorite:
                        if (!fragmentName.equalsIgnoreCase("Favorite")) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, FavoriteFragment.newInstance()).commit();
                            fragmentName = "Favorite";
                        }
                        return true;
                    case R.id.search:
                        if (!fragmentName.equalsIgnoreCase("Search")) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, SearchFragment.newInstance()).commit();
                            fragmentName = "Search";
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public void onListItemSelected(MovieHolder movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("id", movie.getId());
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("overview", movie.getOverview());
        intent.putExtra("image", movie.getImage());
        intent.putExtra("rating", movie.getRating());
        intent.putExtra("releaseDate", movie.getRelease_date());
        intent.putExtra("favorite", movie.isFavorite());
        startActivity(intent);
    }

    @Override
    public void onAddToFavorite(MovieHolder movie) {
        mHelper.addToFavorite(movie, mRealm);
    }

    @Override
    public void onRemoveFromFavorite(MovieHolder movie) {
        mHelper.removeFromFavorite(movie.getId(), mRealm);
    }

    @Override
    public void onListItemSelected(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("id", movie.getId());
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("overview", movie.getOverview());
        intent.putExtra("image", movie.getImage());
        intent.putExtra("rating", movie.getRating());
        intent.putExtra("releaseDate", movie.getRelease_date());
        intent.putExtra("favorite", movie.isFavorite());
        startActivity(intent);

    }
}
