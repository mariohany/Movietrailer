package com.example.ghostnight.movietrailer.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ghostnight.movietrailer.R;
import com.example.ghostnight.movietrailer.model.Movie;

import java.util.List;

public class FavoriteMoviesAdapter extends RecyclerView.Adapter<FavoriteMoviesAdapter.ListViewHolder> {

    private FavoriteMoviesAdapter.ListItemClickListener listener;
    private Context mContext;
    private List<Movie> mMovies;

    public interface ListItemClickListener {
        void onListItemClick(Movie movie);

        void onRemoveFromFavoriteClick(Movie movie);
    }

    public FavoriteMoviesAdapter(List<Movie> movies, FavoriteMoviesAdapter.ListItemClickListener listener, Context context) {
        this.listener = listener;
        mMovies = movies;
        mContext = context;
    }

    @NonNull
    @Override
    public FavoriteMoviesAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        FavoriteMoviesAdapter.ListViewHolder viewHolder = new FavoriteMoviesAdapter.ListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteMoviesAdapter.ListViewHolder listViewHolder, int position) {
        listViewHolder.bind(position);
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


        public ListViewHolder(@NonNull View itemView) {
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
                    favoriteBtn.setImageResource(R.drawable.ic_unfavorite_btn);
                    listener.onRemoveFromFavoriteClick(mMovies.get(listIndex));
                }
            });
        }

        @Override
        public void onClick(View v) {
            listener.onListItemClick(mMovies.get(getAdapterPosition()));
        }
    }
}