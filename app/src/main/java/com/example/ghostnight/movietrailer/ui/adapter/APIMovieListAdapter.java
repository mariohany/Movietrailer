package com.example.ghostnight.movietrailer.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ghostnight.movietrailer.R;
import com.example.ghostnight.movietrailer.model.MovieHolder;

import java.util.List;

public class APIMovieListAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private ListItemClickListener listener;
    private Context mContext;
    private List<MovieHolder> mMovies;
    private int lastVisibleItem, totalItemCount;
    private int visibleThreshold = 5;
    private boolean loading;


    public interface ListItemClickListener {
        void onListItemClick(MovieHolder mMovie);
        void onLoadMoreClick();
        void onAddToFavoriteClick(MovieHolder movie);
        void onRemoveFromFavoriteClick(MovieHolder movie);
    }

    public APIMovieListAdapter(List<MovieHolder> movies, final ListItemClickListener listener, Context context, RecyclerView recyclerView) {
        this.listener = listener;
        mMovies = movies;
        mContext = context;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                .getLayoutManager();
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    // End has been reached
                    // Do something
                    listener.onLoadMoreClick();
                    loading = true;
                }
            }
        });
    }
    @Override
    public int getItemViewType(int position) {
        return mMovies.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        RecyclerView.ViewHolder vh;
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view;
        if (i == VIEW_ITEM) {
            view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
            vh = new ListViewHolder(view);
        }else {
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.load_more, viewGroup, false);
            vh = new ProgressViewHolder(view);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        if(vh instanceof ListViewHolder) {
            ((ListViewHolder) vh).bind(position);
        }else{
            ((ProgressViewHolder) vh).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        ImageView image;
        TextView title;
        TextView rating;
        ImageView favoriteBtn;


        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            rating = itemView.findViewById(R.id.rating);
            favoriteBtn = itemView.findViewById(R.id.favorite_btn);
        }

        void bind(final int listIndex) {
            if(mMovies.get(listIndex).getImage() != null) {
                Glide.with(mContext)
                        .load("https://image.tmdb.org/t/p/w500" + mMovies.get(listIndex).getImage())
                        .into(image);
            }else{
                image.setImageResource(R.drawable.poster_holder);
            }
            title.setText(mMovies.get(listIndex).getTitle());
            rating.setText(String.valueOf(mMovies.get(listIndex).getRating()));
            if (mMovies.get(listIndex).isFavorite()) {
                favoriteBtn.setImageResource(R.drawable.ic_favorite_btn);
            } else {
                favoriteBtn.setImageResource(R.drawable.ic_unfavorite_btn);
            }
            favoriteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMovies.get(listIndex).isFavorite()) {
                        favoriteBtn.setImageResource(R.drawable.ic_unfavorite_btn);
                        listener.onRemoveFromFavoriteClick(mMovies.get(listIndex));
                    } else {
                        favoriteBtn.setImageResource(R.drawable.ic_favorite_btn);
                        listener.onAddToFavoriteClick(mMovies.get(listIndex));
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            listener.onListItemClick(mMovies.get(getAdapterPosition()));
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar1);
        }
    }
}