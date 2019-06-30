package com.example.ghostnight.movietrailer.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ghostnight.movietrailer.R;
import com.example.ghostnight.movietrailer.model.MovieHelper;
import com.example.ghostnight.movietrailer.model.MovieHolder;
import com.example.ghostnight.movietrailer.retrofit.NetworkService;
import com.example.ghostnight.movietrailer.retrofit.retrofit_models.SearchMoviesPageResbonseModel;
import com.example.ghostnight.movietrailer.retrofit.retrofit_response.SearchMoviesResbonse;
import com.example.ghostnight.movietrailer.ui.adapter.APIMovieListAdapter;
import com.example.ghostnight.movietrailer.utills.NetworkUtils;

import java.util.ArrayList;

import io.realm.Realm;

public class SearchFragment extends Fragment implements APIMovieListAdapter.ListItemClickListener, SearchMoviesResbonse.MoviesResbonseListener {

    private OnFragmentInteractionListener mListener;
    private RecyclerView list;
    private APIMovieListAdapter adapter;
    private ArrayList<MovieHolder> mMovies;
    private EditText queryEdit;
    private Button clearBtn;
    private Realm mRealm;
    private MovieHelper mHelper;
    private TextView noItem;
    private int pageNumber;
    private String query;
    private ProgressBar loader;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        pageNumber=1;
        mMovies = new ArrayList<>();
        mRealm = Realm.getDefaultInstance();
        mHelper = MovieHelper.getInstance(getContext());
        loader = view.findViewById(R.id.progress);
        noItem = view.findViewById(R.id.noItems);
        clearBtn = view.findViewById(R.id.clear_btn);
        queryEdit = view.findViewById(R.id.query_editText);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        list = view.findViewById(R.id.list);
        list.setLayoutManager(gridLayoutManager);
        list.setHasFixedSize(true);

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryEdit.getText().clear();
                clearBtn.setVisibility(View.GONE);
            }
        });

        queryEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s == ""){
                    clearBtn.setVisibility(View.GONE);
                }else{
                    clearBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")){
                    clearBtn.setVisibility(View.GONE);
                }
            }
        });

        queryEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    queryEdit.clearFocus();
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(queryEdit.getWindowToken(), 0);
                    pageNumber = 1;
                    query = queryEdit.getText().toString();
                    callApi(pageNumber, query);
                    return true;
                }
                return false;
            }
        });



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

    public void callApi(int pageNumber, String query){
        showLoader();
        if (NetworkUtils.isNetworkConnected(getContext())) {
            NetworkService.getInstance().searchMovie(pageNumber, query, SearchFragment.this);
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            populateList();
        }
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
                callApi(pageNumber, query);
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
    public void onGetMoviesSuccessfuly(SearchMoviesPageResbonseModel body) {
        if(pageNumber == 1) {
            mMovies.clear();
        }
        if(pageNumber == 1) {
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
            hideLoader();
        }
    }

    @Override
    public void onGetMoviesFailed(String status_message) {
        Toast.makeText(getContext(), status_message, Toast.LENGTH_LONG).show();
        populateList();
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
