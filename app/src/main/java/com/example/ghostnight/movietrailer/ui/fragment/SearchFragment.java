package com.example.ghostnight.movietrailer.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ghostnight.movietrailer.R;
import com.example.ghostnight.movietrailer.databinding.FragmentSearchBinding;
import com.example.ghostnight.movietrailer.model.Movie;
import com.example.ghostnight.movietrailer.ui.activity.MovieDetailsActivity;
import com.example.ghostnight.movietrailer.ui.adapter.MovieListAdapter;
import com.example.ghostnight.movietrailer.ui.viewmodel.SearchViewModel;
import com.example.ghostnight.movietrailer.utills.NetworkUtils;

import java.util.ArrayList;


public class SearchFragment extends Fragment {
    private ArrayList<Movie> movies = new ArrayList<>();
    private SearchViewModel searchViewModel;
    private MovieListAdapter adapter;
    private FragmentSearchBinding binding;
    private String query;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        binding = DataBindingUtil.bind(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initMoviesList();

        binding.queryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                query = s.toString();
                if (!query.equals(""))
                    binding.clearBtn.setVisibility(View.VISIBLE);
                else
                    binding.clearBtn.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.queryEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchViewModel.searchMovie(v.getText().toString());
                return true;
            }
        });

        binding.clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.queryEditText.setText("");
            }
        });

        searchViewModel.getShowLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    binding.progress.setVisibility(View.VISIBLE);
                    binding.list.setVisibility(View.GONE);
                    binding.noItems.setVisibility(View.GONE);
                } else {
                    binding.progress.setVisibility(View.GONE);
                }
            }
        });

        searchViewModel.getMovies().observe(this, new Observer<ArrayList<Movie>>() {
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
                searchViewModel.searchMovie(binding.queryEditText.getText().toString());
            }

            @Override
            public void onAddToFavoriteClick(Movie movie) {
                searchViewModel.addMovieToFavorite(movie);
            }

            @Override
            public void onRemoveFromFavoriteClick(Movie movie) {
                searchViewModel.removeMovieFromFavorite(movie);
            }
        }, getContext(), searchViewModel);

        binding.list.setAdapter(adapter);
    }
}