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
import com.example.ghostnight.movietrailer.databinding.FragmentHomeBinding;
import com.example.ghostnight.movietrailer.model.Movie;
import com.example.ghostnight.movietrailer.ui.activity.MovieDetailsActivity;
import com.example.ghostnight.movietrailer.ui.adapter.MovieListAdapter;
import com.example.ghostnight.movietrailer.ui.viewmodel.HomeViewModel;
import com.example.ghostnight.movietrailer.utills.NetworkUtils;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private ArrayList<Movie> movies = new ArrayList<>();
    private HomeViewModel homeViewModel;
    private MovieListAdapter adapter;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        binding = DataBindingUtil.bind(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        initMoviesList();

        homeViewModel.getMovies().observe(this, new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> moviess) {
                if (NetworkUtils.isNetworkConnected(getContext())) {
                    if (moviess != null && moviess.size() > 0) {
                        binding.noItems.setVisibility(View.GONE);
                        binding.list.setVisibility(View.VISIBLE);
                        movies.clear();
                        movies.addAll(moviess);
                        adapter.notifyDataSetChanged();
                    } else {
                        binding.list.setVisibility(View.GONE);
                        binding.noItems.setText(R.string.no_movies);
                        binding.noItems.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.list.setVisibility(View.GONE);
                    binding.noItems.setText(R.string.no_internet);
                    binding.noItems.setVisibility(View.VISIBLE);
                }
            }
        });

        homeViewModel.getShowLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    binding.progress.setVisibility(View.VISIBLE);
                    binding.list.setVisibility(View.GONE);
                    binding.noItems.setVisibility(View.GONE);
                } else {
                    binding.progress.setVisibility(View.GONE);
                    binding.swiperefresh.setRefreshing(false);
                }
            }
        });

        binding.swiperefresh.setOnRefreshListener(homeViewModel.refreshListener);
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
                homeViewModel.loadMoreMovies();
            }

            @Override
            public void onAddToFavoriteClick(Movie movie) {
                homeViewModel.addMovieToFavorite(movie);
            }

            @Override
            public void onRemoveFromFavoriteClick(Movie movie) {
                homeViewModel.removeMovieFromFavorite(movie);
            }
        }, getContext(), homeViewModel);

        binding.list.setAdapter(adapter);
    }
}