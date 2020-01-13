package com.example.ghostnight.movietrailer.ui.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.ghostnight.movietrailer.R;
import com.example.ghostnight.movietrailer.databinding.ActivityMovieDetailBinding;
import com.example.ghostnight.movietrailer.model.Movie;
import com.example.ghostnight.movietrailer.ui.viewmodel.DetailsViewModel;
import com.example.ghostnight.movietrailer.ui.viewmodel.ViewModelFactory;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

public class MovieDetailsActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {

    private static final String YOUTUBE_API_KEY = "AIzaSyDO_1Jvq21Mkf1GticNvkmToLH7PkrVvJw";
    private DetailsViewModel detailsViewModel;
    private Movie mMovie;
    private static final int RECOVERY_REQUEST = 1;
    private ActivityMovieDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

        detailsViewModel = ViewModelProviders.of(this, new ViewModelFactory(this.getApplication(),
                (Movie) getIntent().getSerializableExtra("movie"))).get(DetailsViewModel.class);


        detailsViewModel.getMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                if (movie != null) {
                    mMovie = movie;
                    if (detailsViewModel.isMovieFavorite(movie.getId())) {
                        binding.favoriteBtn.setImageResource(R.drawable.ic_favorite_btn);
                    } else {
                        binding.favoriteBtn.setImageResource(R.drawable.ic_unfavorite_btn);
                    }

                    Glide.with(getApplicationContext())
                            .load("https://image.tmdb.org/t/p/w500" + movie.getImage())
                            .placeholder(R.drawable.poster_holder)
                            .into(binding.image);

                    binding.title.setText(movie.getTitle());
                    binding.rating.setText(String.valueOf(movie.getRating()));
                    binding.releaseDate.setText(movie.getRelease_date());
                    binding.overview.setText(movie.getOverview());
                }
            }
        });


        binding.favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailsViewModel.isMovieFavorite(mMovie.getId())) {
                    binding.favoriteBtn.setImageResource(R.drawable.ic_unfavorite_btn);
                    detailsViewModel.removeMovieFromFavorite(mMovie);
                } else {
                    binding.favoriteBtn.setImageResource(R.drawable.ic_favorite_btn);
                    detailsViewModel.addMovieToFavorite(mMovie);
                }
            }
        });

//        youTubeFragment.initialize(YOUTUBE_API_KEY, MovieDetailsActivity.this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.cueVideo(mMovie.getVideoPath());
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format("Error initializing YouTube player: ", youTubeInitializationResult.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

}
