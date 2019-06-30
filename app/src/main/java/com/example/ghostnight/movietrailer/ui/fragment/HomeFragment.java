package com.example.ghostnight.movietrailer.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ghostnight.movietrailer.R;
import com.example.ghostnight.movietrailer.model.MovieHelper;
import com.example.ghostnight.movietrailer.model.MovieHolder;
import com.example.ghostnight.movietrailer.retrofit.NetworkService;
import com.example.ghostnight.movietrailer.retrofit.retrofit_models.MoviesPageResbonseModel;
import com.example.ghostnight.movietrailer.retrofit.retrofit_response.MoviesResbonse;
import com.example.ghostnight.movietrailer.ui.adapter.APIMovieListAdapter;
import com.example.ghostnight.movietrailer.utills.NetworkUtils;

import java.util.ArrayList;

import io.realm.Realm;

public class HomeFragment extends Fragment implements APIMovieListAdapter.ListItemClickListener, MoviesResbonse.MoviesResbonseListener {

    private OnFragmentInteractionListener mListener;
    private RecyclerView list;
    private APIMovieListAdapter adapter;
    private ArrayList<MovieHolder> mMovies;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private int pageNumber;
    private Realm mRealm;
    private MovieHelper mHelper;
    private TextView noItem;
    private ProgressBar loader;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRealm = Realm.getDefaultInstance();
        mHelper = MovieHelper.getInstance(getContext());
        mMovies = new ArrayList<MovieHolder>();
        loader = view.findViewById(R.id.progress);
        noItem = view.findViewById(R.id.noItems);
        pageNumber = 1;
        linearLayoutManager = new GridLayoutManager(getContext(), 1);
        list = view.findViewById(R.id.list);
        list.setLayoutManager(linearLayoutManager);
        list.setHasFixedSize(true);
        refreshLayout = view.findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callApi(pageNumber);
            }
        });

        callApi(pageNumber);

        return view;
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
    public void onListItemClick(MovieHolder movie) {
        mListener.onListItemSelected(movie);
    }

    @Override
    public void onLoadMoreClick() {
        mMovies.add(null);
        adapter.notifyItemInserted(mMovies.size()-1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mMovies.remove(mMovies.size()-1);
                adapter.notifyItemRemoved(mMovies.size());
                pageNumber++;
                callApi(pageNumber);
            }
        }, 1500);
    }

    @Override
    public void onAddToFavoriteClick(MovieHolder movie) {
        mListener.onAddToFavorite(movie);
        movie.setFavorite(true);
    }

    @Override
    public void onRemoveFromFavoriteClick(MovieHolder movie) {
        mListener.onRemoveFromFavorite(movie);
        movie.setFavorite(false);
    }

    @Override
    public void onGetMoviesSuccessfuly(MoviesPageResbonseModel body) {
        if (pageNumber==1) {
            for (int i = 0; i < body.getResults().size(); i++) {
                mMovies.add(new MovieHolder(body.getResults().get(i).getId(),
                        body.getResults().get(i).getTitle(),
                        body.getResults().get(i).getPoster_path(),
                        body.getResults().get(i).getVote_average(),
                        null,
                        body.getResults().get(i).getOverview(),
                        body.getResults().get(i).getRelease_date(),
                        false));
                if (mHelper.isMovieInFavorite(body.getResults().get(i).getId(), mRealm)) {
                    mMovies.get(i).setFavorite(true);
                }
            }
            populateList();
        }else{
            for (int i = 0; i < body.getResults().size(); i++) {
                mMovies.add(new MovieHolder(body.getResults().get(i).getId(),
                        body.getResults().get(i).getTitle(),
                        body.getResults().get(i).getPoster_path(),
                        body.getResults().get(i).getVote_average(),
                        null,
                        body.getResults().get(i).getOverview(),
                        body.getResults().get(i).getRelease_date(),
                        false));
                if (mHelper.isMovieInFavorite(body.getResults().get(i).getId(), mRealm)) {
                    mMovies.get(i).setFavorite(true);
                }
                adapter.notifyItemInserted(mMovies.size());
            }
            adapter.setLoaded();
            list.setVisibility(View.VISIBLE);
            noItem.setVisibility(View.GONE);
            refreshLayout.setRefreshing(false);
            hideLoader();
        }
    }

    @Override
    public void onGetMoviesFailed(String status_message) {
        Toast.makeText(getContext(), status_message, Toast.LENGTH_LONG).show();
        populateList();
    }

    public void callApi(int page) {
        showLoader();
        if (NetworkUtils.isNetworkConnected(getContext())) {
            NetworkService.getInstance().getMovies(page, HomeFragment.this);
        } else {
            Toast.makeText(getContext(), "No Internet connection!", Toast.LENGTH_SHORT).show();
            populateList();
        }
    }

    public interface OnFragmentInteractionListener {
        void onListItemSelected(MovieHolder movie);
        void onAddToFavorite(MovieHolder movie);
        void onRemoveFromFavorite(MovieHolder movie);
    }

    public void populateList() {
        if (mMovies.size() > 0) {
            adapter = new APIMovieListAdapter(mMovies, this, getContext(), list);
            list.setAdapter(adapter);
            list.setVisibility(View.VISIBLE);
            noItem.setVisibility(View.GONE);
        } else if (mMovies.size() == 0 && !NetworkUtils.isNetworkConnected(getContext())) {
            list.setVisibility(View.GONE);
            noItem.setText(getString(R.string.no_internet));
            noItem.setVisibility(View.VISIBLE);
        } else {
            list.setVisibility(View.GONE);
            noItem.setText(getString(R.string.no_movies));
            noItem.setVisibility(View.VISIBLE);
        }
        refreshLayout.setRefreshing(false);
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
