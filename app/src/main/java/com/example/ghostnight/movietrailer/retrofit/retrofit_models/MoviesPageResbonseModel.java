package com.example.ghostnight.movietrailer.retrofit.retrofit_models;


import java.util.ArrayList;

public class MoviesPageResbonseModel {
    int page;
    ArrayList<MoviesResbonseModel> results;
    int total_results;
    int total_page;
    String status_message;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<MoviesResbonseModel> getResults() {
        return results;
    }

    public void setResults(ArrayList<MoviesResbonseModel> results) {
        this.results = results;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }
}
