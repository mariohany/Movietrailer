package com.example.ghostnight.movietrailer.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ghostnight.movietrailer.R;
import com.example.ghostnight.movietrailer.databinding.FragmentFavoriteBinding;
import com.example.ghostnight.movietrailer.model.Movie;
import com.example.ghostnight.movietrailer.ui.activity.MovieDetailsActivity;
import com.example.ghostnight.movietrailer.ui.adapter.MovieListAdapter;
import com.example.ghostnight.movietrailer.ui.viewmodel.FavoriteViewModel;

import java.util.ArrayList;
import java.util.List;


public class FavoriteFragment extends Fragment {
    private ArrayList<Movie> movies = new ArrayList<>();
    private FavoriteViewModel favoriteViewModel;
    private MovieListAdapter adapter;
    private FragmentFavoriteBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        binding = DataBindingUtil.bind(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);

        favoriteViewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> moviess) {
                if (moviess != null && moviess.size() > 0) {
                    binding.noItems.setVisibility(View.GONE);
                    binding.list.setVisibility(View.VISIBLE);
                    movies.clear();
                    movies.addAll(moviess);
                    adapter.notifyDataSetChanged();
                } else {
                    binding.list.setVisibility(View.GONE);
                    binding.noItems.setText(R.string.no_favorite_movies);
                    binding.noItems.setVisibility(View.VISIBLE);
                }
            }
        });


        initMoviesList();
    }

    private void initMoviesList() {
        binding.list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.list.setHasFixedSize(true);

        adapter = new MovieListAdapter(movies, new MovieListAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(Movie movie) {
                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }

            @Override
            public void onLoadMoreClick() {
            }

            @Override
            public void onAddToFavoriteClick(Movie movie) {
                favoriteViewModel.addMovieToFavorite(movie);
            }

            @Override
            public void onRemoveFromFavoriteClick(Movie movie) {
                favoriteViewModel.removeMovieFromFavorite(movie);
            }
        }, getContext(), favoriteViewModel);

        binding.list.setAdapter(adapter);
    }
}