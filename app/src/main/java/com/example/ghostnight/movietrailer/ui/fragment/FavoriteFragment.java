package com.example.ghostnight.movietrailer.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ghostnight.movietrailer.R;
import com.example.ghostnight.movietrailer.model.Movie;
import com.example.ghostnight.movietrailer.model.MovieHelper;
import com.example.ghostnight.movietrailer.ui.adapter.FavoriteMoviesAdapter;

import java.util.List;

import io.realm.Realm;

public class FavoriteFragment extends Fragment implements FavoriteMoviesAdapter.ListItemClickListener {

    private OnFragmentInteractionListener mListener;
    private RecyclerView list;
    private FavoriteMoviesAdapter adapter;
    private List<Movie> mMovies;
    private MovieHelper mHelper;
    private Realm mRealm;
    private TextView noItem;
    private ProgressBar loader;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    public static FavoriteFragment newInstance() {
        FavoriteFragment fragment = new FavoriteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        mRealm = Realm.getDefaultInstance();
        mHelper = MovieHelper.getInstance(getContext());
        noItem = view.findViewById(R.id.noItems);
        loader = view.findViewById(R.id.progress);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        list = view.findViewById(R.id.list);
        list.setLayoutManager(gridLayoutManager);
        list.setHasFixedSize(true);
        showLoader();
        mMovies = mHelper.getAllMovies(mRealm);
        populateList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showLoader();
        mMovies = mHelper.getAllMovies(mRealm);
        populateList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(Movie movie) {
        mListener.onListItemSelected(movie);
    }

    @Override
    public void onRemoveFromFavoriteClick(Movie movie) {
        showLoader();
        mHelper.removeFromFavorite(movie.getId(), mRealm);
        mMovies = mHelper.getAllMovies(mRealm);
        populateList();
        hideLoader();
    }

    public interface OnFragmentInteractionListener {
        void onListItemSelected(Movie movie);
    }

    public void populateList() {
        if (mMovies.size() > 0) {
            adapter = new FavoriteMoviesAdapter(mMovies, this, getContext());
            list.setAdapter(adapter);
            list.setVisibility(View.VISIBLE);
            noItem.setVisibility(View.GONE);
        } else {
            list.setVisibility(View.GONE);
            noItem.setVisibility(View.VISIBLE);
        }
        hideLoader();
    }

    void showLoader() {
        loader.setVisibility(View.VISIBLE);
        list.setVisibility(View.INVISIBLE);
    }

    void hideLoader() {
        loader.setVisibility(View.GONE);
        list.setVisibility(View.VISIBLE);
    }
}
