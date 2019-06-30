package com.example.ghostnight.movietrailer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ghostnight.movietrailer.BuildConfig;
import com.example.ghostnight.movietrailer.R;
import com.example.ghostnight.movietrailer.model.MovieHelper;
import com.example.ghostnight.movietrailer.model.MovieHolder;
import com.example.ghostnight.movietrailer.retrofit.NetworkService;
import com.example.ghostnight.movietrailer.retrofit.retrofit_models.MovieVideoResponseModel;
import com.example.ghostnight.movietrailer.retrofit.retrofit_response.MoviesVideoResbonse;
import com.example.ghostnight.movietrailer.utills.NetworkUtils;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import io.realm.Realm;

public class MovieDetailActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener
        , MoviesVideoResbonse.MoviesVideoResbonseListener {

    private static final String YOUTUBE_API_KEY = BuildConfig.YoutubeApiKey;

    private ImageView mImage;
    private TextView mTitle;
    private TextView mRating;
    private TextView mReleaseDate;
    private TextView mOverview;
    private ImageView mFavoriteBtn;

    private int id;
    private String title;
    private String overview;
    private String releaseDate;
    private float rating;
    private String image;
    private String video;
    private boolean favorite;
    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private Realm mRealm;
    private MovieHelper mHelper;
    private MovieHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        id = getIntent().getIntExtra("id", 0);
        title = getIntent().getStringExtra("title");
        overview = getIntent().getStringExtra("overview");
        releaseDate = getIntent().getStringExtra("releaseDate");
        rating = getIntent().getFloatExtra("rating", 0);
        image = getIntent().getStringExtra("image");
        favorite = getIntent().getBooleanExtra("favorite", false);

        holder = new MovieHolder(id, title, image, rating, video, overview, releaseDate, favorite);

        mRealm = Realm.getDefaultInstance();
        mHelper = MovieHelper.getInstance(this);

        mImage = findViewById(R.id.image);
        mTitle = findViewById(R.id.title);
        mRating = findViewById(R.id.rating);
        mReleaseDate = findViewById(R.id.release_date);
        mOverview = findViewById(R.id.overview);
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        mFavoriteBtn = findViewById(R.id.favorite_btn);
        if (favorite) {
            mFavoriteBtn.setImageResource(R.drawable.ic_favorite_btn);
        } else {
            mFavoriteBtn.setImageResource(R.drawable.ic_unfavorite_btn);
        }
        if (NetworkUtils.isNetworkConnected(this)) {
            NetworkService.getInstance().getMovieVideo(id, this);
        }
        if(image != null) {
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w500" + image)
                    .into(mImage);
        }else{
            mImage.setImageResource(R.drawable.poster_holder);
        }
        mTitle.setText(title);
        mRating.setText(String.valueOf(rating));
        mReleaseDate.setText(releaseDate);
        mOverview.setText(overview);
        mFavoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favorite) {
                    mFavoriteBtn.setImageResource(R.drawable.ic_unfavorite_btn);
                    mHelper.removeFromFavorite(id, mRealm);
                    favorite = false;
                } else {
                    mFavoriteBtn.setImageResource(R.drawable.ic_favorite_btn);
                    mHelper.addToFavorite(holder, mRealm);
                    favorite = true;
                }
            }
        });

    }

    @Override
    public void onGetMoviesVideoSuccessfuly(final MovieVideoResponseModel body) {
        if (body.getResults().size() > 0)
            video = body.getResults().get(0).getKey();

        youTubeView.initialize(YOUTUBE_API_KEY, this);
    }

    @Override
    public void onGetMoviesVideoFailed(String status_message) {
        Toast.makeText(this, status_message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(YOUTUBE_API_KEY, this);
        }
    }

    public YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.cueVideo(video);
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
