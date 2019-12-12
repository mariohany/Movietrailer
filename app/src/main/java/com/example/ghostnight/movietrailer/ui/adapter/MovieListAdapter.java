package com.example.ghostnight.movietrailer.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ghostnight.movietrailer.Base.BaseViewModel;
import com.example.ghostnight.movietrailer.R;
import com.example.ghostnight.movietrailer.model.Movie;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ListViewHolder> {

    private ListItemClickListener listener;
    private Context mContext;
    private List<Movie> mMovies;
    private BaseViewModel viewModel;


    public interface ListItemClickListener {
        void onListItemClick(Movie mMovie);
        void onLoadMoreClick();
        void onAddToFavoriteClick(Movie movie);
        void onRemoveFromFavoriteClick(Movie movie);
    }

    public MovieListAdapter(List<Movie> movies, final ListItemClickListener listener, Context context, BaseViewModel viewModel) {
        this.listener = listener;
        mMovies = movies;
        mContext = context;
        this.viewModel = viewModel;
    }


    @NonNull
    @Override
    public MovieListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.ListViewHolder vh, int position) {
        vh.bind(position);
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

        void bind(final int i) {
            if (mMovies.get(i).getImage() != null) {
                Glide.with(mContext)
                        .load("https://image.tmdb.org/t/p/w300" + mMovies.get(i).getImage())
                        .into(image);
            }else{
                image.setImageResource(R.drawable.poster_holder);
            }
            title.setText(mMovies.get(i).getTitle());
            rating.setText(String.valueOf(mMovies.get(i).getRating()));
            if (viewModel.isMovieFavorite(mMovies.get(i).getId())) {
                favoriteBtn.setImageResource(R.drawable.ic_favorite_btn);
            } else {
                favoriteBtn.setImageResource(R.drawable.ic_unfavorite_btn);
            }
            favoriteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewModel.isMovieFavorite(mMovies.get(i).getId())) {
                        favoriteBtn.setImageResource(R.drawable.ic_unfavorite_btn);
                        listener.onRemoveFromFavoriteClick(mMovies.get(i));
                    } else {
                        favoriteBtn.setImageResource(R.drawable.ic_favorite_btn);
                        listener.onAddToFavoriteClick(mMovies.get(i));
                    }
                }
            });

            if (i == getItemCount() - 1) {
                listener.onLoadMoreClick();
            }

        }

        @Override
        public void onClick(View v) {
            listener.onListItemClick(mMovies.get(getAdapterPosition()));
        }
    }
}