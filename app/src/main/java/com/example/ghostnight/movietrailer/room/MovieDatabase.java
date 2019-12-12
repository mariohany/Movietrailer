package com.example.ghostnight.movietrailer.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ghostnight.movietrailer.model.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static MovieDatabase INSTANCE;

    public static synchronized MovieDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    MovieDatabase.class,
                    "movies_database")
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return INSTANCE;
    }

    public abstract MovieDao movieDao();

}
