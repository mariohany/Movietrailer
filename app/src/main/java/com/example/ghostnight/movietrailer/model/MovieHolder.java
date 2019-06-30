package com.example.ghostnight.movietrailer.model;

import java.io.Serializable;

public class MovieHolder implements Serializable {

    private int id;
    private String title;
    private String image;
    private float rating;
    private String videoPath;
    private String overview;
    private String release_date;
    private boolean favorite;

    public MovieHolder(int id, String title, String image, float rating, String videoPath, String overview, String release_date, boolean favorite) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.rating = rating;
        this.videoPath = videoPath;
        this.overview = overview;
        this.release_date = release_date;
        this.favorite = favorite;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}