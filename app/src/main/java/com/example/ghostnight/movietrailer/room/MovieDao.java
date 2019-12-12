package com.example.ghostnight.movietrailer.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.ghostnight.movietrailer.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie movie);

    @Delete()
    void delete(Movie movie);

    @Query(value = "SELECT * FROM movies_table")
    LiveData<List<Movie>> getAllMovies();

    @Query(value = "SELECT id FROM movies_table WHERE id == :movieId")
    int findMovie(int movieId);

}
