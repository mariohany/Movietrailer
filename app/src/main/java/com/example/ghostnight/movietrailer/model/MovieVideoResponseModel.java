package com.example.ghostnight.movietrailer.model;

import java.util.ArrayList;

public class MovieVideoResponseModel {
    int id;
    ArrayList<Video> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Video> getResults() {
        return results;
    }

    public void setResults(ArrayList<Video> results) {
        this.results = results;
    }
}


