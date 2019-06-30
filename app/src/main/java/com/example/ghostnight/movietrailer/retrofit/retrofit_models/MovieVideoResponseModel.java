package com.example.ghostnight.movietrailer.retrofit.retrofit_models;

import java.util.ArrayList;

public class MovieVideoResponseModel {
    int id;
    ArrayList<MovieResultResponseModel> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<MovieResultResponseModel> getResults() {
        return results;
    }

    public void setResults(ArrayList<MovieResultResponseModel> results) {
        this.results = results;
    }
}
